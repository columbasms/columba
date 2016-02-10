class Api::V1::TopicSerializer < ActiveModel::Serializer
  attributes :id, :name, :main_color, :status_color
end
