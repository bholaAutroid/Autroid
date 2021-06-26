package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarListResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.view.fragment.carsales.MyGarageFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/10/17.
 */

public class CarStockPresenter {

    private MyGarageFragment mFragment;
    private ViewGroup mMainLayout;

    public CarStockPresenter(MyGarageFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getCarStock(final int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarListResponse> myCall = apiRequest.getGarageCarsList(page);
        myCall.enqueue(new ApiCallback.MyCallback<CarListResponse>() {
            @Override
            public void success(final Response<CarListResponse> response) {

                mFragment.getActivity().runOnUiThread(() -> {

                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessCars(response.body(), page);
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void UnPublishCar(final PublishUnpublishRequest publishUnpublishRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PublishResponse> myCall = apiRequest.putUnPublishCarRequest(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<PublishResponse>() {
            @Override
            public void success(final Response<PublishResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                        if (response.body().getResponseCode() == 200) {
                            mFragment.unPublishResponse(response.body(), publishUnpublishRequest.getCar());
                        }
                    });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }

    public void deleteCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeCar(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessDelete(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

}
