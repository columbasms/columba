class Organization < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable,
         :recoverable, :rememberable, :trackable, :validatable, :lockable

  has_attached_file :avatar, styles: {
      thumb: '32x32#'
  }, default_url: '/assets/avatar.png'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/

  has_many :campaigns

  has_and_belongs_to_many :topics

  validates_associated :topics
  validates :organization_name, presence: true
  validates :email, presence: true
end
