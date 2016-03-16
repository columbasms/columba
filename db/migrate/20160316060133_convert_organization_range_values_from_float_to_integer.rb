class ConvertOrganizationRangeValuesFromFloatToInteger < ActiveRecord::Migration
  def change
    change_column :organization_analytics, :sms_range_follower, :integer
    change_column :organization_analytics, :sms_range_truster, :integer
    change_column :organization_analytics, :global_supporter, :integer
  end
end
