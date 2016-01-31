class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception
  layout :layout_by_resource

  def after_sign_in_path_for(resource)
    sign_in_url = new_organization_session_url
    if request.referer == sign_in_url
      super
    else
      stored_location_for(resource) || request.referer || dashboard_path
    end
  end

  protected

  def layout_by_resource
    if devise_controller?
      'application_login_no_content'
    else
      'application'
    end
  end

end
