class Api::V1::CampaignsController < ApplicationController
  http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
  before_filter :set_campaign, only: [:show]
  force_ssl if !Rails.env.development?

  respond_to :json

  # GET /campaigns
  def index
    @campaigns = Campaign.all

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

end
