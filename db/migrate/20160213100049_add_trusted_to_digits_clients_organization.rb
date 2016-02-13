class AddTrustedToDigitsClientsOrganization < ActiveRecord::Migration
  def change
    add_column :digits_clients_organizations, :trusted, :boolean
  end
end
