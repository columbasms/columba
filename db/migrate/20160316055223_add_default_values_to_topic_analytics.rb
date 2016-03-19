class AddDefaultValuesToTopicAnalytics < ActiveRecord::Migration
  def change
    change_column :topic_analytics, :followers, :integer, :default => 0
    change_column :topic_analytics, :sms_range, :float, :default => 0
  end
end
