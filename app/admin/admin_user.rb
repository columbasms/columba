ActiveAdmin.register AdminUser do
  permit_params :email, :name, :description, :avatar, :password, :password_confirmation

  menu parent: 'Users'

  controller do

    def update
      @admin_user = AdminUser.find(permitted_params[:id])
      if params[:admin_user][:password].blank?
        @admin_user.update_without_password(permitted_params[:admin_user])
      else
        @admin_user.update_attributes(permitted_params[:admin_user])
      end
      if @admin_user.errors.blank?
        redirect_to admin_admin_users_path, :notice => 'Admin user updated successfully.'
      else
        render :edit
      end
    end

  end

  index do
    selectable_column
    id_column
    column :email
    column :name
    column :current_sign_in_at
    column :sign_in_count
    column :created_at
    actions
  end

  filter :email
  filter :name
  filter :current_sign_in_at
  filter :sign_in_count
  filter :created_at

  show do
    attributes_table do
      row :id
      row :email
      row :name
      row :description
      row :current_sign_in_at
      row :sign_in_count
      row :created_at
      row :avatar do |a|
        image_tag a.avatar.url(:normal)
      end
    end
    active_admin_comments
  end

  form do |f|
    f.inputs "Admin Details" do
      f.input :email
      f.input :name
      f.input :description, input_html: { class: 'tinymce' }
      f.input :avatar
      f.input :password
      f.input :password_confirmation
    end
    f.actions
  end

end
