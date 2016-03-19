class OrganizationAnalytic < ActiveRecord::Base

  belongs_to :organization

  validates :created_at, uniqueness: { scope: :organization_id }

end
