class AddIndexToTopicAnalytics < ActiveRecord::Migration
  def change
    add_index :topic_analytics, :created_at
  end
end
