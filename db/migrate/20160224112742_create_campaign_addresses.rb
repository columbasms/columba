class CreateCampaignAddresses < ActiveRecord::Migration
  def change
    create_table :campaign_addresses do |t|
      t.string :address, null: false
      t.timestamps null: false
    end

    remove_column :campaigns, :address
  end
end
