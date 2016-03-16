class ConvertRangeTopicValuesFromFloadToInteger < ActiveRecord::Migration
  def change
    change_column :topic_analytics, :sms_range, :integer
  end
end
