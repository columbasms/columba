class AddGeolocalizationFields < ActiveRecord::Migration
  def change
    add_column :organizations, :town_id, :integer
    add_foreign_key :organizations, :towns

    add_column :campaigns, :town_id, :integer
    add_foreign_key :campaigns, :towns
    add_column :campaigns, :province_id, :integer
    add_foreign_key :campaigns, :provinces
    add_column :campaigns, :region_id, :integer
    add_foreign_key :campaigns, :regions
    add_column :campaigns, :address, :string
    add_column :campaigns, :latitude, :decimal
    add_column :campaigns, :longitute, :decimal
  end
end
