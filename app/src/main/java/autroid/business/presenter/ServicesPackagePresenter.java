package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.VendorServicesResponse;
import autroid.business.view.fragment.BookingServicePackagesFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/25/17.
 */

public class ServicesPackagePresenter {

    private BookingServicePackagesFragment mFragment;
    private ViewGroup mMainLayout;

    public ServicesPackagePresenter(BookingServicePackagesFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getAllServices(String car,String tag) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<VendorServicesResponse> myCall = apiRequest.getEstimateNested(car,tag);
        myCall.enqueue(new ApiCallback.MyCallback<VendorServicesResponse>() {
            @Override
            public void success(final Response<VendorServicesResponse> response) {
                     mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            VendorServicesResponse vendorServicesResponse = response.body();
                            mFragment.onSuccess(vendorServicesResponse);
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
