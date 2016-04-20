class Blog::CampaignsController < ApplicationController
  force_ssl unless Rails.env.development?
  protect_from_forgery except: [:tinymce_assets_create]
  before_filter :set_campaign, only: [:show]
  layout 'application_frontend'

  def index
    @campaigns = Campaign.not_test
    @campaigns = @campaigns.paginate(page: params[:page], per_page: 5).order('campaigns.created_at DESC')
  end

  def show
  end

  private

  def set_campaign
    @campaign = Campaign.find params[:id]
  end

end
