class AddDescriptionLimitToCampaign < ActiveRecord::Migration
  def change
    change_column :campaigns, :message, :string, limit: 320
  end
end
