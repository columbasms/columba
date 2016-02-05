class AddRelationsToCampaignAndTopic < ActiveRecord::Migration
  def change
    create_join_table :campaigns, :topics do |t|
      t.index :campaign_id
      t.index :topic_id
    end
  end
end
