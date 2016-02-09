module Api
  module V1
    class UsersController < ApplicationController
      http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
      protect_from_forgery except: :create
      before_filter :set_user, only: [:show, :campaigns, :send_campaign]
      before_filter :set_campaign, only: [:send_campaign]

      # GET /users/:id
      def show
        render json: @user, root: false, serializer: ::DigitsClientSerializer
      end

      # GET /users/:id/campaigns
      def campaigns
        render json: @user.campaigns, root: false
      end

      # POST /users/
      def create

        provider = request.headers[:'X-Auth-Service-Provider']
        if provider.nil? or provider.empty?
          render json: { errors: 'Provider must be set' }
          return
        end

        auth = request.headers[:'X-Verify-Credentials-Authorization']
        if auth.nil? or auth.empty?
          render json: { errors: 'Credentials authorization must be set' }
          return
        end

        gcm_token = request.headers[:'gcm-token']
        if gcm_token.nil? or gcm_token.empty?
          render json: { errors: 'gcm-token must be set' }
          return
        end

        digits = Api::V1::UsersHelper.twitter_verify_auth_curl(provider, auth)

        if digits.header_str.include? 'HTTP/1.1 200 OK'
          credentials = JSON.parse digits.body_str
          client = Api::V1::UsersHelper.register_user credentials, gcm_token
          render json: client
        else
          render json: JSON.parse(digits.body_str)
        end

      end

      # PUT /user/:id
      def update
      #   TO-DO
      end

      # POST /users/:id/campaigns/:campaign_id
      def send_campaign
        leaf_list = ActiveSupport::JSON.decode(request.body.read)
        hashed_leaf_list=[] #lista ORDINATA degli hash dei numeri richiesti
        result_index_list=[] # lista da ritornare con gli indici dei numeri a cui è possibile inviare la campagna
        # result_index_hash =[] # alternativa: lista da ritornare con gli indici e gli hash dei numeri a cui è possibile inviare la campagna

        # hash dei numeri in input
        leaf_list.each do |number|
          hash = Api::V1::UsersHelper.hash_receiver(number['number'])
          hashed_leaf_list+=[hash]
        end

        hashed_leaf_list.each_with_index do |hashed_leaf,index|
          # aggiungo nel DB il ricevente (se non già presente)
          current_receiver_id=Api::V1::UsersHelper.add_receiver(hashed_leaf)

          # verifico se il ricevente non ha richiesto il blocco del servizio
          if Api__V1::UsersHelper.blacklisted_receiver?(current_receiver_id)
            next
          end
          # verifico se il ricevente è stato già raggiunto da una campagna.
          if Api::V1::UsersHelper.already_reached_receiver?(current_receiver_id, @campaign)
            next
          end
          # aggiungo nel DB la relazione tra campagna-utente-ricevente
          Api::V1::UsersHelper.add_campaign_client_receiver_relation(@campaign, @user, current_receiver_id)

          # aggiungo l'indice del ricevente al risultato
          result_index_list+=[index]

          # alternativa: aggiungo l'indice e l'hash del ricevente al risultato
          # result_index_hash+=[[index,hashed_leaf]]
        end

        # l'API restituisce all'utente gli indici della lista di contatti a cui può inviare l'sms
        render json: result_index_list
      end


      private

      def set_user
        begin
          @user = DigitsClient.find params[:id]
        rescue ActiveRecord::RecordNotFound
          render json: { errors: 'User not found'}
          return
        end
      end

      def set_campaign
        begin
          @campaign = Campaign.find params[:campaign_id]
        rescue ActiveRecord::RecordNotFound
          render json: { errors: 'User not found' }
          return
        end
      end

    end
  end
end