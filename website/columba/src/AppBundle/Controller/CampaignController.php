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
     * @Route("/new", name="campaign_new", methods={"POST", "GET"})
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

            if ($response) {
                return $this->redirectToRoute('campaing_complete');
            } else {
                $this->addFlash('warning', 'There was a problem with your request. Plese try again later');
            }
        }

        return $this->render(':campaign:new.html.twig');
    }

    /**
     * @Route("/complete", name="campaing_complete")
     */
    public function completeAction() {
        return $this->render(':campaign:complete.html.twig');
    }

}
