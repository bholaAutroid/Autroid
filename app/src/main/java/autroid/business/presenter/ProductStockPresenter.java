package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.ProductStockResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.view.fragment.ProductStockFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductStockPresenter {

    private ProductStockFragment mFragment;
    private ViewGroup mMainLayout;

    public ProductStockPresenter(ProductStockFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getProductStock(int page, final Boolean isLoading) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ProductStockResponse> myCall = apiRequest.getProductStock(page);
        myCall.enqueue(new ApiCallback.MyCallback<ProductStockResponse>() {
            @Override
            public void success(final Response<ProductStockResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductStockResponse productStockResponse = response.body();
                            mFragment.onSuccess(productStockResponse,isLoading);
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



    public void publishCar(PublishUnpublishRequest publishUnpublishRequest, final String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PublishResponse> myCall = apiRequest.putPublishProductRequest(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<PublishResponse>() {
            @Override
            public void success(final Response<PublishResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            PublishResponse baseResponse = response.body();
                            mFragment.publishUnpublishResponse(baseResponse,id);
                        } else {

                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }
}
