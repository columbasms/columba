class AddFieldsToCampaign < ActiveRecord::Migration
  def change
    add_attachment :campaigns, :photo
    add_column :campaigns, :long_description, :text
  end
end
