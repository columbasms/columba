class RevertTopics < ActiveRecord::Migration
  def change
    create_join_table :organizations, :topics do |t|
      t.index :organization_id
      t.index :topic_id
    end
    remove_foreign_key :organizations, :topics
    remove_column :organizations, :topic_id

    create_join_table :campaigns, :topics do |t|
      t.index :campaign_id
      t.index :topic_id
    end

  end
end
