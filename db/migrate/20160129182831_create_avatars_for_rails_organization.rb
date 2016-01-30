class CreateAvatarsForRailsOrganization < ActiveRecord::Migration
  def up    
    add_attachment :organizations, :logo
  end
  
  def self.down
    remove_attachment :organizations, :logo
  end
end
