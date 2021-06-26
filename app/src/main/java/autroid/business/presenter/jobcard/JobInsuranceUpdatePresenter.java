package autroid.business.presenter.jobcard;

import android.view.ViewGroup;
import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.InsuranceUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.GetCompanyResponse;
import autroid.business.view.fragment.jobcard.JobsInsuranceUpdate;
import retrofit2.Response;

public class JobInsuranceUpdatePresenter {

    JobsInsuranceUpdate jobsInsuranceUpdate;
    ViewGroup viewGroup;

    public JobInsuranceUpdatePresenter(JobsInsuranceUpdate jobsInsuranceUpdate,ViewGroup viewGroup){
        this.jobsInsuranceUpdate=jobsInsuranceUpdate;
        this.viewGroup=viewGroup;
    }

    public void updateInsuranceData(InsuranceUpdateRequest insuranceUpdateRequest) {
        ApiRequest apiRequest = ApiFactory.createService(jobsInsuranceUpdate.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updateInsuranceData(insuranceUpdateRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                jobsInsuranceUpdate.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        jobsInsuranceUpdate.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        }, jobsInsuranceUpdate.getActivity(), viewGroup, Boolean.TRUE);
    }

    public void getCompanyName() {
        ApiRequest apiRequest=ApiFactory.createService(jobsInsuranceUpdate.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetCompanyResponse> myCall=apiRequest.getCompanyName();
        myCall.enqueue(new ApiCallback.MyCallback<GetCompanyResponse>() {
            @Override
            public void success(Response<GetCompanyResponse> response) {
                jobsInsuranceUpdate.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        jobsInsuranceUpdate.onSuccessCompanyName(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
                //  mFragment.onError(errorMessage);
            }
        },jobsInsuranceUpdate.getActivity(),viewGroup,Boolean.TRUE);
    }

}
