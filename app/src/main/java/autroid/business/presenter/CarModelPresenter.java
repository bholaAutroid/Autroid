package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.CarModelsResponse;
import autroid.business.model.response.CarVariantResponse;
import autroid.business.view.activity.addCar.SelectModelActivity;
import autroid.business.view.activity.addCar.SelectVariantActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/01/17.
 */

public class CarModelPresenter {

    private SelectModelActivity mFragment;
    private ViewGroup mMainLayout;
    private SelectVariantActivity mFragmentVariant;

    public CarModelPresenter(SelectModelActivity frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public CarModelPresenter(SelectVariantActivity frag, ViewGroup mainLayout) {
        mFragmentVariant = frag;
        mMainLayout = mainLayout;
    }

    public void getCarModels(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarModelsResponse> myCall = apiRequest.getMakersList(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarModelsResponse>() {
            @Override
            public void success(final Response<CarModelsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarModelsResponse carItemsResponse = response.body();
                            mFragment.onSuccess(carItemsResponse);
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


    public void getCarVariant(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragmentVariant.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarVariantResponse> myCall = apiRequest.getVariantList(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarVariantResponse>() {
            @Override
            public void success(final Response<CarVariantResponse> response) {
                mFragmentVariant.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarVariantResponse carItemsResponse = response.body();
                            mFragmentVariant.onSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragmentVariant.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
