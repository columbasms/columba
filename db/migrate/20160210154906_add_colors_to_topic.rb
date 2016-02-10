class AddColorsToTopic < ActiveRecord::Migration
  def change
    add_column :topics, :main_color, :string
    add_column :topics, :status_color, :string
  end
end
