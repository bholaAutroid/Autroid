package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.RequirementRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.jobcard.JobCardCollisionFragment;
import retrofit2.Response;

public class JobCardCollisionPresenter {

    JobCardCollisionFragment requirementFragment;
    ViewGroup mainLayout;

    public JobCardCollisionPresenter(JobCardCollisionFragment fragment,ViewGroup mainLayout){
        requirementFragment=fragment;
        this.mainLayout=mainLayout;
    }

    public void addClaim(RequirementRequest reqRequest) {
        ApiRequest apiRequest= ApiFactory.createService(requirementFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.addClaim(reqRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                requirementFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        requirementFragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },requirementFragment.getActivity(),mainLayout,Boolean.TRUE);

    }
}
