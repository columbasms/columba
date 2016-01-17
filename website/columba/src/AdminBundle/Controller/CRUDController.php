<?php

namespace AdminBundle\Controller;

use AppBundle\Entity\Organization;
use Sonata\AdminBundle\Controller\CRUDController as Controller;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;

class CRUDController extends Controller {

    public function unlockAction(Request $request) {

        $object = $this->admin->getSubject();

        if (!$object) {
            throw new NotFoundHttpException(sprintf('unable to find the object'));
        }

        if ($object instanceof Organization) {
            $object->setLocked(false);
            $this->getDoctrine()->getManager()->flush();

            $this->addFlash('sonata_flash_success', 'sonata.user.unlocked');

        }

        if (($route = $request->get('currentPath', null))) {
            return new RedirectResponse($route);
        } else {
            return new RedirectResponse($this->admin->generateUrl('list'), 302, $this->admin->getFilterParameters());
        }

    }

    public function lockAction(Request $request) {

        $object = $this->admin->getSubject();

        if (!$object) {
            throw new NotFoundHttpException(sprintf('unable to find the object'));
        }

        if ($object instanceof Organization) {
            $object->setLocked(true);
            $this->getDoctrine()->getManager()->flush();

            $this->addFlash('sonata_flash_success', 'sonata.user.locked');
        }

        if (($route = $request->get('currentPath', null))) {
            return new RedirectResponse($route);
        } else {
            return new RedirectResponse($this->admin->generateUrl('list'), 302, $this->admin->getFilterParameters());
        }
    }

}