class WelcomeController < ApplicationController
  before_action :authenticate_organization!, only: [:dashboard]

  # GET /
  def index
    render 'welcome/index', layout: 'application_frontend'
  end

  # GET /
  def index_organizations
    render 'welcome/index_organizations', layout: 'application_frontend'
  end

  def dashboard
    @campaigns = current_organization.campaigns.not_expired.order(:created_at => :desc).limit 5
    @analytics = {}
    @analytics[:topic_followers] = TopicAnalytic.where(:topic_id => current_organization.topics.collect { |t| t.id })
                                       .where('topic_analytics.created_at >= ?', Date.today)
                                       .sum(:followers)
    oa_last = current_organization.organization_analytics.where('organization_analytics.created_at >= ?', Date.today).last
    if oa_last.present?
      @analytics[:followers] = oa_last.follower
      @analytics[:truster] = oa_last.truster
    else
      @analytics[:followers] = 0
      @analytics[:truster] = 0
    end
    campaigns_count = current_organization.campaigns.count
    if campaigns_count > 0
      @analytics[:campaigns] = campaigns_count
      @analytics[:active_campaigns] = current_organization.campaigns.where('campaigns.expires_at >= ?', Date.today).count
      @analytics[:sent_sms] = current_organization.campaigns.joins(:campaign_analytics).where('campaign_analytics.created_at >= ?', Date.today).sum(:sent_sms)
    else
      @analytics[:campaigns] = 0
      @analytics[:active_campaigns] = 0
      @analytics[:sent_sms] = 0
    end

    digits = current_organization.digits_clients
    if digits.present?
      @analytics[:sms_avg] = digits.average(:max_sms)
    else
      @analytics[:sms_avg] = 0
    end

    render 'welcome/dashboard', layout: 'application_dashboard'
  end

  def account_locked
    render 'welcome/account_locked', layout: 'application_login_no_content'
  end

  # POST /contact
  def contact
    p = params[:contact].permit(:name, :email, :message)
    if p[:name].present? and p[:email].present? and p[:message].present?
      ContactMailer.contact_email(p).deliver_now
      render json: { success: true }
    else
      render json: { error: true }
    end
  end

  def follow_trending
    topic_followers = TopicAnalytic.where(:topic_id => current_organization.topics.pluck(:id))
                          .where('topic_analytics.created_at >= ?', Date.today - 29.days).map { |x| { created_at: x.created_at.to_date, followers: x.followers } }
    organization_followers = current_organization.organization_analytics
                                 .where('organization_analytics.created_at >= ?', Date.today - 29.days)
                                 .map { |x| { created_at: x.created_at.to_date, followers: x.follower, trusters: x.truster } }
    topic = []
    followers = []
    trusters = []
    ((Date.today - 29.days)..Date.today).each do |date|
      new_date = date.to_time.to_i * 1000

      topic_follower_n = topic_followers.select { |t| t[:created_at] == date }.map { |x| x[:followers] }.sum
      topic.push({ x: new_date, y: topic_follower_n })

      organization_followers_select = organization_followers.select { |t| t[:created_at] == date }

      organization_follower_n = organization_followers_select.map { |x| x[:followers] }.sum
      followers.push({ x: new_date, y: organization_follower_n })

      organization_truster_n = organization_followers_select.map { |x| x[:trusters] }.sum
      trusters.push({ x: new_date, y: organization_truster_n })
    end

    render json: [
        {
            values: topic,
            key: I18n.t('welcome.dashboard.topic_followers'),
            color: "#ff7f0e"
        },
        {
            values: followers,
            key: I18n.t('welcome.dashboard.followers'),
            color: "#2ca02c"
        },
        {
            values: trusters,
            key: I18n.t('welcome.dashboard.truster'),
            color: "#7777ff"
        }
    ], root: false
  end

end
