class AddExpirationToCampaign < ActiveRecord::Migration
  def change
    add_column :campaigns, :expires_at, :date
  end
end
