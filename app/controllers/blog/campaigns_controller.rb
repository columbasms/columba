class Blog::CampaignsController < ApplicationController
  force_ssl unless Rails.env.development?
  protect_from_forgery except: [:tinymce_assets_create]
  before_filter :set_campaign, only: [:show]
  layout 'application_frontend'

  def index
    @campaigns = Campaign.not_test

    if params[:running].present?
      if params[:running]=="true"
        @campaigns=@campaigns.where('expires_at >= ?', Date.today)
      end
    end

    if params[:organization].present?
      @campaigns = @campaigns.where(organization_id: params[:organization])
      @organization = Organization.find(params[:organization])
    end

    if params[:topic].present?
      @campaigns = @campaigns.includes(:topics).where(topics: { id: params[:topic] })
      @topic = Topic.find params[:topic]
    end

    @campaigns = @campaigns.paginate(page: params[:page], per_page: 5).order('campaigns.created_at DESC')
  end

  def show
  end

  private

  def set_campaign
    @campaign = Campaign.find params[:id]
  end

end
