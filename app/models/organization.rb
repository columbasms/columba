class Organization < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable, :lockable

  scope :locked, -> { where(:locked_at => nil) }

  has_attached_file :avatar, styles: {
      normal: '250x250#',
      thumb: '32x32#'
  }, default_url: '/images/avatar.png'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/
  crop_attached_file :avatar

  has_attached_file :cover, styles: {
      normal: '1500x500#'
  }, default_url: '/images/cover.png'
  validates_attachment_content_type :cover, content_type: /\Aimage\/.*\Z/
  crop_attached_file :cover, aspect: '3:1'

  belongs_to :town

  has_many :campaigns

  has_and_belongs_to_many :topics

  accepts_nested_attributes_for :campaigns, allow_destroy: true
  accepts_nested_attributes_for :topics

  validates :organization_name, presence: true
  validates :email, presence: true
  validates_presence_of :fiscal_code, :town_id, :address, :postal_code, :phone_number

  with_options if: :visible? do |o|
    o.validates :topics, presence: true
    o.validates :description, presence: true
    o.validates :website, presence: true
  end

  before_create do
    self.locked_at = Time.now
  end

  def locked
    not locked_at.nil?
  end

  def locked=(locked)
    self.locked_at = locked ? Time.now : nil
  end

  def avatar_normal
    URI.join(ActionController::Base.asset_host, self.avatar.url(:normal)).to_s
  end

  def cover_normal
    URI.join(ActionController::Base.asset_host, self.cover.url(:normal)).to_s
  end

  private

  def visible?
    self.visible
  end

end
