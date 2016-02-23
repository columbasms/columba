class Campaign < ActiveRecord::Base
  include ActiveModel::Serialization

  belongs_to :organization
  belongs_to :town
  belongs_to :province
  belongs_to :region

  has_many :campaign_client_receivers, :dependent => :delete_all
  has_many :receivers, through: :campaign_client_receivers
  has_many :digits_clients, through: :campaign_client_receivers

  validates :message, presence: true
  validates :organization, presence: true
  validates_presence_of :organization_id
  validates :expires_at, presence: true
  validate :expiration_date_cannot_be_in_the_past

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

  def expiration_date_cannot_be_in_the_past
    if expires_at.present? && expires_at < Date.today
      errors.add(:expires_at, I18n.t('campaigns.errors.cant_be_in_the_past'))
    end
  end

end
