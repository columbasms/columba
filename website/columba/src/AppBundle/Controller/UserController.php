<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class UserController extends Controller {

    /**
     * @Route("/locked", name="account_locked")
     */
    public function lockedAction() {
        return $this->render('user/locked.html.twig');
    }

}
