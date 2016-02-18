class Api::V1::TopicsController < ApplicationController
  http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
  before_filter :set_topic, only: [:show, :campaigns, :organizations]
  before_filter :set_locale, exclude: [:organizations, :campaigns]
  force_ssl unless Rails.env.development?

  # GET /api/v1/topics
  def index
    @topics = Topic.active

    if params[:order_field].present? and params[:order_type].present?
      @topics = @topics.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @topics = @topics.limit(params[:limit]) if params[:limit].present?

    @topics = @topics.offset(params[:offset]) if params[:offset].present?

    render json: @topics, root: false
  end

  # GET /topics/{id}
  def show
    render json: @topic, root: false
  end

  # GET /topics/{id}/campaigns
  def campaigns
    render json: @topic.campaigns, root: false
  end

  # GET /topics/{id}/organizations
  def organizations
    render json: @topic.organizations, root: false
  end

  private

  def set_topic
    begin
      @topic = Topic.find params[:id]
    rescue ActiveRecord::RecordNotFound
      render json: { errors: 'Topic not found' }
    end
  end

  def set_locale
    I18n.locale = params[:locale] if params[:locale].present? and %w(en it).include?(params[:locale])
  end

end
