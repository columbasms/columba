<?php

namespace MenuBundle\Menu;

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

    public function mainMenu(FactoryInterface $factory, array $options) {

        $menu = $factory->createItem('Home', array(
            'childrenAttributes' => array(
                'class' => 'menu-items'
            )
        ));

        $menu->addChild('Home', array(
            'route' => 'dashboard',
            'label' => 'Dashboard',
            'childrenAttributes' => array(
                'class' => 'sub-menu'
            )
        ))->setAttribute('icon', 'pg-home');

        $menu->addChild('NewCampaign', array(
            'route' => 'campaign_new',
            'label' => 'New campaign'
        ))->setAttribute('icon', 'pg-plus');

        return $menu;

    }

}