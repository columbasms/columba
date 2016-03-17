module Api::V1::UsersHelper

  def self.twitter_verify_auth_curl(provider, auth)
    Curl.get(provider) do |curl|
      curl.headers['Content-length'] = '0'
      curl.headers['Content-type'] = 'application/json'
      curl.headers['authorization'] = auth
    end
  end

  def self.register_user(digits, gcm_token)

    client = DigitsClient.find_or_create_by(phone_number: digits['phone_number'])
    unless client.new_record?
      client.auth_token = SecureRandom.base58(24)
    else
      random_name="user_#{(rand*10000).to_int}"
      while DigitsClient.find_by_user_name(random_name).present?
        random_name="user_#{(rand*10000).to_int}"
      end
      client.user_name = random_name
    end
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


  # return the hash of a desired phone number
  # IMPORTANT in the future this function will be placed in a complete separate and safe place of the server.
  def self.hash_receiver(clear_number)
    require 'digest'
    require 'openssl'
    digest=OpenSSL::Digest.new('sha512')
    #put the keys in a file placed securely in the server
    #sha2 crypto hash clear_number + salt
    #hmac key encrypt with key

    hash= Digest::SHA2.hexdigest(clear_number+Rails.application.secrets[:crypto_keyed_hashing][:fixed_salt])
    return OpenSSL::HMAC.hexdigest(digest, Rails.application.secrets[:crypto_keyed_hashing][:fixed_key], hash)
  end

  # aggiunge un nuovo utente foglia al DB
  def self.add_receiver(hashed_number)
    existing_receiver = Receiver.find_by_number(hashed_number)
    unless existing_receiver.nil?
      return existing_receiver
    end

    Receiver.create! number: hashed_number, blacklisted: false
  end

  # controlla se un utente foglia non ha già ricevuto una campagna
  def self.already_reached_receiver?(receiver_id, campaign_id)
    # ATTENZIONE implementazione base, non garantisce al 100% che non avvengano collisioni:
    #     due richieste contemporanee al DB  restituirebbero entrambe 'false'!
    CampaignClientReceiver.exists?(campaign_id: campaign_id, receiver_id: receiver_id)
  end

  # controlla se un utente folglia ha richiesto il blocco del servizio
  def self.blacklisted_receiver?(receiver_id)
    if (r = Receiver.find_by_id(receiver_id))
      return r.blacklisted
    else
      return false
    end
  end

  # crea una nuova relazione tra una campagna, un utente ed un utente foglia
  def self.add_campaign_client_receiver_relation(campaign, client, receiver, latitude, longitude)

    #inserire relazione tra campagna e digit client

    new_ternary_relation = CampaignClientReceiver.new
    new_ternary_relation.campaign = campaign
    new_ternary_relation.digits_client = client
    new_ternary_relation.receiver = receiver
    if latitude!= nil and longitude!= nil
      new_ternary_relation.lat= latitude
      new_ternary_relation.lng= longitude
    end

    new_ternary_relation.save
  end
end
