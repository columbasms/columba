class AddIndexToOrganizationAnalytics < ActiveRecord::Migration
  def change
    add_index :organization_analytics, :created_at
  end
end
