class LocationController < ApplicationController

  def town_by_province
    if params[:province_id].present? and params[:first].present?
      @towns = Town.where province_id: params[:province_id]
      data = [{ id: '', text: '' }] + @towns.map { |t| { id: t.id, text: t.name } }
      render json: {
          data: data,
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

  def provinces_by_region
    if params[:region_id].present?
      @provinces = Province.where region_id: params[:region_id]
      data = @provinces.map { |t| { id: t.id, text: t.name } }.push({ id: '', text: '' })
      render json: {
          data: data,
          selected: ''
      }, root: false
    else
      render json: { error: 'region_id not set' }, status: 500
    end
  end

end
