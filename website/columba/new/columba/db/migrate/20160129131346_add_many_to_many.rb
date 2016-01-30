class AddManyToMany < ActiveRecord::Migration
  def change
    create_join_table :campaigns, :digits_clients do |t|
      t.index :campaign_id
      t.index :digits_client_id
    end

    create_join_table :digits_clients, :topics do |t|
      t.index :digits_client_id
      t.index :topic_id
    end

    create_join_table :groups, :receivers do |t|
      t.index :group_id
      t.index :receiver_id
    end

    create_join_table :organizations, :topics do |t|
      t.index :organization_id
      t.index :topic_id
    end
  end
end
