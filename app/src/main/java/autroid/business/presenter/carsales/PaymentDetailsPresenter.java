package autroid.business.presenter.carsales;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.PaymentLogResponse;
import autroid.business.view.fragment.carsales.PaymentDetailsFragment;
import retrofit2.Response;

public class PaymentDetailsPresenter {

    private PaymentDetailsFragment mFragment;
    private ViewGroup mMainLayout;

    public PaymentDetailsPresenter(PaymentDetailsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getPaymentLog(String bookingId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PaymentLogResponse> myCall = apiRequest.getPaymentLog(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<PaymentLogResponse>() {
            @Override
            public void success(final Response<PaymentLogResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                        if (response.body().getResponseCode() == 200) {
                            mFragment.onPaymentLogSuccess(response.body());
                        }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

}
