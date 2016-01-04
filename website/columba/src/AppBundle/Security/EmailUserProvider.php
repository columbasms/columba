<?php

namespace AppBundle\Security;

use FOS\UserBundle\Security\UserProvider;

class EmailUserProvider extends UserProvider {

    protected function findUser($username) {

        return $this->userManager->findUserByEmailOrPhoneNumber($username);
    }

}