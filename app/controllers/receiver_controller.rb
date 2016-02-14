class ReceiverController < ApplicationController
  force_ssl unless Rails.env.development?
  protect_from_forgery
  before_filter :set_receiver


  # GET /receiver/:id
  # def show
  #   render json: @receiver
  # end

  # PUT /receiver/:id
  # def update
  #   # TO-DO:  add a "max n° sms" to receivers table,
  #             (?) add blacklisted topics/organizations
  # end

  # DELETE /receiver/:id
  def destroy
    # BLACKLIST
    @receiver.blacklisted=true
    @receiver.save
    render json: "Receiver correclty blacklisted: #{@receiver.blacklisted}"
  end

  private

  def set_receiver
    # receiver if found by (hashed) number, not id
    begin
      @receiver = Receiver.find_by number:params[:id]
      # @receiver = Receiver.find params[:id]
    # rescue ActiveRecord::RecordNotFound
      if @receiver.nil?
        render json: {errors: 'Receiver not found'}
        return
      end
    end
  end

end
