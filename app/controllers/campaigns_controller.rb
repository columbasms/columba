class CampaignsController < ApplicationController
  before_filter :authenticate_organization!
  before_action :set_campaign, only: [:show, :edit, :update, :crop, :stop, :statistics]
  before_action :validate_visibility
  layout 'application_dashboard'

  # GET /campaigns
  # GET /campaigns.json
  def index
    @campaigns = current_organization.campaigns
    render :index
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

  # GET /campaigns/{id}/edit
  def edit
  end

  # PUT /campaigns/{id}
  def update
    respond_to do |f|
      if @campaign.update(campaign_params)
        # Spam check
        begin
          Rails.logger.info "SPAM_SCORE:"+SpamController.new.check_spam_score(@campaign.message)
        rescue
          Rails.logger.info "SPAM_SCORE: spam service error"
        end

        f.html { redirect_to @campaign, notice: t('campaigns.edited') }
        f.json { render json: @campaign, root: false }
      else
        f.html { render 'edit' }
        f.json { render json: @campaign.errors, root: false }
      end
    end
  end

  # POST /campaigns
  # POST /campaigns.json
  def create
    @campaign = Campaign.new(campaign_params)
    @campaign.organization = current_organization
    date = Date.strptime campaign_params[:expires_at], '%d/%m/%Y'
    @campaign.expires_at = date if date.present?

    # Spam check
    begin
      Rails.logger.info "SPAM_SCORE:"+SpamController.new.check_spam_score(@campaign.message)
    rescue
      Rails.logger.info "SPAM_SCORE: spam service error"
    end

    respond_to do |format|
      if @campaign.save

        if DigitsClient.exists?
          gcm = GCM.new(Rails.application.secrets[:gcm_api_key])

          options = {
              data: {
                  organization_name: current_organization.organization_name,
                  avatar_normal: current_organization.avatar_normal,
                  message: @campaign.message,
                  campaign_id: @campaign.id
              }
          }
          response = gcm.send_with_notification_key "/topics/organization_#{current_organization.id}", options
          Rails.logger.info response
        end

        format.html { redirect_to @campaign, notice: I18n.t('campaigns.created') }
        format.json { render :show, status: :created, location: @campaign }
      else
        format.html { render :new }
        format.json { render json: @campaign.errors, status: :unprocessable_entity }
      end
    end
  end

  # GET|POST /campaigns/:id/crop
  def crop

  end

  # DELETE /campaigns/:id/stop
  def stop
    if @campaign.deactivate
      flash[:notice] = t('campaigns.deactivated')
    else
      flash[:danger] = t('campaigns.error')
    end
    redirect_to :back
  end

  def statistics
    campaign_analytic_today = @campaign.campaign_analytics.last
    @data = {}
    if campaign_analytic_today.present?
      @data[:supporters] = campaign_analytic_today.supporters
      @data[:sent_sms] = campaign_analytic_today.sent_sms
    else
      @data[:supporters] = 0
      @data[:sent_sms] = 0
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_campaign
      begin
        @campaign = Campaign.find(params[:id])

        #   checks if the requested campaign belongs to the current organization
        if current_organization.campaigns.include?(Campaign.find(params[:id]))==false
          # head :unauthorized
          render json: { errors: 'Not your campaign' }, status: :unauthorized
        end
      rescue ActiveRecord::RecordNotFound
        render json: { errors: 'Campaign not found' }, status: :not_found
      end

    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def campaign_params
      params[:campaign].permit(:message, :region_id, :province_id, :town_id, :expires_at,
                               :long_description, :photo,
                               :topic_ids => [], :campaign_address_ids => [],
                               :campaign_addresses_attributes => [:id, :address, :_destroy])
    end

    def validate_visibility
      unless current_organization.visible
        redirect_to dashboard_path, notice: I18n.t('campaigns.update_account')
      end
    end
end
