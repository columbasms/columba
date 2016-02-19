class OrganizationsController < ApplicationController
  protect_from_forgery except: :upload_avatar

  before_action :set_organization, except: [:all]

  # POST /organizations/:id/upload
  def upload
    @type = params[:organization][:type]
    if @organization.update_without_password permitted_params
      if params[:touch] == 'true'
        render json: { avatar: @organization.avatar.url(:normal), cover: @organization.cover.url(:normal) }
      else
        render :upload, layout: nil
      end
    else
      render json: @organization.errors, status: 500
    end
  end

  # POST /organizations/:id/crop
  def crop
    if @organization.update_without_password permitted_params
      render json: { avatar: @organization.avatar.url(:normal), cover: @organization.cover.url(:normal) }
    else
      render json: @organization.errors, status: 500
    end
  end

  private

  def set_organization
    @organization = Organization.find params[:id]
  end

  def permitted_params
    params[:organization].permit(:avatar, :avatar_original_w, :avatar_original_h, :avatar_box_w, :avatar_aspect,
                                 :avatar_crop_x, :avatar_crop_y, :avatar_crop_w, :avatar_crop_h, :cover,
                                 :cover_original_w, :cover_original_h, :cover_box_w, :cover_aspect,
                                 :cover_crop_x, :cover_crop_y, :cover_crop_w, :cover_crop_h)
  end

end
