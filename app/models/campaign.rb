class Campaign < ActiveRecord::Base

  belongs_to :organization

  has_many :campaign_client_receivers
  has_many :receivers, through: :campaign_client_receivers

  has_and_belongs_to_many :digits_clients
  has_and_belongs_to_many :topics

  validates :message, presence: true
  validates :organization, presence: true
  validates :topics, presence: true

  accepts_nested_attributes_for :topics

end
