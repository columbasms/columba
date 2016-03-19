class RemoveNameFromTopic < ActiveRecord::Migration
  def change
    change_column_null :topics, :name, true
  end
end
