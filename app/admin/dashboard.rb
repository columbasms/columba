ActiveAdmin.register_page "Dashboard" do

  menu priority: 1, label: proc{ I18n.t("active_admin.dashboard") }

  content title: proc{ I18n.t("active_admin.dashboard") } do
    # Here is an example of a simple dashboard with columns and panels.
    #
    columns do
      column do
        panel 'Recent Organizations' do
          table_for Organization.all.order(created_at: :desc).limit(5) do
            column :organization_name
            column :email
            column :created_at
            column('actions') do |organization|
              if organization.locked
                link_to 'Unlock', unlock_admin_organization_path(organization)
              else
                link_to 'Lock', lock_admin_organization_path(organization)
              end
            end
          end
        end
      end

      column do
        panel "Info" do
          para "Welcome to ActiveAdmin."
        end
      end
    end
  end # content
end
