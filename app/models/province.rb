class Province < ActiveRecord::Base

  has_many :towns
  has_many :campaigns

  belongs_to :region

  validates_presence_of :name, :code

  def to_s
    "#{self.name}"
  end

end
