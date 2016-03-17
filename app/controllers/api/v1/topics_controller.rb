class Api::V1::TopicsController < ApplicationController
  # http_basic_authenticate_with name: Rails.application.secrets[:http_basic][:name], password: Rails.application.secrets[:http_basic][:password]
  before_filter :restrict_access unless Rails.env.development?
  before_filter :set_topic, only: [:show, :campaigns, :organizations, :update]
  before_filter :set_locale, exclude: [:organizations, :campaigns]
  skip_before_filter :verify_authenticity_token
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

  # PUT /topics/{id}
  def update
    if @topic.update(topic_params)
      render json: @topic, root: false
    else
      render json: @topic.errors, root: false
    end
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

    def topic_params
      params[:topic].permit(:name, :description, :locale)
    end

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

  def restrict_access
    head :unauthorized unless DigitsClient.find_by_auth_token(request.headers['X-Auth-Token'])
  end

end
