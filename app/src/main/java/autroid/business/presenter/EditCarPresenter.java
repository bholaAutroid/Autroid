package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.model.response.CarModelsResponse;
import autroid.business.model.response.CarStockResponse;
import autroid.business.model.response.CarVariantResponse;
import autroid.business.view.activity.editCar.EditCarActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public class EditCarPresenter {

    private EditCarActivity mActivity;
    private ViewGroup mMainLayout;

    public EditCarPresenter(EditCarActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void getCarItems() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarItemsResponse> myCall = apiRequest.getCarItems();
        myCall.enqueue(new ApiCallback.MyCallback<CarItemsResponse>() {
            @Override
            public void success(final Response<CarItemsResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarItemsResponse carItemsResponse = response.body();
                          //  mActivity.onItemSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void getCarDetails(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarStockResponse> myCall = apiRequest.getCarDetails(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarStockResponse>() {
            @Override
            public void success(final Response<CarStockResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarStockResponse carItemsResponse = response.body();
                           // mActivity.onDetailSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void getCarModels(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarModelsResponse> myCall = apiRequest.getMakersList(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarModelsResponse>() {
            @Override
            public void success(final Response<CarModelsResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarModelsResponse carItemsResponse = response.body();
                          //  mActivity.onModelSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }


    public void getCarVariant(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarVariantResponse> myCall = apiRequest.getVariantList(id);
        myCall.enqueue(new ApiCallback.MyCallback<CarVariantResponse>() {
            @Override
            public void success(final Response<CarVariantResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            CarVariantResponse carItemsResponse = response.body();
                          //  mActivity.onVariantSuccess(carItemsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }


    public void removeCarPic(PublishUnpublishRequest publishUnpublishRequest,final String pos) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeCarPic(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onDeleteSuccess(baseResponse,pos);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }


    public void editCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddCarResponse> myCall = apiRequest.editCarStock(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddCarResponse>() {
            @Override
            public void success(final Response<AddCarResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddCarResponse addCarResponse = response.body();
                            mActivity.onEditSuccess(addCarResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
