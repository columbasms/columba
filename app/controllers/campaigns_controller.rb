class CampaignsController < ApplicationController
  before_filter :authenticate_organization!
  before_action :set_campaign, only: [:show, :edit, :update, :destroy]
  before_action :validate_visibility
  layout 'application_dashboard'

  # GET /campaigns
  # GET /campaigns.json
  def index
    @campaigns = current_organization.campaigns
    render :index, layout: 'application_dashboard'
  end

  def filter
    @campaigns = current_organization.campaigns
    count = @campaigns.length
    @campaigns = @campaigns.limit 5

    render json: {
        draw: params[:draw].to_i,
        recordsTotal: count,
        recordsFiltered: @campaigns.count,
        data: @campaigns
    }, root: false
  end

  # GET /campaigns/1
  # GET /campaigns/1.json
  def show
  end

  # GET /campaigns/new
  def new
    @campaign = Campaign.new
  end

  # GET /campaigns/1/edit
  def edit
  end

  # POST /campaigns
  # POST /campaigns.json
  def create
    Rails.logger.info campaign_params.inspect
    @campaign = Campaign.new(campaign_params)
    @campaign.organization = current_organization

    respond_to do |format|
      if @campaign.save

        if DigitsClient.count > 0
          gcm = GCM.new(::Settings.gcm_token)

          registration_ids = DigitsClient.pluck :gcm_token
          options = {
              data: {
                  title: 'Message title',
                  message: @campaign.message
              }
          }
          response = gcm.send registration_ids, options
          Rails.logger.info response.pretty_print_inspect
        end

        format.html { redirect_to dashboard_path, notice: 'Campaign was successfully created.' }
        format.json { render :show, status: :created, location: @campaign }
      else
        format.html { render :new }
        format.json { render json: @campaign.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /campaigns/1
  # PATCH/PUT /campaigns/1.json
  def update
    respond_to do |format|
      if @campaign.update(campaign_params)
        format.html { redirect_to @campaign, notice: 'Campaign was successfully updated.' }
        format.json { render :show, status: :ok, location: @campaign }
      else
        format.html { render :edit }
        format.json { render json: @campaign.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /campaigns/1
  # DELETE /campaigns/1.json
  def destroy
    @campaign.destroy
    respond_to do |format|
      format.html { redirect_to campaigns_url, notice: 'Campaign was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_campaign
      @campaign = Campaign.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def campaign_params
      params[:campaign].permit(:message, :region_id, :province_id, :town_id, :address, :topic_ids => [])
    end

    def validate_visibility
      unless current_organization.visible
        redirect_to dashboard_path, notice: 'You have to update your account in order to access all the functionalities'
      end
    end
end
