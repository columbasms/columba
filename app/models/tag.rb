class Tag < ActiveRecord::Base

  has_and_belongs_to_many :posts

  validates_presence_of :title

  def post_count
    self.posts.count
  end

end
