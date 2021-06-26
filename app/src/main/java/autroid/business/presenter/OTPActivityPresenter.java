package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PhoneVerificationRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.view.activity.OTPActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 01/20/18.
 */

public class OTPActivityPresenter {
    private OTPActivity mActivity;
    private ViewGroup mMainLayout;

    public OTPActivityPresenter(OTPActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void resetPhone(PhoneVerificationRequest phoneVerificationRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.resetPasswordVerify(phoneVerificationRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onResetSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout, Boolean.FALSE);
    }

    public void validatePhone(PhoneVerificationRequest phoneVerificationRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.verifyNumber(phoneVerificationRequest);
        myCall.enqueue(new ApiCallback.MyCallback<LoginResponse>() {
            @Override
            public void success(final Response<LoginResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            LoginResponse loginResponse = response.body();
                            mActivity.onVerifySuccess(loginResponse);
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

    public void resendOtp(PhoneVerificationRequest phoneVerificationRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.resendOtp(phoneVerificationRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onResendSuccess(baseResponse);
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
}
