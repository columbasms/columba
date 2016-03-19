class AddIndexToCampaignAnalytics < ActiveRecord::Migration
  def change
    add_index :campaign_analytics, :created_at
  end
end
