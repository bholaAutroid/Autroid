package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.CarEagerCoinsResponse;
import autroid.business.view.fragment.profile.MyWalletFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 12/28/17.
 */

public class CarEagerCoinsPresenter {

    private MyWalletFragment mFragment;
    private ViewGroup mMainLayout;

    public CarEagerCoinsPresenter(MyWalletFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getTransactions() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarEagerCoinsResponse> myCall = apiRequest.getCoinsData();
        myCall.enqueue(new ApiCallback.MyCallback<CarEagerCoinsResponse>() {
            @Override
            public void success(final Response<CarEagerCoinsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarEagerCoinsResponse carEagerCoinsResponse = response.body();
                            mFragment.carEagerCoinsSuccess(carEagerCoinsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
