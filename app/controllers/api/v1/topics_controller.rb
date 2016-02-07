class Api::V1::TopicsController < ApplicationController
  http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
  before_filter :set_topic, only: [:show, :campaigns, :organizations]
  force_ssl

  # GET /api/v1/topics
  def index
    @topics = Topic.all

    if params[:order_field].present? and params[:order_type].present?
      @topics = @topics.order("#{params[:order_field]}": :"#{params[:order_type]}")
    end

    @topics = @topics.limit(params[:limit]) if params[:limit].present?

    @topics = @topics.offset(params[:offset]) if params[:offset].present?

    render json: @topics, root: false
  end

  def show
    render json: @topic, root: false
  end

  def campaigns
    render json: @topic.campaigns, root: false
  end

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

end
