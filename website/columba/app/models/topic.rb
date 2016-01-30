class Topic < ActiveRecord::Base

  has_and_belongs_to_many :organizations
  has_and_belongs_to_many :digits_clients

  validates :name, presence: true

end
