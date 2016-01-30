class CreateReceivers < ActiveRecord::Migration
  def change
    create_table :receivers do |t|
      t.string :number

      t.timestamps null: false
    end
  end
end
