<?php
/**
 * Created by PhpStorm.
 * User: lorenzorapetti
 * Date: 01/01/16
 * Time: 17:13
 */

namespace AppBundle\Entity;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass="AppBundle\Repository\ClientRepository")
 */
class Client extends User {

    /**
     * @ORM\Column(name="first_name", type="string", length=255, nullable=true)
     */
    protected $firstName;

    /**
     * @ORM\Column(name="last_name", type="string", length=255, nullable=true)
     */
    protected $lastName;

    /**
     * @ORM\Column(name="messages_number", type="integer", options={"default" = 0})
     */
    protected $messagesNumber;

    /**
     * @ORM\Column(name="gcm_token", type="string", length=1000)
     */
    protected $gcmToken;

    /**
     * @ORM\ManyToMany(targetEntity="Contact", inversedBy="clients")
     * @ORM\JoinTable(name="clients_contacts")
     */
    private $contacts;

    /**
     * @ORM\ManyToMany(targetEntity="Topic", inversedBy="clients")
     * @ORM\JoinTable(name="clients_topics")
     */
    private $topics;


    public function __construct() {
        parent::__construct();

        $this->contacts = new ArrayCollection();
        $this->topics = new ArrayCollection();
    }

    /**
     * Set firstName
     *
     * @param string $firstName
     *
     * @return Client
     */
    public function setFirstName($firstName)
    {
        $this->firstName = $firstName;

        return $this;
    }

    /**
     * Get firstName
     *
     * @return string
     */
    public function getFirstName()
    {
        return $this->firstName;
    }

    /**
     * Set lastName
     *
     * @param string $lastName
     *
     * @return Client
     */
    public function setLastName($lastName)
    {
        $this->lastName = $lastName;

        return $this;
    }

    /**
     * Get lastName
     *
     * @return string
     */
    public function getLastName()
    {
        return $this->lastName;
    }

    /**
     * Set messagesNumber
     *
     * @param integer $messagesNumber
     *
     * @return Client
     */
    public function setMessagesNumber($messagesNumber)
    {
        $this->messagesNumber = $messagesNumber;

        return $this;
    }

    /**
     * Get messagesNumber
     *
     * @return integer
     */
    public function getMessagesNumber()
    {
        return $this->messagesNumber;
    }

    /**
     * Add contact
     *
     * @param \AppBundle\Entity\Contact $contact
     *
     * @return Client
     */
    public function addContact(\AppBundle\Entity\Contact $contact)
    {
        $this->contacts[] = $contact;

        return $this;
    }

    /**
     * Remove contact
     *
     * @param \AppBundle\Entity\Contact $contact
     */
    public function removeContact(\AppBundle\Entity\Contact $contact)
    {
        $this->contacts->removeElement($contact);
    }

    /**
     * Get contacts
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getContacts()
    {
        return $this->contacts;
    }

    /**
     * Add topic
     *
     * @param \AppBundle\Entity\Topic $topic
     *
     * @return Client
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
     * Set gcmToken
     *
     * @param string $gcmToken
     *
     * @return Client
     */
    public function setGcmToken($gcmToken)
    {
        $this->gcmToken = $gcmToken;

        return $this;
    }

    /**
     * Get gcmToken
     *
     * @return string
     */
    public function getGcmToken()
    {
        return $this->gcmToken;
    }
}
