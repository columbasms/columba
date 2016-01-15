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

            $registrationIds = array(
                'cIOyC3Hyhnw:APA91bFIoH4St_JMeUo8ENnDW3wbjtKdfeJgYz6jrRmtgtSjq3r3njDjTn-lCOBapxcHU0r-Y5sLtCE7I1DGsAGLMd_eLQeptEdOQVVdESgavT_N217dN4ewYPn8-l8L3CdTvuHR6GPi'
            );

            $data = array(
                'title' => 'Message title',
                'message' => $message,
            );

            $response = $client->sendTo($data, $registrationIds);

            return new Response(var_dump($response));
        }

        return $this->render('AppBundle:Campaign:new.html.twig');
    }

}
