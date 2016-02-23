class Api::V1::CampaignSerializer < ActiveModel::Serializer
  attributes :id, :message, :expires_at, :created_at, :shared_at, :topic
  has_one :organization, only: [:id, :organization_name, :avatar_normal]

  def filter(keys)
    if serialization_options[:include_shared_at]
      keys
    else
      keys - [:shared_at]
    end
  end

  def topic
    Api::V1::TopicSerializer.new object.organization.topic, { root: false }
  end
end
