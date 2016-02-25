class AddLatLngToCampaignAddress < ActiveRecord::Migration
  def change
    add_column :campaign_addresses, :lat, :decimal
    add_column :campaign_addresses, :lng, :decimal
    remove_column :campaigns, :latitude
    remove_column :campaigns, :longitude
  end
end
