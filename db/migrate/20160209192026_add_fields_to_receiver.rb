class AddFieldsToReceiver < ActiveRecord::Migration
  def change
    add_column :receivers, :blacklisted, :boolean
  end
end
