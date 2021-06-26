package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.RequirementRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.GetBookedServicesResponse;
import autroid.business.model.response.GetDriverResponse;
import autroid.business.model.response.GetTechniciansResponse;
import autroid.business.view.fragment.jobcard.JobCardRequirementFragment;
import retrofit2.Response;

public class JobCardRequirementsPresenter {

    JobCardRequirementFragment requirementFragment;
    ViewGroup mainLayout;

    public JobCardRequirementsPresenter(JobCardRequirementFragment fragment,ViewGroup mainLayout){
        requirementFragment=fragment;
        this.mainLayout=mainLayout;
    }

    public void getTechnicians(){
        ApiRequest apiRequest= ApiFactory.createService(requirementFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetTechniciansResponse> myCall=apiRequest.getTechnicians();
        myCall.enqueue(new ApiCallback.MyCallback<GetTechniciansResponse>() {
            @Override
            public void success(Response<GetTechniciansResponse> response) {
                requirementFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        requirementFragment.onSuccessTechnician(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },requirementFragment.getActivity(),mainLayout,Boolean.TRUE);
    }

    public void getServices(String bookingId){
        ApiRequest apiRequest= ApiFactory.createService(requirementFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetBookedServicesResponse> myCall=apiRequest.getBookedServices(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<GetBookedServicesResponse>() {
            @Override
            public void success(Response<GetBookedServicesResponse> response) {
                requirementFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        requirementFragment.onSuccessServices(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },requirementFragment.getActivity(),mainLayout,Boolean.TRUE);
    }

    public void getDriver(String phone){
        ApiRequest apiRequest= ApiFactory.createService(requirementFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetDriverResponse> myCall=apiRequest.getDriver(phone,"contact_no","driver");
        myCall.enqueue(new ApiCallback.MyCallback<GetDriverResponse>() {
            @Override
            public void success(Response<GetDriverResponse> response) {
                requirementFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        requirementFragment.onSuccessDriver(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },requirementFragment.getActivity(),mainLayout,Boolean.TRUE);
    }

    public void putRequirements(RequirementRequest reqRequest) {
        ApiRequest apiRequest= ApiFactory.createService(requirementFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.putRequirements(reqRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                requirementFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200)requirementFragment.onSuccess();
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },requirementFragment.getActivity(),mainLayout,Boolean.TRUE);

    }
}
