class Api::V1::LeaderboardController < ApplicationController
  before_filter :restrict_access
  force_ssl unless Rails.env.development?

  # GET /leaderboard
  def index
    sql = 'select count(ccr.id) as points, dc.user_name
from `digits_clients` dc
inner join `campaign_client_receivers` ccr on ccr.`digits_client_id` = dc.id
group by dc.user_name
order by points desc'
    records = ActiveRecord::Base.connection.execute(sql)
    render json: records.map { |v| { user_name: v[1], points: v[0] } }, root: false
  end

  private

  def restrict_access
    #head :unauthorized unless DigitsClient.find_by_auth_token(request.headers['X-Auth-Token'])
  end

end
