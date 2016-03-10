class AddPositionToCampaignClientReceivers < ActiveRecord::Migration
  def change
    add_column :campaign_client_receivers, :lat, :decimal, precision: 13, scale: 10
    add_column :campaign_client_receivers, :lng, :decimal, precision: 13, scale: 10
  end
end
