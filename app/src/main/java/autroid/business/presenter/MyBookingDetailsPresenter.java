package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.model.response.CarInspectionResponse;
import autroid.business.utils.Constant;
import autroid.business.view.fragment.carsales.MyBookingDetailFragment;

import retrofit2.Response;

public class MyBookingDetailsPresenter {

    MyBookingDetailFragment fragment;
    ViewGroup viewGroup;

    public MyBookingDetailsPresenter(MyBookingDetailFragment fragment, ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void getBookingDetails(String bookingId,String id){
            ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);

            ApiCallback.MyCall<BookingDetailsResponse> myCall=apiRequest.getBookingDetailsResponse(bookingId, Constant.KEY_ID);
            myCall.enqueue(new ApiCallback.MyCallback<BookingDetailsResponse>() {
                @Override
                public void success(final Response<BookingDetailsResponse> response) {
                    fragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body().getResponseCode() == 200) {
                                BookingDetailsResponse jobResponse = response.body();
                                fragment.onSuccess(jobResponse);
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


    public void getCarImages(String bookingId){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<CarInspectionResponse> myCall=apiRequest.getInspectionImages(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<CarInspectionResponse>() {
            @Override
            public void success(Response<CarInspectionResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        fragment.onImagesSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

}
