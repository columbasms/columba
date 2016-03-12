class TopicAnalytic < ActiveRecord::Base

  belongs_to :topic

  validates :created_at, uniqueness: { scope: :topic_id }

end
