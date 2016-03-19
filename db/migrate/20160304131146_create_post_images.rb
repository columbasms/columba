class CreatePostImages < ActiveRecord::Migration
  def change
    create_table :post_images do |t|

      t.timestamps null: false
    end

    add_column :post_images, :alt, :string
    add_column :post_images, :hint, :string
    add_column :post_images, :post_id, :integer
    add_foreign_key :post_images, :posts

    add_attachment :post_images, :file
  end
end
