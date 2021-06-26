package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.InsuranceAccidentUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.jobcard.JobsAccidentDetailsUpdate;
import retrofit2.Response;

public class JobAccidentUpdateDetailsPresenter {

    JobsAccidentDetailsUpdate fragment;
    ViewGroup viewGroup;

    public JobAccidentUpdateDetailsPresenter(JobsAccidentDetailsUpdate fragment, ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void updateAccidentDetails(InsuranceAccidentUpdateRequest request){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateAccidentDetails(request);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200) fragment.onSuccess(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);

    }

}
