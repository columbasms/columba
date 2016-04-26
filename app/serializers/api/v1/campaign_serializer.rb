class Api::V1::CampaignSerializer < ActiveModel::Serializer
  attributes :id, :message, :photo_mobile, :photo_original, :photo_mobile_max, :long_description, :expires_at, :created_at, :shared_at
  has_one :organization, only: [:id, :organization_name, :avatar_normal]
  has_many :topics
  has_many :campaign_addresses

  def filter(keys)
    if serialization_options[:include_shared_at]
      keys
    else
      keys - [:shared_at]
    end
  end

end
