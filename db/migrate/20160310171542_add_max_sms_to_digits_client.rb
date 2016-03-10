class AddMaxSmsToDigitsClient < ActiveRecord::Migration
  def change
    add_column :digits_clients, :max_sms, :integer
  end
end
