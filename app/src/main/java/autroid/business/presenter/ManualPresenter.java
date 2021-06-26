package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.JobCardBookingRequest;
import autroid.business.model.request.LeadBookingRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.model.response.LeadCarsResponse;
import autroid.business.view.fragment.booking.ManualBookingFragment;
import retrofit2.Response;

public class ManualPresenter {

    ManualBookingFragment manualBookingFragment;
    ViewGroup viewGroup;

    public ManualPresenter(ManualBookingFragment manualBookingFragment, ViewGroup viewGroup) {
        this.manualBookingFragment = manualBookingFragment;
        this.viewGroup = viewGroup;
    }

    public void getUserCars(String userId) {
        ApiRequest apiRequest = ApiFactory.createService(manualBookingFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserCarResponse> myCall = apiRequest.getUserCar(userId);
        myCall.enqueue(new ApiCallback.MyCallback<GetUserCarResponse>() {
            @Override
            public void success(Response<GetUserCarResponse> response) {
                manualBookingFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200 && response.body().getGetCarDetails().size() != 0) {
                        manualBookingFragment.onSuccessCarData(response.body());
                    } else if (response.body().getResponseCode() == 200 && response.body().getGetCarDetails().size() == 0) {
                        manualBookingFragment.onSuccessEmptyCarData();
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}

        }, manualBookingFragment.getActivity(), viewGroup, Boolean.TRUE);
    }

    public void addManualBooking(JobCardBookingRequest jobCardBookingRequest){
        ApiRequest apiRequest=ApiFactory.createService(manualBookingFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall=apiRequest.addManualBooking(jobCardBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(Response<BookingsResponse> response) {
                manualBookingFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        manualBookingFragment.onSuccessAddBooking(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },manualBookingFragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void leadBookingAdd(LeadBookingRequest leadBookingRequest){
        ApiRequest apiRequest=ApiFactory.createService(manualBookingFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall=apiRequest.addLeadBooking(leadBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(Response<BookingsResponse> response) {
                manualBookingFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        manualBookingFragment.onSuccessAddLeadBooking(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },manualBookingFragment.getActivity(),viewGroup,Boolean.TRUE);
    }


    public void getCarsByLead(String leadId) {
        ApiRequest apiRequest = ApiFactory.createService(manualBookingFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<LeadCarsResponse> myCall = apiRequest.getCarsLead(leadId);
        myCall.enqueue(new ApiCallback.MyCallback<LeadCarsResponse>() {
            @Override
            public void success(Response<LeadCarsResponse> response) {
                manualBookingFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200 && response.body().getCars().size() != 0) {
                        manualBookingFragment.onSuccessLeadCarData(response.body());
                    } else if (response.body().getResponseCode() == 200 && response.body().getCars().size() == 0) {
                        manualBookingFragment.onSuccessEmptyLeadCarData();
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}

        }, manualBookingFragment.getActivity(), viewGroup, Boolean.TRUE);
    }

    public void getAdvisor() {
        ApiRequest apiRequest = ApiFactory.createService(manualBookingFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AdvisorResponse> myCall = apiRequest.getAdvisorsList();
        myCall.enqueue(new ApiCallback.MyCallback<AdvisorResponse>() {
            @Override
            public void success(final Response<AdvisorResponse> response) {
                manualBookingFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        manualBookingFragment.onSuccessAdvisors(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, manualBookingFragment.getActivity(), viewGroup, Boolean.TRUE);
    }


}
