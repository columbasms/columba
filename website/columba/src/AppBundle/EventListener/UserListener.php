<?php

namespace AppBundle\EventListener;

use AppBundle\Entity\Organization;
use AppBundle\Entity\User;
use Doctrine\ORM\Event\LifecycleEventArgs;

class UserListener {

    public function prePersist(LifecycleEventArgs $args) {

        /**
         * @var User $entity
         */
        $entity = $args->getEntity();

        if (!$entity instanceof User) return;

        if ($entity instanceof Organization) {
            $entity->setLocked(true);
        }

    }

}