class RemoveCampaignTopicJoinTable < ActiveRecord::Migration
  def change
    drop_join_table :campaigns, :topics
  end
end
