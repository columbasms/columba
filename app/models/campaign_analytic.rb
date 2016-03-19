class CampaignAnalytic < ActiveRecord::Base

  belongs_to :campaign

  validates :created_at, uniqueness: { scope: :campaign_id }

end
