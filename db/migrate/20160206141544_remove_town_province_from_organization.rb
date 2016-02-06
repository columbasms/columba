class RemoveTownProvinceFromOrganization < ActiveRecord::Migration
  def change
    remove_column :organizations, :town
    remove_column :organizations, :province
  end
end
