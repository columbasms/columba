class Receiver < ActiveRecord::Base
  has_shortened_urls

  has_many :campaign_client_receivers
  has_many :campaigns, through: :campaign_client_receivers

  has_and_belongs_to_many :groups

  validates :number, presence: true, uniqueness: true

end
