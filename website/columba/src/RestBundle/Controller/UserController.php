<?php

namespace RestBundle\Controller;

use FOS\RestBundle\Controller\Annotations as Rest;
use FOS\RestBundle\Routing\ClassResourceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class UserController extends Controller implements ClassResourceInterface {

    /**
     * @Rest\View()
     */
    public function cgetAction() {
        $data = $this->getDoctrine()->getManager()
            ->getRepository('AppBundle:User')->findAll();

        return $data;
    }

    /**
     * @Rest\Get("/random")
     * @Rest\View()
     */
    public function randomAction() {
        return array('data');
    }

}
