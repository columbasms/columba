class Campaign < ActiveRecord::Base
  include ActiveModel::Serialization

  belongs_to :organization
  belongs_to :town
  belongs_to :province
  belongs_to :region

  has_many :campaign_client_receivers
  has_many :receivers, through: :campaign_client_receivers
  has_many :digits_clients, through: :campaign_client_receivers

  has_and_belongs_to_many :topics

  validates :message, presence: true
  validates :organization, presence: true
  validates :topics, presence: true

  accepts_nested_attributes_for :topics

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

end
