class DigitsClientsTopic < ActiveRecord::Base

  belongs_to :digits_client
  belongs_to :topic

  validates_associated :digits_client
  validates_associated :topic

end
