package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.CarListResponse;
import autroid.business.view.fragment.carsales.BookingCarFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 01/17/18.
 */

public class SelectCarBookingPresenter {

    private BookingCarFragment mFragment;
    private ViewGroup mMainLayout;

    public SelectCarBookingPresenter(BookingCarFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getCarStock() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarListResponse> myCall = apiRequest.getCarStock(-1);
        myCall.enqueue(new ApiCallback.MyCallback<CarListResponse>() {
            @Override
            public void success(final Response<CarListResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarListResponse carStockResponse = response.body();
                            mFragment.onSuccess(carStockResponse);
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
