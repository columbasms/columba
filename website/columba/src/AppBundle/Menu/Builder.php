<?php

namespace AppBundle\Menu;

use Knp\Menu\FactoryInterface;
use Symfony\Component\DependencyInjection\ContainerAwareInterface;
use Symfony\Component\DependencyInjection\ContainerAwareTrait;

class Builder implements ContainerAwareInterface {

    use ContainerAwareTrait;

    public function dashboardMenu(FactoryInterface $factory, array $options) {

        $menu = $factory->createItem('Dashboard', array('route' => 'sonata_admin_dashboard'));

        return $menu;

    }

    public function usersMenu(FactoryInterface $factory, array $options) {

        $menu = $factory->createItem('Users');

        $menu->addChild('Organizations', array('route' => 'admin_app_organization_list'));
        $menu->addChild('Clients', array('route' => 'admin_app_client_list'));

        return $menu;

    }

}