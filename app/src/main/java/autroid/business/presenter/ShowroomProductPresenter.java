package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ShowroomProductResponse;
import autroid.business.view.fragment.profile.ShowroomProductsFragment;
import retrofit2.Response;

public class ShowroomProductPresenter {

    private ShowroomProductsFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomProductPresenter(ShowroomProductsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getAllProducts(String showroomID, final int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomProductResponse> myCall = apiRequest.getShowroomProducts(showroomID,page);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomProductResponse>() {
            @Override
            public void success(final Response<ShowroomProductResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomProductResponse showroomProductResponse = response.body();
                            mFragment.onSuccess(showroomProductResponse,page);
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
