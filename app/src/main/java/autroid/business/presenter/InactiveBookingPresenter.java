package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BookingsResponse;
import autroid.business.view.fragment.leads.InactiveBookingFragment;
import retrofit2.Response;

public class InactiveBookingPresenter {

    private InactiveBookingFragment mFragment;
    private ViewGroup mMainLayout;

    public InactiveBookingPresenter(InactiveBookingFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getBookings(final String status,int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall = apiRequest.getBookings(status,page,"");
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(final Response<BookingsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingsResponse bookingsResponse = response.body();
                            mFragment.onSuccess(bookingsResponse,status);
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
