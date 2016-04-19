class Api::V1::DigitsClientSerializer < ActiveModel::Serializer
  attributes :id, :user_name, :avatar_normal, :cover_normal,
             :organizations_count, :forwarded_campaigns_count,
             :auth_token, :max_sms, :sms_total, :sms_last_month, :is_private

  def sms_total
    object.campaign_client_receivers.count
  end

  def sms_last_month
    object.campaign_client_receivers.where('campaign_client_receivers.created_at > ?', Date.today - 29.days).count
  end
end
