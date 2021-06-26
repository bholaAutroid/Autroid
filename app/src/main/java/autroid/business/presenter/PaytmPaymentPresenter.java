package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ChecksumResponse;
import autroid.business.model.response.PaymentRecheckResponse;
import autroid.business.view.fragment.payment.PaytmPaymentFragment;
import retrofit2.Response;

public class PaytmPaymentPresenter {

    private PaytmPaymentFragment mFragment;
    private ViewGroup mMainLayout;

    public PaytmPaymentPresenter(PaytmPaymentFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getChecksum(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ChecksumResponse> myCall = apiRequest.getCheckSum(id);
        myCall.enqueue(new ApiCallback.MyCallback<ChecksumResponse>() {
            @Override
            public void success(final Response<ChecksumResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ChecksumResponse checksumResponse = response.body();
                            mFragment.onChecksumSuccess(checksumResponse);
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


    public void paymentRecheck(String orderId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PaymentRecheckResponse> myCall = apiRequest.paymentRecheck(orderId);
        myCall.enqueue(new ApiCallback.MyCallback<PaymentRecheckResponse>() {
            @Override
            public void success(final Response<PaymentRecheckResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            PaymentRecheckResponse paymentRecheckResponse = response.body();
                            mFragment.onRecheckSuccess(paymentRecheckResponse);
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
