class Analytics::TopicAnalyticsController < ApplicationController
  force_ssl unless Rails.env.development?

  # recalculate the number of followers
  def update_followers_and_sms_range(input_topic)
    current_topic=Topic.find_by(id: input_topic.id)
    if current_topic.nil?
      return
    end
    current_analytics=TopicAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(topic_id: input_topic.id)
    if current_analytics.nil?
      return
    end
    follower_count=current_topic.digits_clients.count
    # new_sms_range=0
    # current_topic.digits_clients.each { |cli| new_sms_range+=cli.max_sms }
    new_sms_range=current_topic.digits_clients.sum(:max_sms)
    current_analytics.followers=follower_count
    current_analytics.sms_range=new_sms_range
    current_analytics.save
  end

end
