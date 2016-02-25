class CampaignAddress < ActiveRecord::Base

  belongs_to :campaign

  validates_presence_of :address

end
