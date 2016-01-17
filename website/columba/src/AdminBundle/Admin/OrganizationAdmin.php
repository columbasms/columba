<?php

namespace AdminBundle\Admin;

use AppBundle\Entity\Organization;
use Sonata\AdminBundle\Admin\Admin;
use Sonata\AdminBundle\Datagrid\DatagridMapper;
use Sonata\AdminBundle\Datagrid\ListMapper;
use Sonata\AdminBundle\Form\FormMapper;
use Sonata\AdminBundle\Route\RouteCollection;
use Sonata\AdminBundle\Show\ShowMapper;

class OrganizationAdmin extends Admin {

    protected function configureRoutes(RouteCollection $collection)
    {
        $collection->remove('edit');
        $collection->remove('new');
        $collection->remove('create');
        $collection->add('unlock', $this->getRouterIdParameter() . '/unlock');
        $collection->add('lock', $this->getRouterIdParameter() . '/lock');
    }

    protected function configureShowFields(ShowMapper $show)
    {
        $show->add('id')
            ->add('email')
            ->add('organizationName')
            ->add('VATNumber')
            ->add('enabled')
            ->add('locked')
            ->add('lastLogin')
            ->add('expired');

    }


    protected function configureDatagridFilters(DatagridMapper $datagridMapper)
    {
        $datagridMapper->add('id')
            ->add('email')
            ->add('organizationName')
            ->add('VATNumber')
            ->add('enabled')
            ->add('locked')
            ->add('lastLogin')
            ->add('expired');
    }

    protected function configureListFields(ListMapper $listMapper)
    {
        $listMapper->add('id')
            ->addIdentifier('email')
            ->add('organizationName')
            ->add('enabled')
            ->add('locked')
            ->add('_action', 'actions', array(
                'actions' => array(
                    'show' => array(),
                    'delete' => array(),
                    'unlock' => array(
                        'template' => 'AdminBundle:CRUD:list__action_unlock.html.twig'
                    )
                )
            ));
    }

    public function toString($object)
    {
        return $object instanceof Organization
            ? $object->getEmailCanonical()
            : 'Organization'; // shown in the breadcrumb on the create view
    }

}