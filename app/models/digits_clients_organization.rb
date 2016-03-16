class DigitsClientsOrganization < ActiveRecord::Base

  belongs_to :organization
  belongs_to :digits_client

  validates_associated :organization
  validates_associated :digits_client

  def this_month_followers(organization_id)
    self.where(organization_id: organization_id).where('created_at >= ?', Date.today.at_beginning_of_month)
  end

end
