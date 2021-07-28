package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.UserSearchResponse;
import autroid.business.view.fragment.profile.MyReferralsFragment;
import retrofit2.Response;

public class MyReferralsPresenter {

    private MyReferralsFragment mFragment;
    private ViewGroup mMainLayout;

    public MyReferralsPresenter(MyReferralsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


  /*  public void getSearchedBusiness(String query) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<SearchBusinessResponse> myCall = apiRequest.searchBussiness(query);
        myCall.enqueue(new ApiCallback.MyCallback<SearchBusinessResponse>() {
            @Override
            public void success(final Response<SearchBusinessResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SearchBusinessResponse searchBusinessResponse = response.body();
                            //mFragment.onSearchSuccess(searchBusinessResponse);
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
    }*/

    public void getAnyData() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<UserSearchResponse> myCall = apiRequest.getReferrals();
        myCall.enqueue(new ApiCallback.MyCallback<UserSearchResponse>() {
            @Override
            public void success(Response<UserSearchResponse> response) {
                mFragment.getActivity().runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            mFragment.onSuccessAnyData( response.body() );
                        }
                    }
                } );
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

}
