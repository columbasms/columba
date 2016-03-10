class CreateCampaignAnalytics < ActiveRecord::Migration
  def change
    create_table :campaign_analytics do |t|

      t.integer :supporters
      t.integer :sent_sms

      t.timestamps null: false
    end

    add_column :campaign_analytics, :campaign_id, :integer
    add_foreign_key :campaign_analytics, :campaigns
  end
end
