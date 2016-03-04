class AddAttachmentToPost < ActiveRecord::Migration
  def change
    add_attachment :posts, :photo
  end
end
