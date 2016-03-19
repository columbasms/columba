class CampaignClientReceiver < ActiveRecord::Base

  belongs_to :campaign
  belongs_to :digits_client
  belongs_to :receiver

  validates_associated :campaign
  validates_presence_of :campaign_id
  validates_associated :receiver
  validates_presence_of :receiver_id
  validates_associated :digits_client
  validates_presence_of :digits_client_id

end
