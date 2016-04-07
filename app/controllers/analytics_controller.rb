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

        # number of DISTINCT people reached by this organization though all its campaigns
        people_reached: CampaignClientReceiver.where(campaign_id: campaign_ids).select(:receiver_id).distinct.count,

        # total number of sent SMS
        sms_sent: CampaignClientReceiver.where(campaign_id: campaign_ids).count
    }
  end

  # temporal graph of general sms trend
  def campaigns_analytics_async
    month_ago = Date.today - 29.days
    campaign_ids = current_organization.campaigns.pluck(:id)

    ca = CampaignAnalytic.where(campaign_id: campaign_ids).where('created_at >= ?', month_ago)

    campaigns_data = ca.map { |x| {created_at: x.created_at, sent_sms: x.sent_sms}}

    people_reached_data = []
    daily_sent_sms = []
    ((Date.today - 29.days)..Date.today).each do |date|
      new_date = date.to_time.to_i * 1000

      campaign_data_select = campaigns_data.select { |t| t[:created_at].to_date == date }

      sms_sent_sum = campaign_data_select.map { |x| x[:sent_sms] }.sum

      if daily_sent_sms.empty?
        daily_sent_sms.push({x: new_date, y: 0})
      else
        daily_sent_sms.push({x: new_date, y: (sms_sent_sum-people_reached_data.last[:y])})
        end

      people_reached_data.push({ x: new_date, y: sms_sent_sum })
    end

    render json: [
        {
            values: people_reached_data,
            key: I18n.t('analytics.campaigns.sms_sent'),
            color: "#2ca02c"
        },
        {
            values: daily_sent_sms,
            key: I18n.t('analytics.campaigns.daily_sms_sent'),
            color: "#00ff99"
        }
    ], root: false

  end

  # temporal graph of sms trend for a specific campaign
  def campaign_analytics_async
    month_ago = Date.today - 29.days

    ca = CampaignAnalytic.where(campaign_id: params[:id]).where('created_at >= ?', month_ago)

    active_users_data = []
    people_reached_data = []
    daily_supporters = []
    daily_sent_sms = []
    (month_ago..Date.today).each do |date|
      new_date = date.to_time.to_i * 1000

      campaign_data_selected = ca.select { |t| t[:created_at].to_date == date }

      supporters_sum = campaign_data_selected.map { |x| x[:supporters] }.sum

      if daily_supporters.empty?
        daily_supporters.push({x: new_date, y:0})
      else
        daily_supporters.push({x: new_date, y:supporters_sum - active_users_data.last[:y]})
      end

      active_users_data.push({ x: new_date, y: supporters_sum })

      sms_sent_sum = campaign_data_selected.map { |x| x[:sent_sms] }.sum

      if daily_sent_sms.empty?
        daily_sent_sms.push({x: new_date, y: 0})
      else
        daily_sent_sms.push({x: new_date, y: (sms_sent_sum-people_reached_data.last[:y])})
      end

      people_reached_data.push({ x: new_date, y: sms_sent_sum })
    end

    render json: [
        {
            values: active_users_data,
            key: I18n.t('analytics.campaigns.relaunched'),
            color: "#ff7f0e"
        },
        {
            values: daily_supporters,
            key: I18n.t('analytics.campaigns.daily_relauncheds'),
            color: "#ffff00"
        },
        {
            values: people_reached_data,
            key: I18n.t('analytics.campaigns.sms_sent'),
            color: "#2ca02c"
        },
        {
          values: daily_sent_sms,
          key: I18n.t('analytics.campaigns.daily_sms_sent'),
          color: "#00ff99"
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
