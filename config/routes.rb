Rails.application.routes.draw do
  devise_for :admin_users, ActiveAdmin::Devise.config
  ActiveAdmin.routes(self)
  devise_for :organizations, controllers: {
      sessions: 'organizations/sessions',
      registrations: 'organizations/registrations'
  }
  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  root 'welcome#index'

  get '/town-by-province' => 'location#town_by_province', as: :town_by_province
  get '/provinces-by-region' => 'location#provinces_by_region', as: :provinces_by_region

  get '/dashboard' => 'welcome#dashboard', as: :dashboard
  get '/dashboard/campaigns' => 'campaigns#index', as: :index_campaigns
  get '/dashboard/campaigns/filter' => 'campaigns#filter'
  get '/account-locked' => 'welcome#account_locked', as: :account_locked

  namespace :api do
    namespace :v1 do
      resources :users do
        member do
          get :campaigns
          post '/campaigns/:campaign_id', to: 'users#send_campaign'
        end
        member do
          get :organizations
        end
        member do
          get :topics
        end
      end
      resources :organizations do
        member do
          get :campaigns
        end
      end
      resources :campaigns
      resources :topics do
        member do
          get :campaigns
          get :organizations
        end
      end
    end
  end

  resources :campaigns, except: [ :index, :update, :destroy ]

  post '/organizations/:id/upload' => 'organizations#upload', as: :organizations_upload
  match '/organizations/:id/crop' => 'organizations#crop', as: :organizations_crop, via: [:post, :patch]

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', :as => :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end