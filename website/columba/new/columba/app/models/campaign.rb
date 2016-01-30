class Campaign < ActiveRecord::Base

  belongs_to :organization

  has_many :campaign_client_receivers
  has_many :receivers, through: :campaign_client_receivers

  has_and_belongs_to_many :digits_clients

  validates :message, presence: true
  validates_associated :organization

end
