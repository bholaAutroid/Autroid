package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ProductCategoryResponse;
import autroid.business.view.activity.addProduct.ProductCategoryActivity;
import autroid.business.view.activity.addProduct.ProductSubcategoryActivity;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductCategoryPresenter {

    private ProductCategoryActivity mActivity;
    private ProductSubcategoryActivity mActivitySubCategory;
    private ViewGroup mMainLayout;

    public ProductCategoryPresenter(ProductCategoryActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }

    public ProductCategoryPresenter(ProductSubcategoryActivity frag, ViewGroup mainLayout) {
        mActivitySubCategory = frag;
        mMainLayout = mainLayout;
    }

    public void getProductCategory() {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ProductCategoryResponse> myCall = apiRequest.getProductCategory();
        myCall.enqueue(new ApiCallback.MyCallback<ProductCategoryResponse>() {
            @Override
            public void success(final Response<ProductCategoryResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductCategoryResponse productCategoryResponse = response.body();
                            mActivity.onSuccess(productCategoryResponse);
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

    public void getProductSubCategory(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mActivitySubCategory.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ProductCategoryResponse> myCall = apiRequest.getProductSubCategory(id);
        myCall.enqueue(new ApiCallback.MyCallback<ProductCategoryResponse>() {
            @Override
            public void success(final Response<ProductCategoryResponse> response) {
                mActivitySubCategory.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ProductCategoryResponse productCategoryResponse = response.body();
                            mActivitySubCategory.onSuccess(productCategoryResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivitySubCategory.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
