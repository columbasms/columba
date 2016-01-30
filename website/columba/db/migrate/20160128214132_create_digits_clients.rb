class CreateDigitsClients < ActiveRecord::Migration
  def change
    create_table :digits_clients do |t|
      t.string :phone_number
      t.boolean :enabled
      t.text :gcm_token
      t.text :digits_token

      t.timestamps null: false
    end
  end
end
