class Api::V1::CampaignsController < ApplicationController
  http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password

  # GET /api/v1/campaigns
  def index
    @campaigns = Campaign.all

    if params[:order_field].present? and params[:order_type].present?
      @campaigns = @campaigns.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @campaigns = @campaigns.limit(params[:limit]) if params[:limit].present?

    @campaigns = @campaigns.offset(params[:offset]) if params[:offset].present?

    render json: @campaigns, root: nil
  end

end
