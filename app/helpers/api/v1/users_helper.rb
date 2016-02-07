module Api::V1::UsersHelper

  def self.twitter_verify_auth_curl(provider, auth)
    Curl.get(provider) do |curl|
      curl.headers['Content-length'] = '0'
      curl.headers['Content-type'] = 'application/json'
      curl.headers['authorization'] = auth
    end
  end

  def self.register_user(digits, gcm_token)


    client = DigitsClient.new
    client.enabled = true
    client.phone_number = digits['phone_number']
    client.digits_token = digits['access_token']['token']
    client.digits_secret = digits['access_token']['secret']
    client.id_str = digits['id_str']
    client.digits_verification_type = digits['verification_type']
    client.digits_id = digits['id']
    client.gcm_token = gcm_token

    client.save

    client

  end

end
