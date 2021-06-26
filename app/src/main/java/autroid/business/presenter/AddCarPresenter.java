package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.GarageCarResponse;
import autroid.business.view.fragment.carsales.EditCarFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 05/30/17.
 */



public class AddCarPresenter {

    private EditCarFragment mFragment;
    private ViewGroup mMainLayout;

    public AddCarPresenter(EditCarFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

//    public void getCarItems() {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<CarItemsResponse> myCall = apiRequest.getCarItems();
//        myCall.enqueue(new ApiCallback.MyCallback<CarItemsResponse>() {
//            @Override
//            public void success(final Response<CarItemsResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            CarItemsResponse carItemsResponse = response.body();
//                            mFragment.onItemSuccess(carItemsResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
//    }

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

//    public void getCarDetails(String id) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<CarStockResponse> myCall = apiRequest.getCarDetails(id);
//        myCall.enqueue(new ApiCallback.MyCallback<CarStockResponse>() {
//            @Override
//            public void success(final Response<CarStockResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            CarStockResponse carItemsResponse = response.body();
//                            // mFragment.onDetailSuccess(carItemsResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
//    }


    public void editCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService( mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddCarResponse> myCall = apiRequest.editCarStock(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddCarResponse>() {
            @Override
            public void success(final Response<AddCarResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddCarResponse addCarResponse = response.body();
                            mFragment.onEditSuccess(addCarResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },  mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }



//    public void publishCar(final String id, PublishUnpublishRequest publishUnpublishRequest) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<PublishResponse> myCall = apiRequest.putPublishCarRequest(publishUnpublishRequest);
//        myCall.enqueue(new ApiCallback.MyCallback<PublishResponse>() {
//            @Override
//            public void success(final Response<PublishResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            PublishResponse AddImageResponse = response.body();
//                            mFragment.publishUnpublishResponse(id,AddImageResponse);
//                        } else {
//
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
//    }

//    public void removeCar(final String id, AddCarRequest addCarRequest) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeCar(addCarRequest);
//        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
//            @Override
//            public void success(final Response<BaseResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            BaseResponse AddImageResponse = response.body();
//                            mFragment.onRemoveCar(id,AddImageResponse);
//                        } else {
//
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
//    }


//    public void removeRcPic(PublishUnpublishRequest publishUnpublishRequest) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeRcPic(publishUnpublishRequest);
//        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
//            @Override
//            public void success(final Response<BaseResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            BaseResponse AddImageResponse = response.body();
//                            mFragment.onDeleteRcSuccess(AddImageResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
//    }

//    public void removeInsurancePic(PublishUnpublishRequest publishUnpublishRequest) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeInsurancePic(publishUnpublishRequest);
//        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
//            @Override
//            public void success(final Response<BaseResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            BaseResponse AddImageResponse = response.body();
//                            mFragment.onDeleteInsuranceSuccess(AddImageResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
//    }

    public void getCarDetails(String carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GarageCarResponse> myCall = apiRequest.getGarageCarDetails("id",carId);
        myCall.enqueue(new ApiCallback.MyCallback<GarageCarResponse>() {
            @Override
            public void success(final Response<GarageCarResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessCarDetail(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }
}
