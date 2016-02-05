ActiveAdmin.register Organization do

  menu parent: 'Users'

  permit_params :id, :organization_name, :locked_at, :email, :VAT_number, :password, :password_confirmation, :avatar,
                :locked, :salt, :encrypted_password, :fiscal_code, :province, :town, :address, :postal_code, :phone_number,
                :topics_attributes => [:id, :name, :description], :topic_ids => []

  filter :topics
  filter :id
  filter :email
  filter :organization_name

  config.sort_order = 'created_at_desc'

  scope 'All' do |organizations|
    organizations
  end

  scope 'Locked' do |organizations|
    organizations.locked
  end

  member_action :lock, method: :get do
    resource.locked = true
    resource.save(validate: false)
    redirect_to admin_organizations_path, notice: 'Organization locked'
  end

  member_action :unlock, method: :get do
    resource.locked = false
    resource.locked_at = nil
    resource.visible = true
    resource.save(validate: false)
    redirect_to admin_organizations_path, notice: 'Organization unlocked'
  end

  controller do

    def update
      @organization = Organization.find(permitted_params[:id])
      if params[:organization][:password].blank?
        @organization.update_without_password(permitted_params[:organization])
      else
        @organization.update_attributes(permitted_params[:organization])
      end
      if @organization.errors.blank?
        redirect_to admin_organizations_path, :notice => 'Organization updated successfully.'
      else
        render :edit
      end
    end

  end

  index do
    selectable_column
    id_column
    column :email
    column :organization_name
    column :created_at
    column :locked_at
    actions defaults: true do |organization|
      if organization.locked
        link_to 'Unlock', unlock_admin_organization_path(organization)
      else
        link_to 'Lock', lock_admin_organization_path(organization)
      end
    end
  end

  action_item only: :show do
    if organization.locked
      link_to 'Unlock Organization', unlock_admin_organization_path(organization)
    else
      link_to 'Lock Organization', lock_admin_organization_path(organization)
    end
  end

  index as: :grid do |organization|
    link_to organization.organization_name, '/'
  end

  form do |f|
    f.semantic_errors
    f.inputs 'General' do
      f.input :email
      f.input :password, type: :password
      f.input :password_confirmation, type: :password
      f.input :locked, as: :boolean
    end
    f.inputs 'Organization' do
      f.input :organization_name
      f.input :VAT_number
      f.input :topics, as: :select2_multiple, :wrapper_html => { :style => 'width: auto;' }
      f.input :avatar, image_preview: true
    end
    f.actions
  end

  show do
    attributes_table do
      row :id
      row :VAT_number
      row :fiscal_code
      row :province
      row :town
      row :address
      row :postal_code
      row :phone_number
      row :avatar do |a|
        image_tag a.cover.url(:normal)
      end
      row :avatar do |a|
        image_tag a.avatar.url(:thumb)
      end
    end
    active_admin_comments
  end

  sidebar 'summary', only: :show do
    attributes_table_for organization do
      row :email
      row 'name' do
        organization.organization_name
      end
      row('Locked?') { |o| status_tag o.locked }
      row :visible
    end
  end

end
