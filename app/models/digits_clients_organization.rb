class DigitsClientsOrganization < ActiveRecord::Base

  belongs_to :organization
  belongs_to :digits_client

  validates_associated :organization
  validates_associated :digits_client

end
