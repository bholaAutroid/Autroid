package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.LeadAdvisorRequest;
import autroid.business.model.request.LeadPriorityRequest;
import autroid.business.model.request.LeadUpdateRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.LeadDetailsResponse;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.model.response.LeadStatusResponse;
import autroid.business.view.fragment.leads.LeadDetailFragment;
import retrofit2.Response;

public class LeadDetailPresenter {

    private LeadDetailFragment mFragment;
    private ViewGroup mMainLayout;

    public LeadDetailPresenter(LeadDetailFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getLeadsStatus(String stage) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<LeadStatusResponse> myCall = apiRequest.getAllLeadStatus(stage);
        myCall.enqueue(new ApiCallback.MyCallback<LeadStatusResponse>() {
            @Override
            public void success(final Response<LeadStatusResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        LeadStatusResponse leadStatusResponse = response.body();
                        mFragment.onStatusSuccess(leadStatusResponse);
                    } else {
                        //App.getInstance().showErroroBar(response.body().getResponseMessage());
                    }

                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getAdvisor() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AdvisorResponse> myCall = apiRequest.getAdvisorsList();
        myCall.enqueue(new ApiCallback.MyCallback<AdvisorResponse>() {
            @Override
            public void success(final Response<AdvisorResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        mFragment.onSuccessAdvisors(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getLeadDetail(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<LeadDetailsResponse> myCall = apiRequest.getLeadDetail(id);
        myCall.enqueue(new ApiCallback.MyCallback<LeadDetailsResponse>() {
            @Override
            public void success(final Response<LeadDetailsResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                   if(response.body().getResponseCode()==200){
                       mFragment.onSuccessDetails(response.body());
                   }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void updateLeadsStatus(LeadUpdateRequest leadUpdateRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CreateLeadResponse> myCall = apiRequest.addRemark(leadUpdateRequest);
        myCall.enqueue(new ApiCallback.MyCallback<CreateLeadResponse>() {
            @Override
            public void success(final Response<CreateLeadResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                        if (response.body().getResponseCode() == 200) {
                            CreateLeadResponse baseResponse = response.body();
                            mFragment.onUpdateStatusSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void updatePriority(LeadPriorityRequest leadPriorityRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.updatePriority(leadPriorityRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessPriorityUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);

    }

    public void addAdvisor(LeadAdvisorRequest leadAdvisorRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.addAdvisor(leadAdvisorRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessAddAdvisor(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);

    }

}
