class Api::V1::TopicSerializer < ActiveModel::Serializer
  attributes :id, :name, :image_mobile, :main_color, :status_color, :image_mobile
end
