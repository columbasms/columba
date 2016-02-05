class AddFieldToOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :fiscal_code, :string
    add_column :organizations, :province, :string
    add_column :organizations, :town, :string
    add_column :organizations, :address, :string
    add_column :organizations, :postal_code, :integer
    add_column :organizations, :phone_number, :string
  end
end
