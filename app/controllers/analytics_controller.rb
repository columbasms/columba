class AnalyticsController < ApplicationController
  layout 'application_dashboard'
  before_filter :authenticate_organization!

  def campaigns_analytics
    campaign_ids = current_organization.campaigns.pluck(:id)
    @data = {
        campaigns: current_organization.campaigns.count,
        active_campaigns: current_organization.campaigns.not_expired.count,
        active_users: DigitsClient.joins(:campaign_client_receivers).where(
            campaign_client_receivers: { campaign_id: campaign_ids }).count,
        people_reached: CampaignClientReceiver.where(campaign_id: campaign_ids).count
    }
  end

  def campaigns_analytics_async
    month_ago = Date.today - 29.days
    campaign_ids = current_organization.campaigns.pluck(:id)

    active_users = CampaignAnalytic.where(campaign_id: campaign_ids).where('created_at >= ?', month_ago)

    active_users_data = []
    people_reached_data = []
    (month_ago..Date.today).each do |date|
      new_date = date.to_time.to_i * 1000

      a = active_users.select { |t| t[:created_at] == date }

      supporters_sum = a.map { |x| x[:supporters] }.sum
      active_users_data.push({ x: new_date, y: supporters_sum })

      sms_sent_sum = a.map { |x| x[:sent_sms] }.sum
      people_reached_data.push({ x: new_date, y: sms_sent_sum })
    end

    render json: [
        {
            values: active_users_data,
            key: I18n.t('analytics.campaigns.sent_sms'),
            color: "#ff7f0e"
        },
        {
            values: people_reached_data,
            key: I18n.t('analytics.campaigns.sms_received'),
            color: "#2ca02c"
        }
    ], root: false

  end

  def organization_analytics
    last_analytics = current_organization.organization_analytics.last
    @data = {}
    if last_analytics.present?
      @data[:total_followers] = last_analytics.follower
      @data[:total_trusters] = last_analytics.truster
      @data[:sms_range_followers] = last_analytics.sms_range_follower
      @data[:sms_range_trusters] = last_analytics.sms_range_truster
    end
  end

end
