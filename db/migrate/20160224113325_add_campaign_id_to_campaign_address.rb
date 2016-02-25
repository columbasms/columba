class AddCampaignIdToCampaignAddress < ActiveRecord::Migration
  def change
    add_column :campaign_addresses, :campaign_id, :integer
    add_foreign_key :campaign_addresses, :campaigns
  end
end
