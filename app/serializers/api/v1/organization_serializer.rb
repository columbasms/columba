class Api::V1::OrganizationSerializer < ActiveModel::Serializer
  attributes :id, :organization_name, :avatar_normal, :cover_normal, :description
end
