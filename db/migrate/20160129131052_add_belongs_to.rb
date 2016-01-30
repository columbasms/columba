class AddBelongsTo < ActiveRecord::Migration
  def change
    add_column :campaigns, :organization_id, :integer
    add_foreign_key :campaigns, :organizations

    add_column :campaign_client_receivers, :campaign_id, :integer
    add_foreign_key :campaign_client_receivers, :campaigns
    add_column :campaign_client_receivers, :digits_client_id, :integer
    add_foreign_key :campaign_client_receivers, :digits_clients
    add_column :campaign_client_receivers, :receiver_id, :integer
    add_foreign_key :campaign_client_receivers, :receivers

    add_column :groups, :digits_client_id, :integer
    add_foreign_key :groups, :digits_clients
  end
end
