class Receiver < ActiveRecord::Base

  has_many :campaign_client_receivers
  has_many :campaigns, through: :campaign_client_receivers

  has_and_belongs_to_many :groups

  validates :number, presence: true, uniqueness: true
  validates_presence_of :blacklisted

end
