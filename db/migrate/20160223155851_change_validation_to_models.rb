class ChangeValidationToModels < ActiveRecord::Migration
  def change
    change_column_null :campaigns, :message, false
    change_column_null :campaigns, :organization_id, false
    change_column_null :campaigns, :expires_at, false

    change_column_null :digits_clients, :phone_number, false
    change_column_null :digits_clients, :gcm_token, false
    change_column_null :digits_clients, :digits_token, false
    change_column_null :digits_clients, :digits_secret, false
    change_column_null :digits_clients, :digits_id, false
    change_column_null :digits_clients, :user_name, false
    change_column :digits_clients, :phone_number, :string, unique: true
    change_column :digits_clients, :user_name, :string, unique: true

    change_column_null :digits_clients_organizations, :trusted, false
    change_column_default :digits_clients_organizations, :trusted, '0'

    change_column_null :organizations, :organization_name, false
    change_column_null :organizations, :fiscal_code, false
    change_column_null :organizations, :address, false
    change_column_null :organizations, :postal_code, false
    change_column_null :organizations, :phone_number, false
    change_column_null :organizations, :visible, false
    change_column_null :organizations, :town_id, false
    change_column_default :organizations, :visible, '0'
    change_column :organizations, :fiscal_code, :string, unique: true
    change_column :organizations, :email, :string, unique: true

    change_column_null :provinces, :name, false
    change_column_null :provinces, :code, false

    change_column_null :receivers, :number, false
    change_column_null :receivers, :blacklisted, false
    change_column_default :receivers, :blacklisted, '0'
    change_column :receivers, :number, :string, unique: true

    change_column_null :regions, :name, false

    change_column_null :topics, :name, false
    change_column_null :topics, :main_color, false
    change_column_null :topics, :status_color, false
    change_column :topics, :name, :string, unique: true

    change_column_null :towns, :name, false
  end
end
