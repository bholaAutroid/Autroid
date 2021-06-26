package autroid.business.presenter.carsales;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.view.fragment.carsales.MyBookingsFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 11/21/17.
 */

public class MyBookingsPresenter {

    private MyBookingsFragment mFragment;
    private ViewGroup mMainLayout;

    public MyBookingsPresenter(MyBookingsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getBookings(int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall = apiRequest.getMyBookings(page);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(final Response<BookingsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingsResponse bookingsResponse = response.body();
                            mFragment.onSuccess(bookingsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.FALSE);
    }

    public void updateStatus(final BookingStatusRequest bookingStatusRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updateStatusMyBooking(bookingStatusRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mFragment.onStatusSuccess(baseResponse,bookingStatusRequest);
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
