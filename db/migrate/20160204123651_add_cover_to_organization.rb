class AddCoverToOrganization < ActiveRecord::Migration
  def up
    add_attachment :organizations, :cover
  end

  def self.down
    remove_attachment :organizations, :cover
  end
end
