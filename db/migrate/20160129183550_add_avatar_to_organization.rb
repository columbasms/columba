class AddAvatarToOrganization < ActiveRecord::Migration
  def up
    add_attachment :organizations, :avatar
  end

  def down
    remove_attachment :organizations, :avatar
  end
end
