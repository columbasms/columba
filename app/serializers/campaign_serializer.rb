class CampaignSerializer < ActiveModel::Serializer
  attributes :message, :date, :messages_sent

  def date
    object.created_at.strftime("%B %-d, %Y")
  end

  def messages_sent
    0
  end
end
