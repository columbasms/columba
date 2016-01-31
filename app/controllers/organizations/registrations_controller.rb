# noinspection RubyInstanceMethodNamingConvention
class Organizations::RegistrationsController < Devise::RegistrationsController
  before_filter :configure_sign_up_params
# before_filter :configure_account_update_params, only: [:update]
  layout 'application_login_no_content'

  # GET /resource/sign_up
  def new
    build_resource({})
    set_minimum_password_length
    yield resource if block_given?
    respond_with self.resource
  end

  # POST /resource
  def create
    super
  end

  # GET /resource/edit
  # def edit
  #   super
  # end

  # PUT /resource
  # def update
  #   super
  # end

  # DELETE /resource
  # def destroy
  #   super
  # end

  # GET /resource/cancel
  # Forces the session data which is usually expired after sign
  # in to be expired now. This is useful if the user wants to
  # cancel oauth signing in/up in the middle of the process,
  # removing all OAuth session data.
  # def cancel
  #   super
  # end

  protected

  # If you have extra params to permit, append them to the sanitizer.
  def configure_sign_up_params
    devise_parameter_sanitizer.for(:sign_up).push(:organization_name)
  end

  # If you have extra params to permit, append them to the sanitizer.
  # def configure_account_update_params
  #   devise_parameter_sanitizer.for(:account_update) << :attribute
  # end

  # The path used after sign up.
  def after_sign_up_path_for(resource)
    '/account-locked'
  end

  # The path used a fter sign up for inactive accounts.
  def after_inactive_sign_up_path_for(resource)
    '/account-locked'
  end
end
