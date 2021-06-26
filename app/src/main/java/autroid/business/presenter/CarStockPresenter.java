package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.CarListResponse;
import autroid.business.view.fragment.CarStockFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/10/17.
 */

public class CarStockPresenter {

    private CarStockFragment mFragment;
    private ViewGroup mMainLayout;

    public CarStockPresenter(CarStockFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getCarStock(int page, final Boolean isLoading) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarListResponse> myCall = apiRequest.getCarStock(page);
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
        }, mFragment.getActivity(), mMainLayout,isLoading);
    }



//    public void publishCar(PublishUnpublishRequest publishUnpublishRequest,final String id) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<PublishResponse> myCall = apiRequest.putPublishCarRequest(publishUnpublishRequest);
//        myCall.enqueue(new ApiCallback.MyCallback<PublishResponse>() {
//            @Override
//            public void success(final Response<PublishResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            PublishResponse baseResponse = response.body();
//                            mFragment.publishUnpublishResponse(baseResponse,id);
//                        } else {
//
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
//    }
}
