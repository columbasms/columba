class AddFieldsToAdminUser < ActiveRecord::Migration
  def change
    add_attachment :admin_users, :avatar
    add_column :admin_users, :name, :string
    add_column :admin_users, :description, :text
  end
end
