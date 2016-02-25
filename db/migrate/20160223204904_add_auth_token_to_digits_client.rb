class AddAuthTokenToDigitsClient < ActiveRecord::Migration
  def change
    add_column :digits_clients, :auth_token, :string, null: false
  end
end
