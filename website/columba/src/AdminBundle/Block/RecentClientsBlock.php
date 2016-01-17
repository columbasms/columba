<?php
/**
 * Created by PhpStorm.
 * User: lorenzorapetti
 * Date: 17/01/16
 * Time: 15:40
 */

namespace AdminBundle\Block;

use Doctrine\Bundle\DoctrineBundle\Registry;
use Doctrine\Common\Collections\Criteria;
use Sonata\BlockBundle\Block\BaseBlockService;
use Sonata\BlockBundle\Block\BlockContextInterface;
use Symfony\Bundle\FrameworkBundle\Templating\EngineInterface;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\OptionsResolver\OptionsResolver;

class RecentClientsBlock extends BaseBlockService {

    /**
     * @var Registry
     */
    private $doctrine;

    public function __construct($name, EngineInterface $templating, $doctrine)
    {
        parent::__construct($name, $templating);
        $this->doctrine = $doctrine;
    }

    public function configureSettings(OptionsResolver $resolver) {
        $resolver->setDefaults(array(
            'number' => 5,
            'title' => 'Recent Clients',
            'template' => 'AdminBundle:Block:recent_clients.html.twig'
        ));
    }

    public function execute(BlockContextInterface $blockContext, Response $response = null) {
        $settings = $blockContext->getSettings();

        $clients = $this->doctrine->getManager()->getRepository('AppBundle:Client')
            ->matching(Criteria::create()->orderBy(array('createdAt' => Criteria::DESC))->setMaxResults($settings['number']));

        return $this->renderResponse($blockContext->getTemplate(), array(
            'clients' => $clients,
            'block'     => $blockContext->getBlock(),
            'settings' => $settings
        ), $response);
    }


}