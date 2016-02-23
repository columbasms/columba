class ChangeOrganizationTopicRelation < ActiveRecord::Migration
  def change
    drop_join_table :organizations, :topics
    add_column :organizations, :topic_id, :integer
    add_foreign_key :organizations, :topics
  end
end
