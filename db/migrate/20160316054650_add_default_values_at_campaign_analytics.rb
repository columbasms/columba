class AddDefaultValuesAtCampaignAnalytics < ActiveRecord::Migration
  def change
    change_column :campaign_analytics, :supporters, :integer, :default => 0
    change_column :campaign_analytics, :sent_sms, :integer, :default => 0
  end
end
