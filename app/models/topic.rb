class Topic < ActiveRecord::Base

  scope :active, -> { joins(:organizations).uniq }

  has_and_belongs_to_many :campaigns
  has_and_belongs_to_many :organizations
  has_and_belongs_to_many :digits_clients

  validates :name, presence: true

end
