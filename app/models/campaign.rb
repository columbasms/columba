class Campaign < ActiveRecord::Base
  include ActiveModel::Serialization

  belongs_to :organization
  belongs_to :town
  belongs_to :province
  belongs_to :region

  has_many :campaign_client_receivers, :dependent => :delete_all
  has_many :receivers, through: :campaign_client_receivers
  has_many :digits_clients, through: :campaign_client_receivers

  has_and_belongs_to_many :topics

  validates :message, presence: true
  validates :organization, presence: true
  validates :topics, presence: true
  validates :expires_at, presence: true
  validate :expiration_date_cannot_be_in_the_past

  accepts_nested_attributes_for :topics

  scope :not_expired, -> { where('expires_at >= ?', Date.today) }

  def expired?
    self.expires_at < Date.today
  end

  def expires_at_format
    self.expires_at.strftime '%B %-d, %Y' if self.expires_at.present?
  end

  def created_at_format
    self.created_at.strftime("%B %-d, %Y")
  end

  def as_json(options = nil)
    {
        message: self.message,
        created_at: created_at_format,
        messages_sent: '0'
    }
  end

  def expiration_date_cannot_be_in_the_past
    if expires_at.present? && expires_at < Date.today
      errors.add(:expires_at, "can't be in the past")
    end
  end

end
