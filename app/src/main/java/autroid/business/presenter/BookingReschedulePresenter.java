package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingRescheduleRequest;
import autroid.business.model.request.GetTimeSlotRequest;
import autroid.business.model.response.BookingRescheduleResponse;
import autroid.business.model.response.BookingSlotResponse;
import autroid.business.view.fragment.booking.BookingRescheduleFragment;
import retrofit2.Response;

public class BookingReschedulePresenter {

    private BookingRescheduleFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingReschedulePresenter(BookingRescheduleFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void reScheduleBookings(BookingRescheduleRequest bookingRescheduleRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingRescheduleResponse> myCall = apiRequest.bookingReschedule(bookingRescheduleRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingRescheduleResponse>() {
            @Override
            public void success(final Response<BookingRescheduleResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingRescheduleResponse bookingRescheduleResponse = response.body();
                            mFragment.onSuccess(bookingRescheduleResponse);
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

    public void reScheduleMyBookings(BookingRescheduleRequest bookingRescheduleRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingRescheduleResponse> myCall = apiRequest.myBookingReschedule(bookingRescheduleRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingRescheduleResponse>() {
            @Override
            public void success(final Response<BookingRescheduleResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingRescheduleResponse bookingRescheduleResponse = response.body();
                            mFragment.onSuccess(bookingRescheduleResponse);
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

    public void getSlots(String id,String date) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingSlotResponse> myCall = apiRequest.getSlots(id,date);
        myCall.enqueue(new ApiCallback.MyCallback<BookingSlotResponse>() {
            @Override
            public void success(final Response<BookingSlotResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingSlotResponse baseResponse = response.body();
                            mFragment.onSlotsSuccess(baseResponse);
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

    public void getMySlots(GetTimeSlotRequest getTimeSlotRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingSlotResponse> myCall = apiRequest.getMySlots(getTimeSlotRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingSlotResponse>() {
            @Override
            public void success(final Response<BookingSlotResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingSlotResponse baseResponse = response.body();
                            mFragment.onSlotsSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },  mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
