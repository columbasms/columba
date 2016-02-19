ActiveAdmin.register Topic do

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
 permit_params :id, :name, :description, :main_color, :status_color, :image, :locale
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
   f.input :image, image_preview: true
    # f.input :locale, input_html:  { name: 'topic[locale]' }
  end
  f.actions
 end

end
