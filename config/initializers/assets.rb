# Be sure to restart your server when you modify this file.

# Version of your assets, change this if you want to expire all your assets.
Rails.application.config.assets.version = '1.0'

# Add additional assets to the asset load path
# Rails.application.config.assets.paths << Emoji.images_path

# Precompile additional assets.
# application.js, application.css, and all non-JS/CSS in app/assets folder are already added.
Rails.application.config.assets.precompile +=
    %w( ie9.css windows.chrome.fix.css login.js favicon.ico file-upload.js file-upload.css
        application_form.js application_form.css application_data.css application_data.js )
