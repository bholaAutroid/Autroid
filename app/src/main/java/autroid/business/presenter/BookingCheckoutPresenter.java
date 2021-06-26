package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PaymentDetailRequest;
import autroid.business.model.response.AddBookingResponse;
import autroid.business.model.response.PaymentDetailResponse;
import autroid.business.view.fragment.carsales.BookingCheckoutFragment;
import retrofit2.Response;

public class BookingCheckoutPresenter {

    private BookingCheckoutFragment mFragment;
    private ViewGroup mMainLayout;

    public BookingCheckoutPresenter(BookingCheckoutFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getDiscountApply(PaymentDetailRequest paymentDetailRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PaymentDetailResponse> myCall = apiRequest.getDiscountDetail(paymentDetailRequest);
        myCall.enqueue(new ApiCallback.MyCallback<PaymentDetailResponse>() {
            @Override
            public void success(final Response<PaymentDetailResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            PaymentDetailResponse paymentDetailResponse = response.body();
                            mFragment.onSuccess(paymentDetailResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(final String errorMessage) {

                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      //  mFragment.onError(errorMessage);

                    }
                });

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }


    public void bookingPayLater(PaymentDetailRequest paymentDetailRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddBookingResponse> myCall = apiRequest.bookingPayLater(paymentDetailRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddBookingResponse>() {
            @Override
            public void success(final Response<AddBookingResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddBookingResponse baseResponse = response.body();
                            mFragment.onBookingSuccess(baseResponse);
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
