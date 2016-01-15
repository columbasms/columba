<?php

namespace RestBundle\Controller;

use FOS\RestBundle\Controller\FOSRestController;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Routing\ClassResourceInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use FOS\RestBundle\Request\ParamFetcher;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use FOS\RestBundle\Controller\Annotations\QueryParam;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Security\Core\Authentication\Token\UsernamePasswordToken;
use Symfony\Component\Security\Http\Authentication;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
//use AppBundle\Entity\Client;

class UserController extends Controller implements ClassResourceInterface {
//class UserController extends FOSRestController {

//    /**
//     * @Rest\View()
//     */
//    public function cgetAction()
//    {
//        $data = $this->getDoctrine()->getManager()
//            ->getRepository('AppBundle:User')->findAll();
//
//        return $data;
//    }

    /**
     * @Rest\Get("/random")
     * @Rest\View()
     *
     *
     */
    public function randomAction(Request $request)
    {
        return array('data');
    }

    /**
     *
     * @Rest\Get("/demo")
     * @Rest\View()
     *
     */
    public function getDemosAction()
    {
        $data = array("hello" => "world");
        return $data;
    }

    /**
     * @Rest\Post("/register")
     * @Rest\View()
     *
     * @QueryParam(name="provider", nullable=false, description="oauthEcho who to call")
     * @QueryParam(name="auth", nullable=false, description="oauthEcho")
     *
     * @param ParamFetcher $paramFetcher
     *
     * @return string
     * @ApiDoc()
     */
    public function postRegisterAndroidUserAction(ParamFetcher $paramFetcher, Request $request)
    {

//        though HTTP Parmas
//        $provider = $paramFetcher->get('provider');
//        $auth = $paramFetcher->get('auth');

//        capture oauthEcho request and check it trough twitterVerifyAuth()
//        through HTTP Headers
        $provider = $request->headers->get('X-Auth-Service-Provider');
        $auth = $request->headers->get('X-Verify-Credentials-Authorization');

        $digitsCredential = $this->twitterVerifyAuthCurl($provider, $auth);
//          check if response is 200
        if ($digitsCredential[0]['http_code']!=200){
            return json_decode($digitsCredential[1],true);
        }
        $digitsCredential = json_decode($digitsCredential[1], true);

//         check if user already in DB or not
        if ($this->searchUserInDB($digitsCredential['phone_number']))
            return 'Provided user already exists';

//        add user to DB
        $idAndPass=$this->registerUserToDB($digitsCredential);

//        return the oauth2 tokens for the user to access the API
//        return $idAndPass;
        return json_encode(['client_id'=>$idAndPass[0],'client_secret'=>$idAndPass[1],'user_name'=>$idAndPass[2],'user_psw'=>$idAndPass[3]]);
    }

    /**
     * oauthEcho of android users' digits credential
     *
     * @param $provider
     * @param $auth
     * @return array:   -info:      infos of http response
     *                  -content:   digits credentials
     */
    private function twitterVerifyAuthCurl($provider, $auth) {
        $curl = curl_init();
        curl_setopt($curl,CURLOPT_URL, $provider);
        curl_setopt($curl,CURLOPT_HTTPHEADER, array(
            'Content-length: 0',
            'Content-type: application/json',
            'Authorization: '.$auth,
        ));
        curl_setopt($curl,CURLOPT_RETURNTRANSFER, true);
        $content = curl_exec($curl);
        $info = curl_getinfo($curl);
        curl_close($curl);
//        MAKE it return also the user data if respose ==200
//        return $info['http_code'] == 200;
//        return $content;
        return [$info,$content];
    }

    /**
     * TO-CORRECT Checks is the user that we want to register is already in our DataBase
     *
     * @param $userId
     * @return bool
     */
    private function searchUserInDB($userId){
        $userManager = $this->get('fos_user.user_manager');
        return $userManager->findUserByPhoneNumber($userId);
//        $newUser = $this->getDoctrine()
//            ->getRepository('AppBundle:Client')
//            ->find($userId);
//        return $newUser;
    }

    /**
     * Add the new user to the DataBase
     *
     * @param $digitsCredential
     * @return int
     */
    private function registerUserToDB($digitsCredential){
        $userManager = $this->get('fos_user.user_manager');
        $tokenGenerator = $this->get('fos_user.util.token_generator');


        $newUser = $userManager->createClient();
        $newUser->setUsername($digitsCredential['id_str']);
        $newUser->setEmail('null');
        $newUser->setPassword(substr($tokenGenerator->generateToken(), 0, 25));
        $newUser->setPhoneNumber($digitsCredential['phone_number']);
        $newUser->setRoles(['ROLE_API']);
        $newUser->setEnabled(true);

        $em = $this->getDoctrine()->getManager();
        $em->persist($newUser);
        $em->flush();

//        $token=exec('fos:oauth-server:client:create --grant-type="authorization_code" --grant-type="password" --grant-type="refresh_token" --grant-type="token" --grant-type="client_credentials"');
        $oauthTokens=$this->createOauthClient(['authorization_code','password','refresh_token','client_credentials']);
//        $token = new UsernamePasswordToken($newUser, $newUser->getPassword(), 'api_resource', $newUser->getRoles());
//
//        $this->get('security.token_storage')->setToken($token);


//        return ['client_id','client_secret','user_name','user_psw']
//        return [$newUser->getId(),$token,$newUser->getUsername(),$newUser->getPassword()];
        return [$oauthTokens['client_id'],$oauthTokens['client_secret'],$newUser->getUsername(),$newUser->getPassword()];
//        return $token;
    }

    /**
     * create an oauth client for the user
     * @param $grantTypes
     * @return array
     */
    private function createOauthClient($grantTypes)
    {
        $clientManager = $this->get('fos_oauth_server.client_manager.default');
        $client = $clientManager->createClient();
//        $client->setRedirectUris($input->getOption('redirect-uri'));
        $client->setAllowedGrantTypes($grantTypes);
        $clientManager->updateClient($client);
        return ['client_id'=>$client->getPublicId(),'client_secret'=>$client->getSecret()];

    }

}
