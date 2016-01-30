class Group < ActiveRecord::Base

  belongs_to :digits_client

  has_and_belongs_to_many :receivers

  validates_associated :digits_client
  validates_associated :receivers
end
