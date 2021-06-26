package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.ResetPasswordRequest;
import autroid.business.model.response.LoginResponse;
import autroid.business.view.activity.PasswordResetActivity;
import retrofit2.Response;

public class ResetPasswordPresenter {

    private PasswordResetActivity mActivity;
    private ViewGroup mMainLayout;

    public ResetPasswordPresenter(PasswordResetActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.resetPassword(resetPasswordRequest);
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
}
