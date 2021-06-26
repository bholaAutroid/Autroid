package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateRequest;
import autroid.business.model.response.GetParticularsResponse;
import autroid.business.view.fragment.jobcard.JobCardParticularsFragment;
import retrofit2.Response;

public class JobCardParticularsPresenter {

    JobCardParticularsFragment particularsFragment;
    ViewGroup mainLayout;

    public JobCardParticularsPresenter(JobCardParticularsFragment fragment,ViewGroup layout){
        particularsFragment=fragment;
        mainLayout=layout;
    }

    public void getParticulars(String bookingId){
        ApiRequest apiRequest= ApiFactory.createService(particularsFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetParticularsResponse> myCall=apiRequest.getCarParticulars(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<GetParticularsResponse>() {
            @Override
            public void success(Response<GetParticularsResponse> response) {
                particularsFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        particularsFragment.onSuccessResponse(response.body());
                    }
                });

            }

            @Override
            public void error(String errorMessage) {

            }
        },particularsFragment.getActivity(),mainLayout,Boolean.TRUE);
    }

    public void updateParticulars(UpdateRequest updateRequest){
        ApiRequest apiRequest= ApiFactory.createService(particularsFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetParticularsResponse> myCall=apiRequest.updateParticulars(updateRequest);
        myCall.enqueue(new ApiCallback.MyCallback<GetParticularsResponse>() {
            @Override
            public void success(Response<GetParticularsResponse> response) {
                particularsFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        particularsFragment.onSuccessUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },particularsFragment.getActivity(),mainLayout,Boolean.TRUE);
    }
}
