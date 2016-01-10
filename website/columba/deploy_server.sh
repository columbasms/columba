#!/bin/bash

echo "---------------------- Symfony deploy script server ----------------------\n\n"

echo "Stopping webserver..."
sudo service apache2 stop

echo "Fetching latest changes..."
git pull origin site

echo "Installing latest dependencies..."
composer install --working-dir=/var/www/columba/website/columba --no-dev --optimize-autoloader

echo "Clearing cache..."
php /var/www/columba/website/columba/bin/console cache:clear --env=prod --no-debug

echo "Dumping and installing assets..."
php /var/www/columba/website/columba/bin/console assetic:dump --env=prod --no-debug
php /var/www/columba/website/columba/bin/console assets:install --env=prod --no-debug

echo "Starting webserver..."
sudo service apache2 start