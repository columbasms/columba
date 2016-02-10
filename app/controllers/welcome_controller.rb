class WelcomeController < ApplicationController
  before_action :authenticate_organization!, only: [:dashboard]

  def index
    if organization_signed_in?
      redirect_to dashboard_path
    else
      render 'welcome/index', layout: 'application_login'
    end
  end

  def dashboard
    @campaigns = Campaign.order(:created_at => :desc).limit 5
    render 'welcome/dashboard', layout: 'application_dashboard'
  end

  def account_locked
    render 'welcome/account_locked', layout: 'application_login_no_content'
  end

end