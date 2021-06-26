package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.GetTimeSlotRequest;
import autroid.business.model.response.AddBookingResponse;
import autroid.business.model.response.BookingConvenienceResponse;
import autroid.business.model.response.BookingSlotResponse;
import autroid.business.view.fragment.booking.BookingScheduleFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 11/16/17.
 */

public class AddBookingPresenter {

    private BookingScheduleFragment mFragment;
    private ViewGroup mMainLayout;

    public AddBookingPresenter(BookingScheduleFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void addCarBooking(AddBookingRequest addBookingRequest,Boolean isCheckout) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingResponse> myCall = apiRequest.addCarBooking(addBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingResponse>() {
            @Override
            public void success(final Response<AddBookingResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddBookingResponse loginPostResponse = response.body();
                            mFragment.onBookingSuccess(loginPostResponse,isCheckout);
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

    public void addBooking(AddBookingRequest addBookingRequest,Boolean isCheckout) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingResponse> myCall = apiRequest.scheduleBooking(addBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingResponse>() {
            @Override
            public void success(final Response<AddBookingResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddBookingResponse loginPostResponse = response.body();
                            mFragment.onBookingSuccess(loginPostResponse,isCheckout);
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
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getConvenience(String bookingId,String businessId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingConvenienceResponse> myCall = apiRequest.getConvenience(bookingId,businessId);
        myCall.enqueue(new ApiCallback.MyCallback<BookingConvenienceResponse>() {
            @Override
            public void success(final Response<BookingConvenienceResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingConvenienceResponse baseResponse = response.body();
                            mFragment.onConvenienceSuccess(baseResponse);
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

    public void getBookingConvenience(String businessId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingConvenienceResponse> myCall = apiRequest.getBookingConvenience(businessId);
        myCall.enqueue(new ApiCallback.MyCallback<BookingConvenienceResponse>() {
            @Override
            public void success(final Response<BookingConvenienceResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingConvenienceResponse baseResponse = response.body();
                            mFragment.onConvenienceSuccess(baseResponse);
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
