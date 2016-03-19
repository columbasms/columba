class StopController < ApplicationController
  force_ssl unless Rails.env.development?
  protect_from_forgery
  before_filter :set_receiver
  layout 'application_login_no_content'


  # GET /stop/:id
  def show
    render 'stop/ask'
  end

  # PUT /stop/:id
  # def update
  #   # TO-DO:  add a "max n° sms" to receivers table,
  #             (?) add blacklisted topics/organizations
  # end

  # DELETE /stop/:id
  def destroy
    # BLACKLIST
    @receiver.update_attribute :blacklisted, true
  end

  # PUT /stop/:id
  def unblacklist
  #   UN-BLACKLIST
    @receiver.update_attribute :blacklisted, false
  end

  private

  def set_receiver
    begin
      @receiver = Receiver.find_by_number params[:id]
      if @receiver.nil?
        render json: {errors: 'Receiver not found' }
        return
      end
    end
  end

end
