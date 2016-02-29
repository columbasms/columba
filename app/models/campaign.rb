class Campaign < ActiveRecord::Base
  include ActiveModel::Serialization

  belongs_to :organization
  belongs_to :town
  belongs_to :province
  belongs_to :region

  has_many :campaign_client_receivers, :dependent => :delete_all
  has_many :receivers, through: :campaign_client_receivers
  has_many :digits_clients, through: :campaign_client_receivers
  has_many :campaign_addresses

  has_and_belongs_to_many :topics

  accepts_nested_attributes_for :campaign_addresses, allow_destroy: true

  has_attached_file :photo, styles: {
      normal: '1280x720>',
      mobile: '800x450>',
  }, default_url: '/images/invalid',
                    convert_options: {
                        normal: '-gravity center -extent 1280x720',
                        mobile: '-gravity center -extent 800x450'
                    }
  validates_attachment_content_type :photo, content_type: /\Aimage\/.*\Z/
  crop_attached_file :photo, aspect: '16:9'

  validates :message, presence: true
  validates :organization, presence: true
  validates_presence_of :organization_id, :topics
  validates :expires_at, presence: true
  validate :expiration_date_cannot_be_in_the_past

  scope :not_expired, -> { where('expires_at >= ?', Date.today) }

  def expired?
    self.expires_at < Date.today
  end

  def deactivate
    self.update_attribute :expires_at, Date.yesterday
  end

  def expires_at_format
    self.expires_at.strftime '%B %-d, %Y' if self.expires_at.present?
  end

  def created_at_format
    self.created_at.strftime('%B %-d, %Y')
  end

  def expiration_date_cannot_be_in_the_past
    if expires_at.present? && expires_at < Date.today
      errors.add(:expires_at, I18n.t('campaigns.errors.cant_be_in_the_past'))
    end
  end

  def photo_mobile
    URI.join(ActionController::Base.asset_host, self.photo.url(:mobile)).to_s
  end

  def photo_normal
    URI.join(ActionController::Base.asset_host, self.photo.url(:normal)).to_s
  end

end
