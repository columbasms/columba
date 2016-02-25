class Api::V1::DigitsClientSerializer < ActiveModel::Serializer
  attributes :id, :user_name, :avatar_normal, :cover_normal,
             :organizations_count, :forwarded_campaigns_count, :auth_token
end
