ActiveAdmin.register Tag do

  menu parent: 'Blog'

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
  permit_params :title
#
# or
#
# permit_params do
#   permitted = [:permitted, :attributes]
#   permitted << :other if resource.something?
#   permitted
# end

  filter :title
  filter :posts

  index do
    selectable_column
    id_column
    column :title
    column :post_count
    actions
  end

  form do |f|
    f.semantic_errors
    f.inputs 'General' do
      f.input :title
    end
    f.actions
  end

end
