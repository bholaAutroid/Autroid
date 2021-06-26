package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.request.BookingUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.view.fragment.search.MainSearchBookingDetailsFragment;
import retrofit2.Response;

public class BookingDetailsPresenter {

    MainSearchBookingDetailsFragment fragment;
    ViewGroup viewGroup;

    public BookingDetailsPresenter(MainSearchBookingDetailsFragment fragment,ViewGroup viewGroup){
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
                            fragment.onSuccessStatusUpdate(baseResponse,bookingStatusRequest);
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

}
