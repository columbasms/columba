class Topic < ActiveRecord::Base

  translates :name, :description

  scope :active, -> { joins(:organizations).uniq }

  has_and_belongs_to_many :campaigns
  has_and_belongs_to_many :organizations
  has_and_belongs_to_many :digits_clients

  has_attached_file :image, styles: {
      normal: '250x250#',
      thumb: '32x32#'
  }, default_url: '/images/avatar.png'
  validates_attachment_content_type :image, content_type: /\Aimage\/.*\Z/

  validates :name, presence: true

end
