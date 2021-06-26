package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddBusinessRequest;
import autroid.business.model.response.LoginResponse;
import autroid.business.model.response.RegistrationDataResponse;
import autroid.business.view.activity.RegisterActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/12/17.
 */

public class RegisterPresenter {

    private RegisterActivity mActivity;
    private ViewGroup mMainLayout;

    public RegisterPresenter(RegisterActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void getRegistrationsItems() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<RegistrationDataResponse> myCall = apiRequest.getRegistrationData();
        myCall.enqueue(new ApiCallback.MyCallback<RegistrationDataResponse>() {
            @Override
            public void success(final Response<RegistrationDataResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            RegistrationDataResponse registrationDataResponse = response.body();
                            mActivity.onItemSuccess(registrationDataResponse);
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


    public void addLocalBussiness(AddBusinessRequest addBusinessRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<LoginResponse> myCall = apiRequest.registeration(addBusinessRequest);
        myCall.enqueue(new ApiCallback.MyCallback<LoginResponse>() {
            @Override
            public void success(final Response<LoginResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            LoginResponse loginResponse = response.body();
                            mActivity.addBussSuccess(loginResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },mActivity, mMainLayout,Boolean.TRUE);
    }
}
