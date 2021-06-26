package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.view.fragment.carsales.AddCarFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 12/26/17.
 */

public class AddCarBookingPresenter {
    private AddCarFragment mFragment;
    private ViewGroup mMainLayout;

    public AddCarBookingPresenter(AddCarFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getCarItems() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarItemsResponse> myCall = apiRequest.getCarItems();
        myCall.enqueue(new ApiCallback.MyCallback<CarItemsResponse>() {
            @Override
            public void success(final Response<CarItemsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarItemsResponse carItemsResponse = response.body();
                          //  mFragment.onItemSuccess(carItemsResponse);
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

    public void addCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddCarResponse> myCall = apiRequest.postCar(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddCarResponse>() {
            @Override
            public void success(final Response<AddCarResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddCarResponse addCarResponse = response.body();
                            mFragment.onAddCarSuccess(addCarResponse);
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


    public void addUserCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddCarResponse> myCall = apiRequest.postUserCar(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddCarResponse>() {
            @Override
            public void success(final Response<AddCarResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddCarResponse addCarResponse = response.body();
                            mFragment.onAddUserCarSuccess(addCarResponse);
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
