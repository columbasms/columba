<?php
/**
 * Created by PhpStorm.
 * User: lorenzorapetti
 * Date: 01/01/16
 * Time: 17:14
 */

namespace AppBundle\Entity;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass="AppBundle\Repository\OrganizationRepository")
 */
class Organization extends User {

    /**
     * @ORM\Column(name="organization_name", type="string", length=255)
     */
    protected $organizationName;

    /**
     * @ORM\Column(name="VAT_number", type="string", length=255)
     */
    protected $VATNumber;

    /**
     * @ORM\ManyToMany(targetEntity="Topic", inversedBy="organizations")
     * @ORM\JoinTable(name="organizations_topics")
     */
    private $topics;

    /**
     * @ORM\ManyToMany(targetEntity="Message", inversedBy="organizations")
     * @ORM\JoinTable(name="organizations_messages")
     */
    private $messages;

    public function setEmail($email) {
        $email = is_null($email) ? '' : $email;
        parent::setEmail($email);
        $this->setUsername($email);

        return $this;
    }


    public function __construct() {
        parent::__construct();

        $this->topics = new ArrayCollection();
        $this->messages = new ArrayCollection();
    }

    /**
     * Set organizationName
     *
     * @param string $organizationName
     *
     * @return Organization
     */
    public function setOrganizationName($organizationName)
    {
        $this->organizationName = $organizationName;

        return $this;
    }

    /**
     * Get organizationName
     *
     * @return string
     */
    public function getOrganizationName()
    {
        return $this->organizationName;
    }

    /**
     * Set vATNumber
     *
     * @param string $vATNumber
     *
     * @return Organization
     */
    public function setVATNumber($vATNumber)
    {
        $this->VATNumber = $vATNumber;

        return $this;
    }

    /**
     * Get vATNumber
     *
     * @return string
     */
    public function getVATNumber()
    {
        return $this->VATNumber;
    }

    /**
     * Add topic
     *
     * @param \AppBundle\Entity\Topic $topic
     *
     * @return Organization
     */
    public function addTopic(\AppBundle\Entity\Topic $topic)
    {
        $this->topics[] = $topic;

        return $this;
    }

    /**
     * Remove topic
     *
     * @param \AppBundle\Entity\Topic $topic
     */
    public function removeTopic(\AppBundle\Entity\Topic $topic)
    {
        $this->topics->removeElement($topic);
    }

    /**
     * Get topics
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getTopics()
    {
        return $this->topics;
    }

    /**
     * Add message
     *
     * @param \AppBundle\Entity\Message $message
     *
     * @return Organization
     */
    public function addMessage(\AppBundle\Entity\Message $message)
    {
        $this->messages[] = $message;

        return $this;
    }

    /**
     * Remove message
     *
     * @param \AppBundle\Entity\Message $message
     */
    public function removeMessage(\AppBundle\Entity\Message $message)
    {
        $this->messages->removeElement($message);
    }

    /**
     * Get messages
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getMessages()
    {
        return $this->messages;
    }
}
