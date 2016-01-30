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
    render 'welcome/dashboard', layout: 'application_dashboard'
  end

end
