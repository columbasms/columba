class AddClientsOrganizationsRelation < ActiveRecord::Migration
  def change
    create_join_table :organizations, :digits_clients do |t|
      t.index :organization_id
      t.index :digits_client_id
    end
  end
end
