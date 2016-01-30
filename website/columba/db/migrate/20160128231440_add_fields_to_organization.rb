class AddFieldsToOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :organization_name, :string
    add_column :organizations, :VAT_number, :string
  end
end
