class CreateProvinces < ActiveRecord::Migration
  def change
    create_table :provinces do |t|
      t.string :name
      t.string :code

      t.timestamps null: false
    end

    add_column :provinces, :region_id, :integer
    add_foreign_key :provinces, :regions
  end
end
