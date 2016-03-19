class AdminUser < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :confirmable, :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, 
         :recoverable, :rememberable, :trackable, :validatable

  has_attached_file :avatar, styles: {
      normal: '250x250#',
      thumb: '32x32#',
      thumb_48: '48x48#'
  }, default_url: '/images/avatar.png'
  validates_attachment_content_type :avatar, content_type: /\Aimage\/.*\Z/

  has_many :posts

  def to_s
    self.name
  end

end
