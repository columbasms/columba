<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Response;

class UserController extends Controller {

    /**
     * @Route("/locked", name="account_locked")
     */
    public function lockedAction() {
        return new Response("<html><body>Account locked</body></html>");
    }

}
