package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.SearchDetailResponse;
import autroid.business.view.fragment.search.SearchDetailFragment;
import retrofit2.Response;

public class SearchDetailPresenter {

    private SearchDetailFragment mFragment;
    private ViewGroup mMainLayout;

    public SearchDetailPresenter(SearchDetailFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }



    public void getSearchedUser(String query) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<SearchDetailResponse> myCall = apiRequest.searchDetail(query);
        myCall.enqueue(new ApiCallback.MyCallback<SearchDetailResponse>() {
            @Override
            public void success(final Response<SearchDetailResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SearchDetailResponse baseResponse = response.body();
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
    }
}
