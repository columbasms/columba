module Api
  module V1
    class UserController < ApplicationController
      http_basic_authenticate_with name: 'b2beb49014d2de5db85cadf2832c2d3c', password: '2b31e8e321709d8c22ba2845579b33cd'
      respond_to :json
      protect_from_forgery except: :register

      def register

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

        digits = Api::V1::UserHelper.twitter_verify_auth_curl(provider, auth)

        if digits.header_str.include? 'HTTP/1.1 200 OK'
          credentials = JSON.parse digits.body_str
          client = Api::V1::UserHelper.register_user credentials, gcm_token
          render json: [ client.phone_number, client.digits_token ]
        else
          render json: JSON.parse(digits.body_str)
        end

      end

      def test
        render json: { success: true  }
      end

    end
  end
end