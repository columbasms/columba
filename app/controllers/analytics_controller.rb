class AnalyticsController < ApplicationController
  layout 'application_dashboard'
  before_filter :authenticate_organization!

  # GET /dashboard/analytics/campaigns
  def campaigns_analytics
    campaign_ids = current_organization.campaigns.pluck(:id)
    @data = {
        # number of campaigns sent by this organization
        campaigns: current_organization.campaigns.count,

        # number of campaigns actually running of this organization
        active_campaigns: current_organization.campaigns.not_expired.count,

        # number of DISTINCT users that had spread the campaigns of this organization
        active_users: CampaignClientReceiver.where(campaign_id: campaign_ids).select(:digits_client_id).distinct.count,

        # number of DISTINCT people reaced by this organization though all its campaigns
        people_reached: CampaignClientReceiver.where(campaign_id: campaign_ids).select(:receiver_id).distinct.count,

        # total number of sent SMS
        sms_sent: CampaignClientReceiver.where(campaign_id: campaign_ids).count
    }
  end

  # temporal graph of sms trend
  def campaigns_analytics_async
    month_ago = Date.today - 29.days
    campaign_ids = current_organization.campaigns.pluck(:id)

    ca = CampaignAnalytic.where(campaign_id: campaign_ids).where('created_at >= ?', month_ago)

    campaigns_data = ca.map { |x| {created_at: x.created_at, supporters: x.supporters ,sent_sms: x.sent_sms}}

    active_users_data = []
    people_reached_data = []
    ((Date.today - 29.days)..Date.today).each do |date|
      new_date = date.to_time.to_i * 1000

      campaign_data_select = campaigns_data.select { |t| t[:created_at].to_date == date }

      supporters_sum = campaign_data_select.map { |x| x[:supporters] }.sum
      active_users_data.push({ x: new_date, y: supporters_sum })

      sms_sent_sum = campaign_data_select.map { |x| x[:sent_sms] }.sum
      people_reached_data.push({ x: new_date, y: sms_sent_sum })
    end

    render json: [
        {
            values: active_users_data,
            key: I18n.t('analytics.campaigns.relauncheds'),
            color: "#ff7f0e"
        },
        {
            values: people_reached_data,
            key: I18n.t('analytics.campaigns.sms_received'),
            color: "#2ca02c"
        }
    ], root: false

  end

  def campaign_analytics_async
    month_ago = Date.today - 29.days

    active_users = CampaignAnalytic.where(campaign_id: params[:id]).where('created_at >= ?', month_ago)

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
            key: I18n.t('analytics.campaigns.relaunched'),
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
    else
      @data[:total_followers] = 0
      @data[:total_trusters] = 0
      @data[:sms_range_followers] = 0
      @data[:sms_range_trusters] = 0
    end
  end

end
