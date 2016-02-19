class RenameLongitudeInCampaigns < ActiveRecord::Migration
  def change
    rename_column :campaigns, :longitute, :longitude
  end
end
