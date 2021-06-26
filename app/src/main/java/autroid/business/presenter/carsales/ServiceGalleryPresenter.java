package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ServiceGalleryResponse;
import autroid.business.view.fragment.carsales.ServiceGalleryFragment;
import retrofit2.Response;

public class ServiceGalleryPresenter {
    private ServiceGalleryFragment mFragment;
    private ViewGroup mMainLayout;

    public ServiceGalleryPresenter(ServiceGalleryFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getServiceGallery(String id,String type) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ServiceGalleryResponse> myCall = apiRequest.getVendorServicesGallery(id,type);
        myCall.enqueue(new ApiCallback.MyCallback<ServiceGalleryResponse>() {
            @Override
            public void success(final Response<ServiceGalleryResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ServiceGalleryResponse vendorServicesResponse = response.body();
                           // mFragment.onSuccess(vendorServicesResponse);
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
