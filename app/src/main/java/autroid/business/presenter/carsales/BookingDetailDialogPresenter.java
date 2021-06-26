package autroid.business.presenter.carsales;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.ServiceApproveRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingEstimateResponse;
import autroid.business.view.fragment.carsales.BookingDetailDialogFragment;
import retrofit2.Response;

public class BookingDetailDialogPresenter {

    BookingDetailDialogFragment fragment;
    ViewGroup viewGroup;

    public BookingDetailDialogPresenter(BookingDetailDialogFragment fragment, ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void getBookingDetails(String bookingId,String type){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BookingEstimateResponse> myCall=apiRequest.getBookingDetail(bookingId,type);
        myCall.enqueue(new ApiCallback.MyCallback<BookingEstimateResponse>() {
            @Override
            public void success(final Response<BookingEstimateResponse> response) {
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BookingEstimateResponse bookingRescheduleResponse = response.body();
                            fragment.onSuccessBookingDetails(bookingRescheduleResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void approveService(ServiceApproveRequest serviceApproveRequest){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.approveServices(serviceApproveRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            fragment.onSuccessApprove(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }
}
