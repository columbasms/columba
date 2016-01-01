<?php

namespace AppBundle\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Gedmo\Mapping\Annotation as Gedmo;

/**
 * Message
 *
 * @ORM\Table(name="message")
 * @ORM\Entity(repositoryClass="AppBundle\Repository\MessageRepository")
 */
class Message
{
    /**
     * @var int
     *
     * @ORM\Column(name="id", type="integer")
     * @ORM\Id
     * @ORM\GeneratedValue(strategy="AUTO")
     */
    private $id;

    /**
     * @var string
     *
     * @ORM\Column(name="message", type="string", length=160)
     */
    private $message;

    /**
     * @Gedmo\Timestampable(on="create")
     * @ORM\Column(name="created_at", type="datetime")
     */
    protected $createdAt;

    /**
     * @Gedmo\Timestampable(on="update")
     * @ORM\Column(name="updated_at", type="datetime")
     */
    protected $updatedAt;

    /**
     * @ORM\ManyToMany(targetEntity="Organization", mappedBy="messages")
     */
    private $organizations;

    /**
     * @ORM\ManyToOne(targetEntity="Columba\LocationBundle\Entity\Region", inversedBy="messages")
     * @ORM\JoinColumn(name="region_id", referencedColumnName="id")
     */
    private $region;

    /**
     * @ORM\ManyToOne(targetEntity="Columba\LocationBundle\Entity\Province", inversedBy="messages")
     * @ORM\JoinColumn(name="province_id", referencedColumnName="id")
     */
    private $province;

    /**
     * @ORM\ManyToOne(targetEntity="Columba\LocationBundle\Entity\Town", inversedBy="messages")
     * @ORM\JoinColumn(name="town_id", referencedColumnName="id")
     */
    private $town;


    public function __construct() {
        $this->organizations = new ArrayCollection();
    }

    /**
     * Get id
     *
     * @return int
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Set message
     *
     * @param string $message
     *
     * @return Message
     */
    public function setMessage($message)
    {
        $this->message = $message;

        return $this;
    }

    /**
     * Get message
     *
     * @return string
     */
    public function getMessage()
    {
        return $this->message;
    }

    /**
     * Add organization
     *
     * @param \AppBundle\Entity\Organization $organization
     *
     * @return Message
     */
    public function addOrganization(\AppBundle\Entity\Organization $organization)
    {
        $this->organizations[] = $organization;

        return $this;
    }

    /**
     * Remove organization
     *
     * @param \AppBundle\Entity\Organization $organization
     */
    public function removeOrganization(\AppBundle\Entity\Organization $organization)
    {
        $this->organizations->removeElement($organization);
    }

    /**
     * Get organizations
     *
     * @return \Doctrine\Common\Collections\Collection
     */
    public function getOrganizations()
    {
        return $this->organizations;
    }

    /**
     * Set region
     *
     * @param \Columba\LocationBundle\Entity\Region $region
     *
     * @return Message
     */
    public function setRegion(\Columba\LocationBundle\Entity\Region $region = null)
    {
        $this->region = $region;

        return $this;
    }

    /**
     * Get region
     *
     * @return \Columba\LocationBundle\Entity\Region
     */
    public function getRegion()
    {
        return $this->region;
    }

    /**
     * Set province
     *
     * @param \Columba\LocationBundle\Entity\Province $province
     *
     * @return Message
     */
    public function setProvince(\Columba\LocationBundle\Entity\Province $province = null)
    {
        $this->province = $province;

        return $this;
    }

    /**
     * Get province
     *
     * @return \Columba\LocationBundle\Entity\Province
     */
    public function getProvince()
    {
        return $this->province;
    }

    /**
     * Set town
     *
     * @param \Columba\LocationBundle\Entity\Town $town
     *
     * @return Message
     */
    public function setTown(\Columba\LocationBundle\Entity\Town $town = null)
    {
        $this->town = $town;

        return $this;
    }

    /**
     * Get town
     *
     * @return \Columba\LocationBundle\Entity\Town
     */
    public function getTown()
    {
        return $this->town;
    }

    /**
     * Set createdAt
     *
     * @param \DateTime $createdAt
     *
     * @return Message
     */
    public function setCreatedAt($createdAt)
    {
        $this->createdAt = $createdAt;

        return $this;
    }

    /**
     * Get createdAt
     *
     * @return \DateTime
     */
    public function getCreatedAt()
    {
        return $this->createdAt;
    }

    /**
     * Set updatedAt
     *
     * @param \DateTime $updatedAt
     *
     * @return Message
     */
    public function setUpdatedAt($updatedAt)
    {
        $this->updatedAt = $updatedAt;

        return $this;
    }

    /**
     * Get updatedAt
     *
     * @return \DateTime
     */
    public function getUpdatedAt()
    {
        return $this->updatedAt;
    }
}
