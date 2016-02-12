class DigitsClient < ActiveRecord::Base

  has_many :campaign_client_receivers
  has_many :groups
  has_many :campaigns, through: :campaign_client_receivers

  has_and_belongs_to_many :topics
  has_and_belongs_to_many :organizations

  validates :phone_number, presence: true, uniqueness: true
  validates :gcm_token, presence: true
  validates :digits_secret, presence: true
  validates :digits_token, presence: true

end
