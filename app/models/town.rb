class Town < ActiveRecord::Base
  belongs_to :province

  has_many :organizations
  has_many :campaigns
end
