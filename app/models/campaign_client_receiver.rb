class CampaignClientReceiver < ActiveRecord::Base

  belongs_to :campaign
  belongs_to :digits_client
  belongs_to :receiver

  validates_associated :campaign
  validates_associated :receiver
  validates_associated :digits_client

end
