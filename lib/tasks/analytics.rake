namespace :analytics do
  desc "Updating daily reports for analytics and creating new one for the next day"
  task compute: :environment do
    analytic_controller=Analytics::OrganizationAnalyticsController.new
    Organization.find_each do |org|
      analytic_controller.update_followers_trusters_range(org)
      analytic_controller.update_sms_general_range(org)
      analytic_controller.update_global_supporters(org)
      analytic_controller.update_global_sent_sms(org)
    end
    analytic_controller=Analytics::TopicAnalyticsController.new
    Topic.find_each do |top|
      analytic_controller.update_followers_and_sms_range(top)
    end
    analytic_controller=Analytics::CampaignAnalyticsController.new
    Campaign.find_each do |sup|
      analytic_controller.update_supporters_and_sms_sent(sup)
    end

  end

  desc "create the starting rows for every analytic entity setting them to 0"
  task initialize: :environment do
    # temporaneo, solo per creare i record iniziali
    Organization.find_each do |org|
      current_analytic=OrganizationAnalytic.find_or_create_by(organization_id: org.id)
      if current_analytic.new_record?
        current_analytic.save
      end
    end

    Topic.find_each do |top|
      current_analytic=TopicAnalytic.find_or_create_by(topic_id: top.id)
      if current_analytic.created_at>=Date.today
        next
      end
      if current_analytic.new_record?
        current_analytic.save
      end
    end

    Campaign.find_each do |camp|
      current_analytic=CampaignAnalytic.find_or_create_by(campaign_id: camp.id)
      if current_analytic.new_record?
        current_analytic.save
      end
    end

    DigitsClient.find_each do |a|
      if a.max_sms.nil?
        a.max_sms=50
        a.save
      end
    end

  end

end
