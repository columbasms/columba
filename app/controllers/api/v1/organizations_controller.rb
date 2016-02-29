class Api::V1::OrganizationsController < ApplicationController
  #http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
  before_filter :restrict_access
  before_filter :set_organization, only: [:show, :campaigns]
  force_ssl unless Rails.env.development?

  # GET /organizations
  def index
    @organizations = Organization.all

    if params[:order_field].present? and params[:order_type].present?
      @organizations = @organizations.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @organizations = @organizations.limit(params[:limit]) if params[:limit].present?

    @organizations = @organizations.offset(params[:offset]) if params[:offset].present?

    render json: @organizations, root: nil
  end

  # GET /organizations/:id
  def show
    render json: @organization, root: false
  end

  # GET /organizations/:id/campaigns
  def campaigns
    render json: @organization.campaigns, root: false
  end

  private

  def set_organization
    begin
      @organization = Organization.find params[:id]
    rescue ActiveRecord::RecordNotFound
      render json: { errors: 'Organization not found' }
    end
  end

  def restrict_access
    head :unauthorized unless DigitsClient.find_by_auth_token(request.headers['X-Auth-Token'])
  end

end
