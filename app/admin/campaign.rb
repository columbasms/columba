ActiveAdmin.register Campaign do

# See permitted parameters documentation:
# https://github.com/activeadmin/activeadmin/blob/master/docs/2-resource-customization.md#setting-up-strong-parameters
#
# permit_params :list, :of, :attributes, :on, :model
#
# or
#
# permit_params do
#   permitted = [:permitted, :attributes]
#   permitted << :other if resource.something?
#   permitted
# end

  permit_params :organization_id, :message, :town_id, :province_id,
                :region_id, :address, :latitude, :longitude, :topic_ids => []

  form do |f|
    f.semantic_errors
    f.inputs 'Campaign' do
      f.input :message
      f.input :topics, as: :select2, input_html: { multiple: 'multiple' }
      f.input :organization, as: :select2
      f.input :region, as: :select2
      f.input :province, as: :select2
      f.input :town, as: :select2
      f.input :address
    end
    f.actions
  end

end
