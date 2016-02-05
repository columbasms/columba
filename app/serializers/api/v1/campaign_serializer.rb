class Api::V1::CampaignSerializer < ActiveModel::Serializer
  attributes :id, :message
  has_one :organization, only: [:id, :organization_name, :avatar_normal]
  has_many :topics
end
