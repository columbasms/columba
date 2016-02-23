class Region < ActiveRecord::Base

  has_many :provinces
  has_many :campaigns

  validates_presence_of :name

end
