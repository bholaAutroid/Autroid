package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.bean.BusinessPlanResponse;
import autroid.business.model.request.FcmTokenRequest;
import autroid.business.model.request.RefreshTokenRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/05/17.
 */

public class HomeScreenPresenter {

    private HomeScreen mActivity;
    private ViewGroup mMainLayout;

    public HomeScreenPresenter(HomeScreen frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void validateUser(RefreshTokenRequest loginPostRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.refreshToken(loginPostRequest);
        myCall.enqueue(new ApiCallback.MyCallback<LoginResponse>() {
            @Override
            public void success(final Response<LoginResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            LoginResponse loginPostResponse = response.body();
                            mActivity.onLoginSuccess(loginPostResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout,Boolean.TRUE);
    }

    public void getCarItems() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<CarItemsResponse> myCall = apiRequest.getCarItems();
        myCall.enqueue(new ApiCallback.MyCallback<CarItemsResponse>() {
            @Override
            public void success(final Response<CarItemsResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarItemsResponse carItemsResponse = response.body();
                            mActivity.onItemSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },mActivity, mMainLayout, Boolean.FALSE);
    }

    public void logout() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.logout();
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onLogoutSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity,null,Boolean.FALSE);
    }

    public void getShowroom() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<BusinessPlanResponse> myCall = apiRequest.getNavigation();
        myCall.enqueue(new ApiCallback.MyCallback<BusinessPlanResponse>() {
            @Override
            public void success(final Response<BusinessPlanResponse> response) {
                    mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BusinessPlanResponse showroomProfileResponse = response.body();
                            mActivity.onSuccess(showroomProfileResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout,Boolean.TRUE);
    }

    public void sendRegistrationToServer(String refreshtoken) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        FcmTokenRequest fcmTokenRequest=new FcmTokenRequest();
        fcmTokenRequest.setFcmId(refreshtoken);
        fcmTokenRequest.setDeviceId(Utility.getDeviceId());
        fcmTokenRequest.setDeviceModel(Utility.getDeviceModelName());
        fcmTokenRequest.setDeviceType("Android");
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.refreshFcmToken(fcmTokenRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {

            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity,null,Boolean.FALSE);
    }
}
