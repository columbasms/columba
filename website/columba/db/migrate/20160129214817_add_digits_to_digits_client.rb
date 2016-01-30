class AddDigitsToDigitsClient < ActiveRecord::Migration
  def change
    add_column :digits_clients, :digits_secret, :text
    add_column :digits_clients, :id_str, :string
    add_column :digits_clients, :digits_verification_type, :string
    add_column :digits_clients, :digits_id, :string
  end
end
