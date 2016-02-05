class AddVisibleToOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :visible, :boolean
  end
end
