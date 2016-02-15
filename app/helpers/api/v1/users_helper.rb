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
    client.enabled = true
    client.phone_number = digits['phone_number']
    client.digits_token = digits['access_token']['token']
    client.digits_secret = digits['access_token']['secret']
    client.id_str = digits['id_str']
    client.digits_verification_type = digits['verification_type']
    client.digits_id = digits['id']
    client.gcm_token = gcm_token
    client.save

    client.user_name = "user_#{client.id}"
    client.save

    client

  end


  # ritorna l'hash di un numero richieso
  # IMPORTANTE PER IL FUTURO questa funzione dovrebbe trovarssi in una zona separata e sicura del DB.
  def self.hash_receiver(clear_number)
    require 'digest'
    require 'openssl'
    digest=OpenSSL::Digest.new('sha512')
    fixed_salt = "6C=rCo#i,$f6j`cC6^o(bb{]}RpErX4kYiG:MEy!3EHxKZK?&@fsB7>GQXqEfu8:w2&z~5{z2M$eb%Y:)u?£^vwG&DN]2+JTM*mNB&Ub5!8xQXVK(>b~6x8?a6b8'3/["
    fixed_key = "utq54oD~<Ha<XB~fzkJU,7EndXV4u]4@AijoBiUcPaf%Gf}w5?`HE^X*LX~($LypGu(Ni^GPi;=:jf}3TqZ97giV~5<t5A}^ue=[qAe]`wLY3M&8@c73*duBp5S*~J4b"
    #put the key in a file in the server not accessible place
    #sha2 crypto hash clear_number + salt
    #hmac key encrypt with key

    hash= Digest::SHA2.hexdigest(clear_number+fixed_salt)
    return OpenSSL::HMAC.hexdigest(digest, fixed_key, hash)
  end

  # aggiunge un nuovo utente foglia al DB
  def self.add_receiver(hashed_number)
    existing_receiver=Receiver.find_by_number(hashed_number)
    if !existing_receiver.nil?
      return existing_receiver.id
    end

    receiver = Receiver.new
    receiver.number = hashed_number
    receiver.blacklisted = false

    receiver.save

    return receiver.id
  end

  # controlla se un utente foglia non ha già ricevuto una campagna
  def self.already_reached_receiver?(receiver_id, campaign_id)
    # ATTENZIONE implementazione base, non garantisce al 100% che non avvengano collisioni:
    #     due richieste contemporanee al DB  restituirebbero entrambe 'false'!
    if CampaignClientReceiver.exists?(campaign_id: campaign_id, receiver_id: receiver_id)
      return true
    else
      return false
    end
  end

  # controlla se un utente folglia ha richiesto il blocco del servizio
  def self.blacklisted_receiver?(receiver_id)
    if Receiver.find_by_id(receiver_id).blacklisted
      return true
    else
      return false
    end
  end

  # crea una nuova relazione tra una campagna, un utente ed un utente foglia
  def self.add_campaign_client_receiver_relation(campaign, client, receiver)

    #inserire relazione tra campagna e digit client

    new_ternary_relation = CampaignClientReceiver.new
    new_ternary_relation.campaign = campaign
    new_ternary_relation.digits_client = client
    new_ternary_relation.receiver = receiver

    new_ternary_relation.save
  end
end
