<?php

namespace AppBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * @Route("/campaign")
 */
class CampaignController extends Controller
{
    /**
     * @Route("/new", methods={"POST", "GET"})
     */
    public function newAction(Request $request) {

        if ($request->getMethod() == 'POST') {
            $message = $request->request->get('message');

            $client = $this->get('endroid.gcm.client');

            $clients = $this->getDoctrine()->getManager()->getRepository('AppBundle:Client')->findAll();

            $registrationIds = array();

            foreach ($clients as $key => $value) {
                $registrationIds[] = $value->getGcmToken();
            }

            $data = array(
                'title' => 'Message title',
                'message' => $message,
            );

            $response = $client->send($data, $registrationIds);

            return new Response(var_dump($response));
        }

        return $this->render('AppBundle:Campaign:new.html.twig');
    }

}
