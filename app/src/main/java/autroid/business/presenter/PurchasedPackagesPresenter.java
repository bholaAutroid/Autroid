package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.PurchasedPackagesResponse;
import autroid.business.view.fragment.PurchasedPackagesFragment;

import retrofit2.Response;

public class PurchasedPackagesPresenter {
    private PurchasedPackagesFragment mFragment;
    private ViewGroup mMainLayout;

    public PurchasedPackagesPresenter(PurchasedPackagesFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getPackages() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PurchasedPackagesResponse> myCall = apiRequest.purchasedPackages();
        myCall.enqueue(new ApiCallback.MyCallback<PurchasedPackagesResponse>() {
            @Override
            public void success(final Response<PurchasedPackagesResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            PurchasedPackagesResponse purchasedPackagesResponse = response.body();
                            mFragment.onSuccess(purchasedPackagesResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
