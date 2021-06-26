package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.AnalyticsResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.AnalyticsFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/04/17.
 */

public class AnalyticsPresenter {

    private AnalyticsFragment mFragment;
    private ViewGroup mMainLayout;

    public AnalyticsPresenter(AnalyticsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getShowroom(String department,String type,String query) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AnalyticsResponse> myCall = apiRequest.getAnalyticsData(department,type,query);
        myCall.enqueue(new ApiCallback.MyCallback<AnalyticsResponse>() {
            @Override
            public void success(final Response<AnalyticsResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                        if (response.body().getResponseCode() == 200) {
                            mFragment.onSuccess(response.body());
                        }
                });
            }

            @Override
            public void error(String errorMessage) {
            }

        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void removeMember(String userId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeMember(userId);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessRemoveMember(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }

        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

}
