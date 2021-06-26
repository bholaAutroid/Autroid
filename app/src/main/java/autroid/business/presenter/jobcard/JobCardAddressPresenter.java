package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddressRequest;
import autroid.business.model.request.UpdateMemberRequest;
import autroid.business.model.response.AddressResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingAddressResponse;
import autroid.business.model.response.PinResponse;
import autroid.business.view.fragment.jobcard.JobCardAddressFragment;
import retrofit2.Response;

public class JobCardAddressPresenter {

    JobCardAddressFragment addressFragment;
    ViewGroup viewGroup;

    public JobCardAddressPresenter(JobCardAddressFragment addressFragment, ViewGroup viewGroup){
        this.addressFragment=addressFragment;
        this.viewGroup=viewGroup;
    }

    public void getAddress(String bookingId){
        ApiRequest apiRequest= ApiFactory.createService(addressFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BookingAddressResponse> myCall=apiRequest.getBookingAddress(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<BookingAddressResponse>() {
            @Override
            public void success(Response<BookingAddressResponse> response) {
                addressFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200) addressFragment.onSuccessAddressResponse(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },addressFragment.getActivity(),viewGroup,Boolean.TRUE);

    }

    public void getPinData(String zip) {
        ApiRequest apiRequest= ApiFactory.createService(addressFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<PinResponse> myCall=apiRequest.getPinData(zip);
        myCall.enqueue(new ApiCallback.MyCallback<PinResponse>() {
            @Override
            public void success(Response<PinResponse> response) {
                addressFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200) addressFragment.onSuccessPinResponse(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },addressFragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void setAddress(AddressRequest addressRequest) {
        ApiRequest apiRequest= ApiFactory.createService(addressFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<AddressResponse> myCall=apiRequest.addAddress(addressRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddressResponse>() {
            @Override
            public void success(Response<AddressResponse> response) {
                addressFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        addressFragment.onSuccessAddAddress(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },addressFragment.getActivity(),viewGroup, Boolean.TRUE);
    }

    public void setSelectedAddress(UpdateMemberRequest request) {
        ApiRequest apiRequest= ApiFactory.createService(addressFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.addSelectedAddress(request);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                addressFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        addressFragment.onSuccessAddressUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },addressFragment.getActivity(),viewGroup, Boolean.TRUE);
    }
}
