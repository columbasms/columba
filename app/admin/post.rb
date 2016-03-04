ActiveAdmin.register Post do

  menu parent: 'Blog'

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
  permit_params :title, :content, :admin_user_id, :photo,
                :category_id, :published, tag_ids: [], tags_attributes: [:id, :title]
#
# or
#
# permit_params do
#   permitted = [:permitted, :attributes]
#   permitted << :other if resource.something?
#   permitted
# end

  member_action :clone, method: :get do
    post = Post.find(params[:id])
    @post = post.dup
    render :new, :layout => false
  end

  action_item :only => :show do
    link_to("Make a Copy", clone_admin_post_path(id: post.id))
  end

  filter :admin_user
  filter :category
  filter :tags
  filter :title
  filter :published

  index do
    selectable_column
    id_column
    column :title
    column :admin_user
    column :published
    column :created_at
    actions
  end

  form do |f|
    f.semantic_errors
    f.inputs 'General' do
      f.input :title
      f.input :content, input_html: { class: 'tinymce' }
      f.input :admin_user, as: :select2
      f.input :photo
    end
    f.inputs 'Details' do
      f.input :published
      f.input :category, as: :select2
      f.input :tags, as: :select2, input_html: { multiple: true }
      f.has_many :tags, new_record: 'Add Tag', allow_destroy: true do |t|
        t.input :title
      end
    end
    f.actions
  end

end
