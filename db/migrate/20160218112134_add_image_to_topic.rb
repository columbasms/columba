class AddImageToTopic < ActiveRecord::Migration
  def up
    add_attachment :topics, :image
  end

  def down
    remove_attachment :topics, :image
  end
end
