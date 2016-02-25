class DigitsClientSerializer < ActiveModel::Serializer
  attributes :id, :digits_id, :digits_token, :phone_number, :auth_token
end
