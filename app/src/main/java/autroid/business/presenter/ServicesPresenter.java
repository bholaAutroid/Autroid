package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddServiceRequest;
import autroid.business.model.response.BaseResponse;

import autroid.business.view.fragment.ServicesLIstFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/25/17.
 */

public class ServicesPresenter {


    private ServicesLIstFragment mFragment;
    private ViewGroup mMainLayout;

    public ServicesPresenter(ServicesLIstFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


   /* public void getAllServices(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<VendorServicesResponse> myCall = apiRequest.getVendorServices(id);
        myCall.enqueue(new ApiCallback.MyCallback<VendorServicesResponse>() {
            @Override
            public void success(final Response<VendorServicesResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            VendorServicesResponse vendorServicesResponse = response.body();
                            mFragment.onSuccessGetUser(vendorServicesResponse);
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
    }*/

    public void addService(AddServiceRequest addServiceRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.addService(addServiceRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mFragment.onAddSuccess(baseResponse);
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
}
