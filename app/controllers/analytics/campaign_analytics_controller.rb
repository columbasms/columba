class Analytics::CampaignAnalyticsController < ApplicationController
  force_ssl unless Rails.env.development?

  # recalculate the number of users that forward the campaign
  def update_supporters_and_sms_sent(input_campaign)
    current_campaign=Campaign.find_by(id: input_campaign.id)
    if current_campaign.nil?
      return
    end
    current_analytics=CampaignAnalytic.where("created_at > ? AND created_at < ?", Date.today, Date.tomorrow).find_by(campaign_id: input_campaign.id)
    if current_analytics.nil?
      return
    end
    campaign_clients=current_campaign.campaign_client_receivers
    sms_count=campaign_clients.count
    supporters_count=campaign_clients.group(:digits_client_id).distinct.count.size
    current_analytics.supporters=supporters_count
    current_analytics.sent_sms=sms_count
    current_analytics.save

    analytic_control=Analytics::OrganizationAnalyticsController.new
    analytic_control.update_global_sent_sms(current_campaign.organization_id)
    analytic_control.update_global_supporters(current_campaign.organization_id)

  end
end
