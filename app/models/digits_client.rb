class DigitsClient < ActiveRecord::Base

  has_many :campaign_client_receivers
  has_many :groups
  has_many :campaigns, through: :campaign_client_receivers
  has_many :digits_clients_organizations
  has_many :organizations, through: :digits_clients_organizations

  has_and_belongs_to_many :topics

  validates :phone_number, presence: true, uniqueness: true
  validates :gcm_token, presence: true
  validates :digits_secret, presence: true
  validates :digits_token, presence: true

  has_attached_file :avatar, styles: {
      normal: '250x250#',
      thumb: '32x32#'
  }, default_url: '/images/avatar.png'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/
  crop_attached_file :avatar

  has_attached_file :cover, styles: {
      normal: '1500x844#'
  }, default_url: '/images/cover.png'
  validates_attachment_content_type :cover, content_type: /\Aimage\/.*\Z/
  crop_attached_file :cover, aspect: '16:9'

  def avatar_normal
    URI.join(ActionController::Base.asset_host, self.avatar.url(:normal)).to_s
  end

  def cover_normal
    URI.join(ActionController::Base.asset_host, self.cover.url(:normal)).to_s
  end

  def user_name
    "user_#{self.id}"
  end
end
