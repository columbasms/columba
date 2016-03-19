class Api::V1::CampaignAddressSerializer < ActiveModel::Serializer
  attributes :address, :lat, :lng
end