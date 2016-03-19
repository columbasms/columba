class AddLatLngToOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :lat, :decimal, precision: 13, scale: 10
    add_column :organizations, :lng, :decimal, precision: 13, scale: 10
  end
end
