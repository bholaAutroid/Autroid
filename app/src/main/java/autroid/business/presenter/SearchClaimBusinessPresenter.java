package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.SearchBusinessResponse;
import autroid.business.view.activity.SearchClaimBusinessActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/13/17.
 */

public class SearchClaimBusinessPresenter {

    private SearchClaimBusinessActivity mActivity;
    private ViewGroup mMainLayout;

    public SearchClaimBusinessPresenter(SearchClaimBusinessActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void getSearchedBusiness(String query) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<SearchBusinessResponse> myCall = apiRequest.searchBussiness(query);
        myCall.enqueue(new ApiCallback.MyCallback<SearchBusinessResponse>() {
            @Override
            public void success(final Response<SearchBusinessResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SearchBusinessResponse searchBusinessResponse = response.body();
                            mActivity.onSearchSuccess(searchBusinessResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout,Boolean.FALSE);
    }
}
