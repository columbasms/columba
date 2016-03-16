class AddDefaultValuesToOrganizationAnalytics < ActiveRecord::Migration
  def change
    change_column :organization_analytics, :follower, :integer, :default => 0
    change_column :organization_analytics, :truster, :integer, :default => 0
    change_column :organization_analytics, :sms_range_general, :integer, :default => 0
    change_column :organization_analytics, :sms_range_follower, :float, :default => 0
    change_column :organization_analytics, :sms_range_truster, :float, :default => 0
    change_column :organization_analytics, :global_supporter, :float, :default => 0
    change_column :organization_analytics, :global_sent_sms, :integer, :default => 0
  end
end
