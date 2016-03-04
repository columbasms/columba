class PostsController < ApplicationController
  protect_from_forgery except: [:tinymce_assets_create]
  before_filter :set_post, only: [:show]
  layout 'application_frontend'

  def tinymce_assets_create
    p = params.permit(:file, :alt, :hint)
    geometry = Paperclip::Geometry.from_file p[:file]
    image = PostImage.create p

    render json: {
        image: {
            url: image.file.url,
            height: geometry.height.to_i,
            width: geometry.width.to_i
        }
    }, layout: false, content_type: 'text/html'
  end

  def index
    @posts = Post.published

    if params[:category].present?
      @posts = @posts.where(category_id: params[:category])
      @category = Category.find(params[:category])
    end

    if params[:tag].present?
      @posts = @posts.includes(:tags).where(tags: { id: params[:tag] })
      @tag = Tag.find params[:tag]
    end

    @posts = @posts.paginate(page: params[:page], per_page: 5).order('posts.created_at DESC')
  end

  def show
  end

  private

  def set_post
    @post = Post.find params[:id]
  end

end
