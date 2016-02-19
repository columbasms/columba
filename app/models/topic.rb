class Topic < ActiveRecord::Base

  translates :name, :description

  scope :active, -> { joins(:organizations).uniq }

  has_and_belongs_to_many :campaigns
  has_and_belongs_to_many :organizations
  has_and_belongs_to_many :digits_clients

  has_attached_file :image, styles: {
      normal: '1280x720#',
      mobile: '800x450#',
      thumb: '32x32#'
  }, default_url: '/images/avatar.png'
  validates_attachment_content_type :image, content_type: /\Aimage\/.*\Z/

  validates :name, presence: true

  def image_mobile
    URI.join(ActionController::Base.asset_host, self.image.url(:mobile)).to_s
  end

end
