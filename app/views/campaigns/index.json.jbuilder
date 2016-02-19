json.array!(@campaigns) do |campaign|
  json.extract! campaign, :id
  json.url campaign_url(campaign, format: :json)
end
