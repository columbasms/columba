class Api::V1::OrganizationSerializer < ActiveModel::Serializer
  attributes :id, :organization_name, :avatar_normal, :cover_normal, :description, :followers, :address, :website, :phone_number

  def followers
    object.digits_clients.count
  end
end
