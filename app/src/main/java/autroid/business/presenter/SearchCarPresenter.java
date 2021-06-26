package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.SearchCarResponse;
import autroid.business.view.fragment.search.SearchCarFragment;
import retrofit2.Response;

public class SearchCarPresenter {

    private SearchCarFragment mFragment;
    private ViewGroup mMainLayout;

    public SearchCarPresenter(SearchCarFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void searchCar(String car) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<SearchCarResponse> myCall = apiRequest.searchCar(car);

        myCall.enqueue(new ApiCallback.MyCallback<SearchCarResponse>() {
            @Override
            public void success(final Response<SearchCarResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SearchCarResponse baseResponse = response.body();
                            mFragment.onSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.FALSE);
    }
}
