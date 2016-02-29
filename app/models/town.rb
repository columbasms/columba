class Town < ActiveRecord::Base
  belongs_to :province

  has_many :organizations
  has_many :campaigns

  validates_presence_of :name

  def to_s
    "#{self.name}"
  end

end
