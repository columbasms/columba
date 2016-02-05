class Api::V1::OrganizationsController < ApplicationController
  http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password

  # GET /api/v1/organizations
  def index
    @organizations = Organization.all

    if params[:order_field].present? and params[:order_type].present?
      @organizations = @organizations.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @organizations = @organizations.limit(params[:limit]) if params[:limit].present?

    @organizations = @organizations.offset(params[:offset]) if params[:offset].present?

    render json: @organizations, root: nil
  end

end
