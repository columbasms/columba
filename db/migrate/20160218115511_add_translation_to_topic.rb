class AddTranslationToTopic < ActiveRecord::Migration
  def up
    Topic.create_translation_table!(
        { name: :string, description: :string },
        { migrate_data: true })
  end

  def down
    Topic.drop_translation_table! migrate_data: true
  end
end
