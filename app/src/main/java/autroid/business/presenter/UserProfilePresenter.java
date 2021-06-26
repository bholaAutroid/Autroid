package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.UserProfileResponse;
import autroid.business.view.fragment.carpurchase.UserProfileFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 09/13/17.
 */

public class UserProfilePresenter {
    private UserProfileFragment mFragment;
    private ViewGroup mMainLayout;

    public UserProfilePresenter(UserProfileFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getProfileInfo(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<UserProfileResponse> myCall = apiRequest.getProfile(id);
        myCall.enqueue(new ApiCallback.MyCallback<UserProfileResponse>() {
            @Override
            public void success(final Response<UserProfileResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            UserProfileResponse userProfileResponse = response.body();
                            mFragment.onSuccess(userProfileResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }





}
