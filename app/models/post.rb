class Post < ActiveRecord::Base

  scope :published, -> { where(published: true) }

  has_attached_file :photo, styles: {
      normal: '700x315>',
  }, default_url: '/images/avatar.png',
                    convert_options: {
                        normal: '-gravity center -extent 700x315',
                    }
  validates_attachment_content_type :photo, content_type: /\Aimage\/.*\Z/

  validates_presence_of :title, :content, :admin_user_id, :category_id

  belongs_to :admin_user
  belongs_to :category

  has_many :post_images

  has_and_belongs_to_many :tags

  accepts_nested_attributes_for :category, allow_destroy: true
  accepts_nested_attributes_for :tags, allow_destroy: true

  def published_at
    self.created_at.strftime('<span>%d</span> %B %Y')
  end

end
