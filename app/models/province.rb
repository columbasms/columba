class Province < ActiveRecord::Base

  has_many :towns
  has_many :campaigns

  belongs_to :region

end
