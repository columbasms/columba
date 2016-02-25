class Api::V1::CampaignsController < ApplicationController
  #http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
  before_filter :restrict_access
  before_filter :set_campaign, only: [:show]
  before_filter :set_user, only: [:index]
  force_ssl unless Rails.env.development?

  respond_to :json

  # GET /campaigns
  def index
    # if user param is not used or the user doesn't follow any topic/organization
    if @user.nil? or (@user.topics.empty? and @user.organizations.empty?)
      @campaigns = Campaign.not_expired
    else
      # campaigns under user followed topic
      @topics_campaigns = Campaign.not_expired.includes(:topics).where(topics: { id: @user.topics.select(:topic_id) })
      # campaigns of organizations followed by user
      @organizations_campaigns = Campaign.not_expired.where(organization_id: @user.organizations)

      @campaigns= (@topics_campaigns + @organizations_campaigns).uniq
      if @campaigns.empty?
        # if no active campaign match the user's preferences all the campaigns are returned
        # ALESSIO: maybe in this case we should notify the user about it
        @campaigns = Campaign.not_expired
      end
    end
    if params[:order_field].present? and params[:order_type].present?
      @campaigns = @campaigns.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @campaigns = @campaigns.limit(params[:limit]) if params[:limit].present?

    @campaigns = @campaigns.offset(params[:offset]) if params[:offset].present?

    render json: @campaigns, root: nil
  end

  # GET /campaigns/:id
  def show
    render json: @campaign, root: false
  end

  private

  def set_campaign
    begin
      @campaign = Campaign.find params[:id]
    rescue ActiveRecord::RecordNotFound
      render json: { errors: 'Campaign not found' }
    end
  end

  def set_user
    if params[:user_id].nil?
      @user = nil
    else
      begin
        @user = DigitsClient.find params[:user_id]
      rescue ActiveRecord::RecordNotFound
        render json: { errors: 'User not found' }
      end
    end
  end

  def restrict_access
    head :unauthorized unless DigitsClient.find_by_auth_token(params[:auth_token])
  end

end
