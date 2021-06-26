package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddProductRequest;
import autroid.business.model.response.AddProductResponse;
import autroid.business.model.response.ProductBrandResponse;
import autroid.business.view.activity.addProduct.AddProductActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class AddProductPresenter {

    private AddProductActivity mActivity;

    private ViewGroup mMainLayout;

    public AddProductPresenter(AddProductActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }



    public void addProduct(AddProductRequest addProductRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddProductResponse> myCall = apiRequest.postProduct(addProductRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddProductResponse>() {
            @Override
            public void success(final Response<AddProductResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddProductResponse addProductResponse = response.body();
                            mActivity.onSuccess(addProductResponse);
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

    public void getBrands(String type) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ProductBrandResponse> myCall = apiRequest.getBrands(type);
        myCall.enqueue(new ApiCallback.MyCallback<ProductBrandResponse>() {
            @Override
            public void success(final Response<ProductBrandResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductBrandResponse productBrandResponse = response.body();
                            mActivity.onBrandSuccess(productBrandResponse);
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

    public void getTyre(String type) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ProductBrandResponse> myCall = apiRequest.getTyreSpecification(type);
        myCall.enqueue(new ApiCallback.MyCallback<ProductBrandResponse>() {
            @Override
            public void success(final Response<ProductBrandResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductBrandResponse baseResponse = response.body();
                            mActivity.tyreResponse(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.FALSE);
    }


}
