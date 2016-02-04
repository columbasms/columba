# noinspection RubyInstanceMethodNamingConvention,RubyScope
class Organizations::RegistrationsController < Devise::RegistrationsController
  before_filter :configure_sign_up_params
  before_filter :configure_account_update_params, only: [:update]
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
  def edit
    @organization = current_organization
    render :edit, layout: 'application_dashboard'
  end

  # PUT /resource
  def update
    self.resource = resource_class.to_adapter.get!(send(:"current_#{resource_name}").to_key)
    prev_unconfirmed_email = resource.unconfirmed_email if resource.respond_to?(:unconfirmed_email)

    resource_updated = update_resource(resource, account_update_params)
    yield resource if block_given?
    if resource_updated
      if is_flashing_format?
        flash_key = update_needs_confirmation?(resource, prev_unconfirmed_email) ?
            :update_needs_confirmation : :updated
        set_flash_message :notice, flash_key
      end
      sign_in resource_name, resource, bypass: true
      respond_with resource, location: after_update_path_for(resource)
    else
      clean_up_passwords resource
      render :edit, layout: 'application_dashboard'
    end
  end

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
    devise_parameter_sanitizer.for(:sign_up).push(:organization_name, :description)
  end

  # If you have extra params to permit, append them to the sanitizer.
  def configure_account_update_params
    devise_parameter_sanitizer.for(:account_update).push(:organization_name, :description)
  end

  # The path used after sign up.
  def after_sign_up_path_for(resource)
    '/account-locked'
  end

  # The path used a fter sign up for inactive accounts.
  def after_inactive_sign_up_path_for(resource)
    '/account-locked'
  end
end
