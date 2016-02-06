class CreateTowns < ActiveRecord::Migration
  def change
    create_table :towns do |t|
      t.string :name

      t.timestamps null: false
    end

    add_column :towns, :province_id, :integer
    add_foreign_key :towns, :provinces
  end
end
