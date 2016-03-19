class Category < ActiveRecord::Base

  has_many :posts

  validates :title, presence: true, uniqueness: true

  def post_count
    self.posts.count
  end

end
