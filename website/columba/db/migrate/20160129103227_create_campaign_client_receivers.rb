class CreateCampaignClientReceivers < ActiveRecord::Migration
  def change
    create_table :campaign_client_receivers do |t|

      t.timestamps null: false
    end
  end
end
