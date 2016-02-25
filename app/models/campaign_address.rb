class CampaignAddress < ActiveRecord::Base

  acts_as_mappable default_units: :kms,
                   distance_field_name: :distance,
                   lat_column_name: :lat,
                   lng_column_name: :lng

  belongs_to :campaign

  validates_presence_of :address

  before_save do
    g = Geokit::Geocoders::MultiGeocoder.geocode self.address
    self.lat, self.lng = g.lat, g.lng
  end

end
