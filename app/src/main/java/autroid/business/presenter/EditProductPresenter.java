package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddProductRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddProductResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.ProductBrandResponse;
import autroid.business.view.activity.EditProductActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/27/17.
 */

public class EditProductPresenter {

    private EditProductActivity mActivity;
    private ViewGroup mMainLayout;

    public EditProductPresenter(EditProductActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public void removeProductPic(PublishUnpublishRequest publishUnpublishRequest, final String pos) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeProductPic(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
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
        }, mActivity, mMainLayout,Boolean.TRUE);
    }


    public void editProduct(final AddProductRequest addProductRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<AddProductResponse> myCall = apiRequest.editProductStock(addProductRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddProductResponse>() {
            @Override
            public void success(final Response<AddProductResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddProductResponse addProductResponse = response.body();
                            mActivity.onEditSuccess(addProductResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout,Boolean.TRUE);
    }

    public void getBrands(String type, final String brand) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<ProductBrandResponse> myCall = apiRequest.getBrands(type);
        myCall.enqueue(new ApiCallback.MyCallback<ProductBrandResponse>() {
            @Override
            public void success(final Response<ProductBrandResponse> response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductBrandResponse productBrandResponse = response.body();
                            mActivity.onBrandSuccess(productBrandResponse,brand);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity, mMainLayout,Boolean.TRUE);
    }
}
