# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
sangue = Topic.create(
    {
        :name => 'sangue'
    }
)

Organization.create!(
    {
        :email => 'lorenzo.rapetti.94@gmail.com',
        :password => 'fioredicampo',
        :password_confirmation => 'fioredicampo',
        :organization_name => 'Lorenzo Rapetti SRL',
        :topics => [sangue]
    }
)

AdminUser.create!(email: 'admin@example.com', password: 'password', password_confirmation: 'password')