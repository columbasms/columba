<?php

namespace AppBundle\EventListener;

use FOS\UserBundle\Event\FormEvent;
use FOS\UserBundle\FOSUserEvents;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;

class FOSUserListener implements EventSubscriberInterface {

    private $router;

    public function __construct(UrlGeneratorInterface $router) {
        $this->router = $router;
    }

    public static function getSubscribedEvents() {
        return array(
            FOSUserEvents::REGISTRATION_SUCCESS => 'onRegistrationSuccess'
        );
    }

    public function onRegistrationSuccess(FormEvent $event) {
        $url = $this->router->generate('account_locked');

        $event->setResponse(new RedirectResponse($url));
    }

}