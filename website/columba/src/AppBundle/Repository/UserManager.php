<?php

namespace AppBundle\Repository;

use AppBundle\Entity\Organization;
use AppBundle\Entity\Client;
use FOS\UserBundle\Model\UserInterface;

class UserManager extends \FOS\UserBundle\Doctrine\UserManager {

    /**
     * Finds a user either by email, or phone number
     *
     * @param string $emailOrPhone
     *
     * @return UserInterface
     */
    public function findUserByEmailOrPhoneNumber($emailOrPhone)
    {
        if (filter_var($emailOrPhone, FILTER_VALIDATE_EMAIL)) {
            return $this->findUserByEmail($emailOrPhone);
        }

        return $this->findUserByPhoneNumber($emailOrPhone);
    }

    public function findUserByPhoneNumber($number) {
        return $this->findUserBy(array('phoneNumber' => $number));
    }

    public function createOrganization() {
        return new Organization();
    }

    public function createClient(){
        return new Client();
    }
}