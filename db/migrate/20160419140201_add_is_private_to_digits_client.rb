class AddIsPrivateToDigitsClient < ActiveRecord::Migration
  def change
    add_column :digits_clients, :is_private, :boolean, :null => false, :default => false
  end
end
