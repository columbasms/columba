class AddFieldsToDigitsClient < ActiveRecord::Migration
  def change
    add_column :digits_clients, :avatar_file_name, :string
    add_column :digits_clients, :avatar_content_type, :string
    add_column :digits_clients, :avatar_file_size, :integer
    add_column :digits_clients, :avatar_updated_at, :datetime

    add_column :digits_clients, :cover_file_name, :string
    add_column :digits_clients, :cover_content_type, :string
    add_column :digits_clients, :cover_file_size, :integer
    add_column :digits_clients, :cover_updated_at, :datetime
  end
end
