class CreateTopicAnalytics < ActiveRecord::Migration
  def change
    create_table :topic_analytics do |t|

      t.integer :followers
      t.float :sms_range

      t.timestamps null: false
    end

    add_column :topic_analytics, :topic_id, :integer
    add_foreign_key :topic_analytics, :topics
  end
end
