class Organizations::SessionsController < Devise::SessionsController
# before_filter :configure_sign_in_params, only: [:create]
  layout 'application_login_no_content'

  # GET /resource/sign_in
  def new
    self.resource = resource_class.new(sign_in_params)
    if params[:redirect_to].present?
      store_location_for(resource, params[:redirect_to])
    end
    clean_up_passwords(resource)
    yield resource if block_given?
    respond_with(resource, serialize_options(resource))
  end

  # POST /resource/sign_in
  def create
    self.resource = warden.authenticate!(auth_options)
    sign_in(resource_name, resource)
    yield resource if block_given?
    if self.resource.visible?
      set_flash_message(:notice, :signed_in) if is_flashing_format?
      respond_with resource, location: after_sign_in_path_for(resource)
    else
      set_flash_message(:warning, :not_visible)
      redirect_to edit_organization_registration_path(resource)
    end
  end

  # DELETE /resource/sign_out
  # def destroy
  #   super
  # end

  # protected

  # If you have extra params to permit, append them to the sanitizer.
  # def configure_sign_in_params
  #   devise_parameter_sanitizer.for(:sign_in) << :attribute
  # end
end
