namespace :analytics do
  desc "Updating daily reports for analytics and creating new one for the next day"
  task compute: :environment do
    # temporaneo per forzare manualmente l'aggiornamento dei record del giorno
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
    # per esecuzione manuale sul server
    # RAILS_ENV=production bundle exec rake analytics:initialize
    Organization.find_each do |org|
      current_analytic=OrganizationAnalytic.find_or_create_by(organization_id: org.id)
      if current_analytic.new_record?
        current_analytic.save
      end
    end

    Topic.find_each do |top|
      current_analytic=TopicAnalytic.find_or_create_by(topic_id: top.id)
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

  desc "create the records for today"
  task start_day: :environment do
    # temporaneo, per creare manualmente i record giornalieri

    Organization.find_each do |org|
      if OrganizationAnalytic.where(created_at: (Date.today)..(Date.tomorrow), organization_id: org.id).empty?
        current_analytic=OrganizationAnalytic.create(organization_id: org.id)
        current_analytic.save
      end
    end

    Topic.find_each do |top|
      if TopicAnalytic.where(created_at: (Date.today)..(Date.tomorrow), topic_id: top.id).empty?
        current_analytic=TopicAnalytic.create(topic_id: top.id)
        current_analytic.save
      end
    end

    Campaign.find_each do |camp|
      if CampaignAnalytic.where(created_at: (Date.today)..(Date.tomorrow), campaign_id: camp.id).empty?
        current_analytic=CampaignAnalytic.create(campaign_id: camp.id)
        current_analytic.save
      end
    end

  end

end
