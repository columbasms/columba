class AddTitleToCategory < ActiveRecord::Migration
  def change
    add_column :categories, :title, :string, null: false
  end
end
