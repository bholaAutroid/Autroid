package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.MultipleDataResponse;
import autroid.business.view.fragment.search.MainSearchFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 03/07/18.
 */

public class MainSearchPresenter {
    private MainSearchFragment mFragment;
    private ViewGroup mMainLayout;

    public MainSearchPresenter(MainSearchFragment frag, ViewGroup mainLayout) {
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

    public void getAnyData(String query,String type) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<MultipleDataResponse> myCall = apiRequest.getSearchData(query,type);
        myCall.enqueue(new ApiCallback.MyCallback<MultipleDataResponse>() {
            @Override
            public void success(Response<MultipleDataResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessAnyData(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

   /* public void getSearchedBusiness(String query) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<UserSearchResponse> myCall = apiRequest.searchBusiness(query);
        myCall.enqueue(new ApiCallback.MyCallback<UserSearchResponse>() {
            @Override
            public void success(final Response<UserSearchResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            UserSearchResponse baseResponse = response.body();
                            mFragment.onSearchUserSuccess(baseResponse);
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
}
