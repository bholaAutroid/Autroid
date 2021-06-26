package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.view.fragment.booking.BookingsFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 11/21/17.
 */

public class BookingsPresenter {

    private BookingsFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingsPresenter(BookingsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getBookings(final String stage, final int page,String sorttBy) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall = apiRequest.getBookings(stage,page,sorttBy);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(final Response<BookingsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingsResponse bookingsResponse = response.body();
                            mFragment.onSuccess(bookingsResponse,/*,stage,*/ page);
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



    public void getBookingsByBumber(final String number) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall = apiRequest.getBookingsByNumber(number);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(final Response<BookingsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingsResponse bookingsResponse = response.body();
                            mFragment.onSuccessNumber(bookingsResponse);
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

    public void updateStatus(final BookingStatusRequest bookingStatusRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updateStatus(bookingStatusRequest);
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
