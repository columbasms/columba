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
                'ezphlEBYcGw:APA91bHL37W4Oa12y59fRVbE-mbEh-HWn73Wofqw3M6esUO74zEoWc8uKYtjPIcQBLGR5N8Kyb2djcqVVbh4T1HZ94JiBc2dv0usZDzXhJKWzpsXtwVBy-Jr-MERd8DkXvWQs4fO9Eqk'
            );

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
