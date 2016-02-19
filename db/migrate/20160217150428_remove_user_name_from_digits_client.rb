class RemoveUserNameFromDigitsClient < ActiveRecord::Migration
  def change
    remove_column :digits_clients, :user_name
  end
end
