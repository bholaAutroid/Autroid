package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BookingsResponse;
import autroid.business.view.fragment.leads.LeadBookingFragment;
import retrofit2.Response;

public class LeadBookingPresenter {

    private LeadBookingFragment fragment;

    private ViewGroup viewGroup;


    public LeadBookingPresenter(LeadBookingFragment fragment, ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }


    public void getData(int page) {
        ApiRequest apiRequest = ApiFactory.createService(fragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BookingsResponse> myCall = apiRequest.getLeadBookings(page);
        myCall.enqueue(new ApiCallback.MyCallback<BookingsResponse>() {
            @Override
            public void success(Response<BookingsResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        fragment.onSucessLeadBooking(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);

    }

}
