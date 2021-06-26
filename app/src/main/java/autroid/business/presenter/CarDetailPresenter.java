package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddOwnerRequest;
import autroid.business.model.request.CarLeadRequest;
import autroid.business.model.request.CarSoldRequest;
import autroid.business.model.response.AddOwnerResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarLeadResponse;
import autroid.business.model.response.CarSoldResponse;
import autroid.business.model.response.CarStockResponse;
import autroid.business.model.response.GetUserResponse;
import autroid.business.view.fragment.carsales.UsedCarDetailFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 11/23/17.
 */

public class CarDetailPresenter {

    private UsedCarDetailFragment mFragment;
    private ViewGroup mMainLayout;

    public CarDetailPresenter(UsedCarDetailFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getCarDetails(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarStockResponse> myCall = apiRequest.getCarDetails(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarStockResponse>() {
            @Override
            public void success(final Response<CarStockResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarStockResponse carItemsResponse = response.body();
                            mFragment.onDetailSuccess(carItemsResponse);
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

    public void addOwner(AddOwnerRequest addOwnerRequest){
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<AddOwnerResponse> myCall=apiRequest.addOwner(addOwnerRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddOwnerResponse>() {
            @Override
            public void success(Response<AddOwnerResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.OnSuccessAddOwner(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getUser(String mobile) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserResponse> myCall = apiRequest.verifyUser(mobile, "contact_no");
        myCall.enqueue(new ApiCallback.MyCallback<GetUserResponse>() {
            @Override
            public void success(Response<GetUserResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseData().size()>0) {
                        GetUserResponse getUserResponse = response.body();
                        mFragment.onSuccessGetUser(getUserResponse);
                    }else if(response.body().getResponseData().size()==0){
                        mFragment.notFound();
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void soldCar(CarSoldRequest carSoldRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.markCarSold(carSoldRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onCarSold(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void soldCarOTP(CarSoldRequest carSoldRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarSoldResponse> myCall = apiRequest.markCarSoldOTP(carSoldRequest);
        myCall.enqueue(new ApiCallback.MyCallback<CarSoldResponse>() {
            @Override
            public void success(Response<CarSoldResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onCarSoldOTP(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void carLead(CarLeadRequest carLeadRequest, final String userId, final String type, final String action) {
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
                            mFragment.onLeadSuccess(carItemsResponse,userId,type,action);
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
