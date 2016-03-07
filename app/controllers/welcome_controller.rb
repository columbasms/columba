class WelcomeController < ApplicationController
  before_action :authenticate_organization!, only: [:dashboard]

  def index
    render 'welcome/index', layout: 'application_frontend'
  end

  def dashboard
    @campaigns = current_organization.campaigns.not_expired.order(:created_at => :desc).limit 5
    render 'welcome/dashboard', layout: 'application_dashboard'
  end

  def account_locked
    render 'welcome/account_locked', layout: 'application_login_no_content'
  end

  # POST /contact
  def contact
    p = params[:contact].permit(:name, :email, :message)
    if p[:name].present? and p[:email].present? and p[:message].present?
      ContactMailer.contact_email(p).deliver_now
      render json: { success: true }
    else
      render json: { error: true }
    end
  end

end
