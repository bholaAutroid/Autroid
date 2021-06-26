package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingAddressRequest;
import autroid.business.model.response.AddBookingAddressResponse;
import autroid.business.model.response.BookingConvenienceResponse;
import autroid.business.view.fragment.booking.BookingAddressFragment;
import retrofit2.Response;

public class BookingAddressPresenter {

    private BookingAddressFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingAddressPresenter(BookingAddressFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
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
                            mFragment.onAddressSuccess(baseResponse);
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

    public void getBookingAddress(String bookingId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingConvenienceResponse> myCall = apiRequest.getConvenience(bookingId,"");
        myCall.enqueue(new ApiCallback.MyCallback<BookingConvenienceResponse>() {
            @Override
            public void success(final Response<BookingConvenienceResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingConvenienceResponse baseResponse = response.body();
                            mFragment.onAddressSuccess(baseResponse);
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

    public void addBookingAddress(BookingAddressRequest bookingAddressRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingAddressResponse> myCall = apiRequest.addBookingAddress(bookingAddressRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingAddressResponse>() {
            @Override
            public void success(final Response<AddBookingAddressResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddBookingAddressResponse addBookingAddressResponse = response.body();
                            mFragment.onAddAddressSuccess(addBookingAddressResponse);
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

    public void getBookingCity(String zip) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingAddressResponse> myCall = apiRequest.getBookingAddressCity(zip);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingAddressResponse>() {
            @Override
            public void success(final Response<AddBookingAddressResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddBookingAddressResponse addBookingAddressResponse = response.body();
                            mFragment.onCitySuccess(addBookingAddressResponse);
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
