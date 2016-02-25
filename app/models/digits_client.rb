class DigitsClient < ActiveRecord::Base
  has_secure_token :auth_token

  has_many :campaign_client_receivers
  has_many :groups
  has_many :campaigns, through: :campaign_client_receivers
  has_many :digits_clients_organizations
  has_many :organizations, through: :digits_clients_organizations

  has_and_belongs_to_many :topics

  validates_presence_of :phone_number, :gcm_token, :digits_secret, :digits_id, :id_str,
                        :digits_verification_type, :digits_token, :user_name
  validates_uniqueness_of :phone_number, :user_name

  has_attached_file :avatar, styles: {
      normal: '250x250#',
      thumb: '32x32#'
  }, default_url: '/images/invalid'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/

  has_attached_file :cover, styles: {
      normal: '1500x844#'
  }, default_url: '/images/invalid'
  validates_attachment_content_type :cover, content_type: /\Aimage\/.*\Z/

  def avatar_normal
    URI.join(ActionController::Base.asset_host, self.avatar.url(:normal)).to_s
  end

  def cover_normal
    URI.join(ActionController::Base.asset_host, self.cover.url(:normal)).to_s
  end

  def user_name
    self[:user_name] || "user_#{self.id}"
  end

  def organizations_count
    self.organizations.count
  end

  def forwarded_campaigns_count
    self.campaign_client_receivers.select(:campaign_id).uniq.count
  end
end
