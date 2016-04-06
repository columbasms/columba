class Analytics::OrganizationAnalyticsController < ApplicationController
  # before_action :authenticate_organization!
  # before_filter :set_organization

  force_ssl unless Rails.env.development?

  # tutte le funzioni ricalcolano i valori leggendo il db, non gli si passano nuove cose

  # # updates all the statisty of the day
  # def update_all(input_organization)
  #
  # end

  # # instantiate the DB records for the current day
  # def create_daily_report(input_organization)
  #   # OrganizationAnalytic.find(66).dup.save
  #   #   duplica la righa del DB tranne id e date
  #   OrganizationAnalytic.find_by(id: input_organization.id).dup.save
  # end

  # recalculate the number of followers and truster and their respective SMS range
  def update_followers_trusters_range(input_organization)
    current_organization=Organization.find_by(id: input_organization.id)
    if current_organization.nil?
      return
    end
    current_analytics=OrganizationAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(organization_id: input_organization.id)
    if current_analytics.nil?
      return
    end
    organization_clients=current_organization.digits_clients_organizations.joins(:digits_client)
    follower_count=organization_clients.count
    range_follower=organization_clients.sum(:max_sms)
    truster_count=organization_clients.where(trusted: true).count
    range_truster=organization_clients.where(trusted: true).sum(:max_sms)
    current_analytics.follower=follower_count
    current_analytics.sms_range_follower=range_follower
    current_analytics.truster=truster_count
    current_analytics.sms_range_truster=range_truster
    current_analytics.save
  end

  # calculate the SMS range at topic followers level
  # "how many SMS would be sent if all the people that like my topics would spread my campaign with all their available SMS?"
  def update_sms_general_range(input_organization)
    current_organization=Organization.find_by(id: input_organization.id)
    if current_organization.nil?
      return
    end
    current_analytics=OrganizationAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(organization_id: input_organization.id)
    if current_analytics.nil?
      return
    end

    general_range=DigitsClient.joins(:digitsclients_topics).where('digits_clients_topics.topic_id': current_organization.topics).distinct.map{|a| a.max_sms}.sum

    current_analytics.sms_range_general=general_range
    current_analytics.save
  end

  # number of distinct users that had spread my campaigns in general
  def update_global_supporters(input_organization)
    current_organization=Organization.find_by(id: input_organization)
    if current_organization.nil?
      return
    end
    current_analytics=OrganizationAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(organization_id: input_organization)
    if current_analytics.nil?
      return
    end

    organization_campaigns=current_organization.campaigns
    global_distinct_supporters_count=CampaignClientReceiver.where(campaign_id: organization_campaigns).group(:digits_client_id).distinct.count.size
    current_analytics.global_supporter=global_distinct_supporters_count
    current_analytics.save

  end

  # total amount of sms sent for all my campaigns
  def update_global_sent_sms(input_organization)
    current_organization=Organization.find_by(id: input_organization)
    if current_organization.nil?
      return
    end
    current_analytics=OrganizationAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(organization_id: input_organization)
    if current_analytics.nil?
      return
    end
    global_sms_count=current_organization.campaigns.joins(:campaign_analytics).where('campaign_analytics.created_at >= ?', Date.today).sum(:sent_sms)
    current_analytics.global_sent_sms=global_sms_count
    current_analytics.save
  end


end
