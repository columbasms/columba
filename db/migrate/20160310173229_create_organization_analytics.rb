class CreateOrganizationAnalytics < ActiveRecord::Migration
  def change
    create_table :organization_analytics do |t|

      t.integer :follower
      t.integer :truster
      t.float :sms_range_general
      t.float :sms_range_follower
      t.float :sms_range_truster
      t.integer :global_supporter
      t.integer :global_sent_sms

      t.timestamps null: false
    end

    add_column :organization_analytics, :organization_id, :integer
    add_foreign_key :organization_analytics, :organizations
  end
end
