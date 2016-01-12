<?php

namespace RestBundle\Controller;

use FOS\RestBundle\Controller\FOSRestController;
//use FOS\RestBundle\Controller;
use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Routing\ClassResourceInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use FOS\RestBundle\Request\ParamFetcher;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use FOS\RestBundle\Controller\Annotations\QueryParam;
use Nelmio\ApiDocBundle\Annotation\ApiDoc;
use Symfony\Component\HttpFoundation\Request;

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
     */
    public function randomAction()
    {
        return array('data');
    }

    public function getDemosAction()
    {
        $data = array("hello" => "world");
        $view = $this->view($data);
        return $this->handleView($view);
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

//        through HTTP Headers
        $provider = $request->headers->get('X-Auth-Service-Provider');
        $auth = $request->headers->get('X-Verify-Credentials-Authorization');

        return json_encode($this->twitterVerifyAuth($provider, $auth));
//        capture oauthEcho request and check it trough twitterVerifyAuth()
//        if ok check if user already in DB or not
//        if not add it
//        return the oauth2 tokens for the user to access the API
    }

    /**
     * oauthEcho of android users' digits credential
     *
     * @param $provider
     * @param $auth
     * @return bool
     */
    private function twitterVerifyAuth($provider, $auth) {
        $curl = curl_init();
        curl_setopt($curl,CURLOPT_URL, $provider);
        curl_setopt($curl,CURLOPT_HTTPHEADER, array(
            'Content-length: 0',
            'Content-type: application/json',
            'Authorization: '.$auth,
        ));
        curl_exec($curl);
        $info = curl_getinfo($curl);
        curl_close($curl);
//        MAKE it return also the user data if respose ==200
        return $info['http_code'] == 200;
    }

}
