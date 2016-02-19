class AddUserNameToDigitsClients < ActiveRecord::Migration
  def change
    add_column :digits_clients, :user_name, :string
  end
end
