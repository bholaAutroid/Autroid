package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.CarLeadRequest;
import autroid.business.model.response.CarLeadResponse;
import autroid.business.model.response.OldCarResponse;
import autroid.business.view.fragment.carpurchase.PurchaseCarFragment;

import retrofit2.Response;

/**
 * Created by pranav.mittal on 09/21/17.
 */

public class OldCarsPresenter {

    private PurchaseCarFragment mFragment;
    private ViewGroup mMainLayout;

    public OldCarsPresenter(PurchaseCarFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getShowroom(String fuel,String gear,String body,String min,String max,String color,String model,String brand,String range,Double latitude,Double longitude,String postedBy,int page,Boolean isLoading) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<OldCarResponse> myCall = apiRequest.getUsedCar( fuel, gear, body, min,max,color, model,brand,range,latitude,longitude,postedBy,page);
        myCall.enqueue(new ApiCallback.MyCallback<OldCarResponse>() {
            @Override
            public void success(final Response<OldCarResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            OldCarResponse carListResponse = response.body();
                            mFragment.onSuccess(carListResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, isLoading);
    }

    public void carLead(CarLeadRequest carLeadRequest, final String userId, final String type) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarLeadResponse> myCall = apiRequest.carLead(carLeadRequest);
        myCall.enqueue(new ApiCallback.MyCallback<CarLeadResponse>() {
            @Override
            public void success(final Response<CarLeadResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarLeadResponse carItemsResponse = response.body();
                            mFragment.onLeadSuccess(carItemsResponse,userId,type);
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
