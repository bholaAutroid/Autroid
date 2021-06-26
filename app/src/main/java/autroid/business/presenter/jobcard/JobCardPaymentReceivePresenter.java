package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.JobCardPaymentRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.PaymentModeResponse;
import autroid.business.view.fragment.jobcard.JobCardPaymentReceiveFragment;
import retrofit2.Response;

public class JobCardPaymentReceivePresenter {

    JobCardPaymentReceiveFragment particularsFragment;
    ViewGroup mainLayout;

    public JobCardPaymentReceivePresenter(JobCardPaymentReceiveFragment fragment,ViewGroup layout){
        particularsFragment=fragment;
        mainLayout=layout;
    }

    public void getMode(){
        ApiRequest apiRequest= ApiFactory.createService(particularsFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<PaymentModeResponse> myCall=apiRequest.getPayment();
        myCall.enqueue(new ApiCallback.MyCallback<PaymentModeResponse>() {
            @Override
            public void success(Response<PaymentModeResponse> response) {
                particularsFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        particularsFragment.onSuccesPayment(response.body());
                    }
                });

            }

            @Override
            public void error(String errorMessage) {

            }
        },particularsFragment.getActivity(),mainLayout,Boolean.TRUE);
    }

    public void receivePayment(JobCardPaymentRequest jobCardPaymentRequest){
        ApiRequest apiRequest= ApiFactory.createService(particularsFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.receivePayment(jobCardPaymentRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                particularsFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        particularsFragment.onSuccesPaymentReceive(response.body());
                    }
                });

            }

            @Override
            public void error(String errorMessage) {

            }
        },particularsFragment.getActivity(),mainLayout,Boolean.TRUE);
    }
}
