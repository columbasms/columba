class AddDefaultValueToDigitsClientSmsMax < ActiveRecord::Migration
  def change
    change_column :digits_clients, :max_sms, :integer, :default => 50
  end
end
