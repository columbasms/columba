class Api::V1::CampaignSerializer < ActiveModel::Serializer
  attributes :id, :message, :expires_at, :created_at, :shared_at
  has_one :organization, only: [:id, :organization_name, :avatar_normal]
  has_many :topics

  def filter(keys)
    if serialization_options[:include_shared_at]
      keys
    else
      keys - [:shared_at]
    end
  end
end
