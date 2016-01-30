class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  def after_sign_in_path_for(resource)
    sign_in_url = new_organization_session_url
    if request.referer == sign_in_url
      super
    else
      stored_location_for(resource) || request.referer || dashboard_path
    end
  end
end
