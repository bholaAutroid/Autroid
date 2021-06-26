package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.ForgotPasswordRequest;
import autroid.business.model.request.LoginRequest;
import autroid.business.model.response.ForgotPasswordResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.view.activity.LoginActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 05/18/17.
 */

public class LoginPresenter {

    private LoginActivity mActivity;
    private ViewGroup mMainLayout;

    public LoginPresenter(LoginActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void login(LoginRequest loginPostRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.postLogin(loginPostRequest);
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


    public void validateUser(LoginRequest loginPostRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.validateAccount(loginPostRequest);
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
        }, mActivity, mMainLayout, Boolean.TRUE);
    }

    public void forgotPassword(ForgotPasswordRequest loginPostRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<ForgotPasswordResponse> myCall = apiRequest.forgotPassword(loginPostRequest);
        myCall.enqueue(new ApiCallback.MyCallback<ForgotPasswordResponse>() {
            @Override
            public void success(final Response<ForgotPasswordResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ForgotPasswordResponse baseResponse = response.body();
                            mActivity.onForgotSuccess(baseResponse);
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
}
