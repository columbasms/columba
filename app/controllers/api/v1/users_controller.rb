module Api
  module V1
    class UsersController < ApplicationController
      http_basic_authenticate_with name: ::Settings.http_basic.name, password: ::Settings.http_basic.password
      force_ssl
      protect_from_forgery except: :create
      before_filter :set_user, only: [:show, :campaigns]

      def show
        render json: @user, root: false, serializer: ::DigitsClientSerializer
      end

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

      private

      def set_user
        begin
          @user = DigitsClient.find params[:id]
        rescue ActiveRecord::RecordNotFound
          render json: { errors: 'User not found' }
          return
        end
      end

    end
  end
end