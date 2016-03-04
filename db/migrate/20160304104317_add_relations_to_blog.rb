class AddRelationsToBlog < ActiveRecord::Migration
  def change
    add_column :posts, :admin_user_id, :integer
    add_foreign_key :posts, :admin_users

    add_column :posts, :category_id, :integer
    add_foreign_key :posts, :categories

    create_join_table :posts, :tags do |t|
      t.index :post_id
      t.index :tag_id
    end
  end
end
