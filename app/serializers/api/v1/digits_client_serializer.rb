class Api::V1::DigitsClientSerializer < ActiveModel::Serializer
  attributes :id, :user_name, :avatar_normal, :cover_normal
end
