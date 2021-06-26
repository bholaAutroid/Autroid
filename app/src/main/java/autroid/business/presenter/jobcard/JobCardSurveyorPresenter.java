package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.SurveyorRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.GetDriverResponse;
import autroid.business.view.fragment.jobcard.JobCardSurveyorFragment;
import retrofit2.Response;

public class JobCardSurveyorPresenter {

    private JobCardSurveyorFragment fragment;
    private ViewGroup viewGroup;

    public JobCardSurveyorPresenter(JobCardSurveyorFragment fragment,ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void getSurveyorDetails(String phone){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetDriverResponse> myCall=apiRequest.getDriver(phone,"contact_no","surveyor"); // driver and surveyor use same api
        myCall.enqueue(new ApiCallback.MyCallback<GetDriverResponse>() {
            @Override
            public void success(Response<GetDriverResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessSurveyor(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup, Boolean.TRUE);
    }

    public void setSurveyorDetails(SurveyorRequest surveyorRequest){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.setSurveyorDetails(surveyorRequest); // driver and surveyor use same api
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessSurveyorDetails(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

}
