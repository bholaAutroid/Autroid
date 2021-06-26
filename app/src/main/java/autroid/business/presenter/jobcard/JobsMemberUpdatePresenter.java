package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateMemberRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.jobcard.JobsMemberUpdate;
import retrofit2.Response;

public class JobsMemberUpdatePresenter{

    private JobsMemberUpdate mFragment;
    private ViewGroup mMainLayout;

    public JobsMemberUpdatePresenter(JobsMemberUpdate memberUpdate,ViewGroup mainLayout ){
        mFragment=memberUpdate;
        mMainLayout=mainLayout;
    }


    public void updateTechnician(UpdateMemberRequest updateMemberRequest){
        ApiRequest apiRequest= ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateTechnician(updateMemberRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }

    public void updateAdvisor(UpdateMemberRequest updateMemberRequest){
        ApiRequest apiRequest= ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateAdvisor(updateMemberRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }
}