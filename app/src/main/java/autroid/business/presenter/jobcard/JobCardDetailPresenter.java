package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.request.BookingUpdateRequest;
import autroid.business.model.request.UpdateStatusRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.model.response.GetTechniciansResponse;
import autroid.business.model.response.PaymentLogResponse;
import autroid.business.view.fragment.jobcard.JobCardDetailFragment;
import retrofit2.Response;

public class JobCardDetailPresenter {

    JobCardDetailFragment fragment;
    ViewGroup viewGroup;

    public JobCardDetailPresenter(JobCardDetailFragment fragment,ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void getBookingDetails(String bookingId,String type){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BookingDetailsResponse> myCall=apiRequest.getBookingDetailsResponse(bookingId,type);
        myCall.enqueue(new ApiCallback.MyCallback<BookingDetailsResponse>() {
            @Override
            public void success(Response<BookingDetailsResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessBookingDetails(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void addBookingRemark(BookingUpdateRequest bookingUpdateRequest){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.addBookingRemark(bookingUpdateRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessBookingRemark(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void updateStatus(UpdateStatusRequest updateStatusRequest) {
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateJobsStatus(updateStatusRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessStatusUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void updateStatus(final BookingStatusRequest bookingStatusRequest) {
        ApiRequest apiRequest = ApiFactory.createService(fragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updateStatus(bookingStatusRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            fragment.onSuccessCancelJob(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, fragment.getActivity(), viewGroup, Boolean.TRUE);
    }

    public void getPaymentLog(String bookingId) {
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<PaymentLogResponse> myCall=apiRequest.getPaymentLog(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<PaymentLogResponse>() {
            @Override
            public void success(Response<PaymentLogResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessPaymentLogs(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void getTechnicians() {
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetTechniciansResponse> myCall=apiRequest.getTechnicians();
        myCall.enqueue(new ApiCallback.MyCallback<GetTechniciansResponse>() {
            @Override
            public void success(Response<GetTechniciansResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessTechnicians(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void getAdvisors() {
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<AdvisorResponse> myCall=apiRequest.getAdvisorsList();
        myCall.enqueue(new ApiCallback.MyCallback<AdvisorResponse>() {
            @Override
            public void success(Response<AdvisorResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessAdvisors(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }
}
