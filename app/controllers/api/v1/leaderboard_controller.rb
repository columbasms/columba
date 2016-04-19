class Api::V1::LeaderboardController < ApplicationController
  before_filter :restrict_access unless Rails.env.development?
  force_ssl unless Rails.env.development?

  # GET /leaderboard
  def index
    records = DigitsClient
        .joins(:campaign_client_receivers)
        .select('digits_clients.*, count(campaign_client_receivers.id) as points')
        .where('digits_clients.is_private = ?', false)
        .group(:user_name)
        .order('points desc')
    render json: records.map { |v| { user_name: v.user_name, avatar_normal: v.avatar_normal, points: v.points } }, root: false, serializer: nil
  end

  private

  def restrict_access
    head :unauthorized unless DigitsClient.find_by_auth_token(request.headers['X-Auth-Token'])
  end

end
