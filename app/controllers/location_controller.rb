class LocationController < ApplicationController

  def town_by_province
    if params[:province_id].present? and params[:first].present?
      @towns = Town.where province_id: params[:province_id]
      data = @towns.map { |t| { id: t.id, text: t.name } }
      render json: {
          towns: data,
          selected: if params[:first] == 'true'
                      current_organization.town_id.present? ? current_organization.town_id : 0
                    else
                      data[0][:id]
                    end
      }, root: false
    else
      render json: { error: 'province_id not set' }, status: 500
    end
  end

end
