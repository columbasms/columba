class Organization < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable, :lockable

  scope :locked, -> { where(:locked_at => nil) }

  has_attached_file :avatar, styles: {
      thumb: '32x32#'
  }, default_url: '/assets/avatar.png'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/

  has_many :campaigns

  has_and_belongs_to_many :topics

  accepts_nested_attributes_for :campaigns, allow_destroy: true
  accepts_nested_attributes_for :topics

  validates_associated :topics
  validates :organization_name, presence: true
  validates :email, presence: true

  before_create do
    self.locked_at = Time.now
  end

  def locked
    not locked_at.nil?
  end

  def locked=(locked)
    self.locked_at = locked ? Time.now : nil
  end

end
