class AddLockedAtOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :locked_at, :datetime
  end
end
