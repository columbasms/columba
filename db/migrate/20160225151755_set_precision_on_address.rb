class SetPrecisionOnAddress < ActiveRecord::Migration
  def change
    change_column :campaign_addresses, :lat, :decimal, precision: 13, scale: 10
    change_column :campaign_addresses, :lng, :decimal, precision: 13, scale: 10
  end
end
