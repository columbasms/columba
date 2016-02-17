module Api
  module V1
    class UsersController < ApplicationController
      http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
      force_ssl unless Rails.env.development?
      protect_from_forgery except: :create
      before_filter :set_user, except: [:create]
      before_filter :set_campaign, only: [:send_campaign]
      before_filter :set_organization, only: [:follow_organization, :show_organization]
      before_filter :set_topic, only: [:follow_topic, :show_topic]

      # GET /users/:id
      def show
        render json: @user, root: false, serializer: Api::V1::DigitsClientSerializer
      end

      # POST /users/
      def create

        provider = request.headers[:'X-Auth-Service-Provider']
        if provider.nil? or provider.empty?
          render json: {errors: 'Provider must be set'}
          return
        end

        auth = request.headers[:'X-Verify-Credentials-Authorization']
        if auth.nil? or auth.empty?
          render json: {errors: 'Credentials authorization must be set'}
          return
        end

        gcm_token = request.headers[:'gcm-token']
        if gcm_token.nil? or gcm_token.empty?
          render json: {errors: 'gcm-token must be set'}
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
        if @user.update user_params
          render json: @user, root: false, serializer: Api::V1::DigitsClientSerializer
        else
          render json: @user.errors, root: false, status: :unprocessable_entity
        end
      end

      # POST /users/:id/images
      def update_images
        if !params['profile_image'].nil? and !params['profile_image'].empty?
          img=Paperclip.io_adapters.for("data:#{content_type};base64,#{params['profile_image']}")
          img.original_filename = "avatar"
          @user.avatar=img
          @user.save
        end
        if !params['cover_image'].nil? and !params['cover_image'].empty?
          cov=Paperclip.io_adapters.for("data:#{content_type};base64,#{params['cover_image']}")
          cov.original_filename = "cover"
          @user.cover=cov
          @user.save
        end
        render json: @user
      end

      # GET /users/:id/campaigns
      def campaigns
        render json: @user.campaigns.uniq, root: false
      end

      # POST /users/:id/campaigns/:campaign_id
      def send_campaign
        # leaf_list = ActiveSupport::JSON.decode(request.body.read)
        leaf_list = params['users']
        hashed_leaf_list = [] #lista ORDINATA degli hash dei numeri richiesti
        result_index_list = [] # lista da ritornare con gli indici dei numeri a cui è possibile inviare la campagna
        # result_index_hash =[] # alternativa: lista da ritornare con gli indici e gli hash dei numeri a cui è possibile inviare la campagna

        # hash dei numeri in input
        leaf_list.each do |number|
          hash = Api::V1::UsersHelper.hash_receiver(number['number'])
          hashed_leaf_list+=[hash]
        end

        hashed_leaf_list.each_with_index do |hashed_leaf, index|
          # aggiungo nel DB il ricevente (se non già presente)
          current_receiver_id=Api::V1::UsersHelper.add_receiver(hashed_leaf)

          # verifico se il ricevente non ha richiesto il blocco del servizio
          if Api::V1::UsersHelper.blacklisted_receiver?(current_receiver_id)
            next
          end
          # verifico se il ricevente è stato già raggiunto da una campagna.
          if Api::V1::UsersHelper.already_reached_receiver?(current_receiver_id, @campaign)
            next
          end
          # aggiungo nel DB la relazione tra campagna-utente-ricevente e campagna-utente
          Api::V1::UsersHelper.add_campaign_client_receiver_relation(@campaign, @user, Receiver.find_by_id(current_receiver_id))


          # aggiungo l'indice del ricevente al risultato
          result_index_list+=[index]

          # alternativa: aggiungo l'indice e l'hash del ricevente al risultato
          # result_index_hash+=[[index,hashed_leaf]]
        end

        # l'API restituisce all'utente gli indici della lista di contatti a cui può inviare l'sms
        render json: result_index_list
      end

      # GET users/:id/topics
      def topics
        result = []
        # result= Topic.active
        user_topics = @user.topics
        Topic.active.each do |current_topic|
          if current_topic.in?(user_topics)
            result += [current_topic.slice(:id, :name, :main_color, :status_color).as_json.merge(:following => true)]
          else
            result += [current_topic.slice(:id, :name, :main_color, :status_color).as_json.merge(:following => false)]
          end
        end
        render json: result, root: false
      end

      # GET users/:id/topics/:topic_id
      def show_topic
        render json: {'following' => @topic.in?(@user.topics)}, root: false
      end

      # PUT users/:id/topics/:topic_id
      def follow_topic
        # if a connection between user and topic already exists...
        connection=DigitsClientsTopic.where(digits_client_id: @user, topic_id: @topic)
        if connection.exists?
          # ...we destroy it
          connection.delete_all
        else
          # ... else we create it
          new_connection=DigitsClientsTopic.new
          new_connection.topic=@topic
          new_connection.digits_client=@user
          new_connection.save
        end
        render json: @user.topics, root: false
      end

      # GET /users/:id/organizations
      def organizations
        render json: @user.organizations, root: false
      end

      # GET /users/:id/organizations/:organization_id
      def show_organization
        user_follow=DigitsClientsOrganization.find_by(organization_id: @organization, digits_client_id: @user)
        if user_follow.nil?
          render json: @organization.slice(:organization_name, :avatar_normal, :cover_normal, :description).as_json.merge(:followers => @organization.digits_clients.count, :following => false, :trusting => false), root: false
        else
          render json: @organization.slice(:organization_name, :avatar_normal, :cover_normal, :description).as_json.merge(:followers => @organization.digits_clients.count, :following => true, :trusting => user_follow.trusted), root: false
        end
      end

      # PUT /users/:id/organizations/:organization_id
      def follow_organization
        # USER-ORGANIZATION link

        connection=DigitsClientsOrganization.where(digits_client_id: @user, organization_id: @organization)

        # if a connection between user and topic already exists...
        if connection.exists?
          # ....and the trust option is not present...
          if params[:trusted].nil?
            # ...we destroy it
            connection.delete_all
          else
            # ...else we update the trust setting of the connection
            if params[:trusted]=='true'
              connection.update_all(trusted: true)
            else
              connection.update_all(trusted: false)
            end

          end

        else
          # ... else we create it
          new_connection = DigitsClientsOrganization.new
          new_connection.organization = @organization
          new_connection.digits_client = @user
          if params[:trusted].nil?
            new_connection.trusted=false
          else
            if params[:trusted]=='true'
              new_connection.trusted=true
            else
              new_connection.trusted=false
            end
          end
          new_connection.save

          # auto follow dei topic dell'associazione
          @user.topics+=@organization.topics
        end

        render json: @user.organizations, root: false
      end

      private

      def set_user
        begin
          @user = DigitsClient.find params[:id]
        rescue ActiveRecord::RecordNotFound
          render json: {errors: 'User not found'}
          return
        end
      end

      def set_campaign
        begin
          @campaign = Campaign.find params[:campaign_id]
        rescue ActiveRecord::RecordNotFound
          render json: {errors: 'Campaign not found'}
          return
        end
      end

      def set_organization
        begin
          @organization = Organization.find params[:organization_id]
        rescue ActiveRecord::RecordNotFound
          render json: {errors: 'Organization not found'}
          return
        end
      end

      def set_topic
        begin
          @topic = Topic.find params[:topic_id]
        rescue ActiveRecord::RecordNotFound
          render json: {errors: 'Topic not found'}
          return
        end
      end

      def user_params
        params[:user].permit(:user_name,:avatar, :cover, :avatar_normal, :cover_normal, :digits_id, :digits_token, :digits_secret, :phone_number, :gcm_token)
      end

    end
  end
end