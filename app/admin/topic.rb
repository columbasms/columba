ActiveAdmin.register Topic do

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
 permit_params :id, :name, :description, :main_color, :status_color
#
# or
#
# permit_params do
#   permitted = [:permitted, :attributes]
#   permitted << :other if resource.something?
#   permitted
# end

 form do |f|
  f.semantic_errors
  f.inputs 'Topics' do
   f.input :name
   f.input :description
   f.input :main_color, input_html: { class: 'colorpicker' }
   f.input :status_color, input_html: { class: 'colorpicker' }
  end
  f.actions
 end

end
