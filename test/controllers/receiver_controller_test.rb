require 'test_helper'

class ReceiverControllerTest < ActionController::TestCase
  test "should get status" do
    get :status
    assert_response :success
  end

  test "should get blacklist" do
    get :blacklist
    assert_response :success
  end

end
