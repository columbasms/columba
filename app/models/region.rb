class Region < ActiveRecord::Base

  has_many :provinces
  has_many :campaigns

end
