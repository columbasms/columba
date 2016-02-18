class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception
  layout :layout_by_resource
  before_action :set_locale

  def after_sign_in_path_for(resource)
    sign_in_url = new_organization_session_url
    if request.referer == sign_in_url
      super
    else
      stored_location_for(resource) || request.referer || dashboard_path
    end
  end

  def set_locale
    if controller_name.split('::').first != 'Api'
      if language_change_necessary?
        I18n.locale = the_new_locale
        set_locale_cookie(I18n.locale)
      else
        use_locale_from_cookie
      end
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

  # A locale change is necessary if no locale cookie is found, or if the locale param has been specified
  def language_change_necessary?
    cookies['locale'].nil? || params[:locale]
  end

  # The new locale is taken from the current_user language setting, it logged_in, or from the http accept language header if not
  # In both cases, if a locale param has been passed, it takes precedence. Only available locales are accepted
  def the_new_locale
    new_locale = (params[:locale] || extract_locale_from_accept_language_header)
    %w(en it).include?(new_locale) ? new_locale : I18n.default_locale.to_s
  end

  # Sets the locale cookie
  def set_locale_cookie(locale)
    cookies['locale'] = locale.to_s
  end

  # Reads the locale cookie and sets the locale from it
  def use_locale_from_cookie
    I18n.locale = cookies['locale']
  end

  # Extracts the locale from the accept language header, if found
  def extract_locale_from_accept_language_header
    locale = request.env['HTTP_ACCEPT_LANGUAGE'].scan(/^[a-z]{2}/).first rescue I18n.default_locale
  end

end
