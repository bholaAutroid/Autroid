package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.CreateLeadRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.CategoryResponse;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.view.fragment.leads.LeadCreateFragment;
import retrofit2.Response;

public class LeadCreatePresenter {

    private LeadCreateFragment mFragment;
    private ViewGroup mMainLayout;

    public LeadCreatePresenter(LeadCreateFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void createLead(CreateLeadRequest createLeadRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CreateLeadResponse> myCall = apiRequest.createLead(createLeadRequest);
        myCall.enqueue(new ApiCallback.MyCallback<CreateLeadResponse>() {
            @Override
            public void success(final Response<CreateLeadResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    mFragment.onSuccess(response.body());
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

//    public void getLeadsStatus() {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<LeadStatusResponse> myCall = apiRequest.getAllLeadStatus();
//        myCall.enqueue(new ApiCallback.MyCallback<LeadStatusResponse>() {
//            @Override
//            public void success(final Response<LeadStatusResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            LeadStatusResponse leadStatusResponse = response.body();
//                            //mFragment.onStatusSuccess(leadStatusResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//            }
//        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
//    }

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

    public void getLeadCategory() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CategoryResponse> myCall = apiRequest.getLeadCategory();
        myCall.enqueue(new ApiCallback.MyCallback<CategoryResponse>() {
            @Override
            public void success(final Response<CategoryResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        mFragment.onSuccessCategory(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
