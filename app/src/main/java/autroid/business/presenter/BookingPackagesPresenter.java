package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BookingServicesResponse;
import autroid.business.view.fragment.BookingPackagesFragment;
import retrofit2.Response;

public class BookingPackagesPresenter {

    private BookingPackagesFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingPackagesPresenter(BookingPackagesFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void bookingPackages(String carId,String tag) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingServicesResponse> myCall = apiRequest.getEstimate(carId,tag);
        myCall.enqueue(new ApiCallback.MyCallback<BookingServicesResponse>() {
            @Override
            public void success(final Response<BookingServicesResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingServicesResponse bookingServicesResponse = response.body();
                            mFragment.onSuccess(bookingServicesResponse);
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
