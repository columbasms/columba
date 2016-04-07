class Timestamp
  def initialize(app)
    @app = app
  end

  def call(env)
    env[:timestamp] = Time.current
    @app.call(env)
  end
end

