package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.LeadUserRequest;
import autroid.business.model.response.EditLeadResponse;
import autroid.business.view.fragment.leads.LeadUserUpdate;
import retrofit2.Response;

public class LeadUserUpdatePresenter {

    LeadUserUpdate leadUserUpdate;

    ViewGroup viewGroup;

    public LeadUserUpdatePresenter(LeadUserUpdate fragment,ViewGroup viewGroup){
        this.leadUserUpdate=fragment;
        this.viewGroup=viewGroup;
    }

    public void updateUserDetails(LeadUserRequest leadUserRequest) {

        ApiRequest apiRequest = ApiFactory.createService(leadUserUpdate.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<EditLeadResponse> myCall = apiRequest.updateUser(leadUserRequest);
        myCall.enqueue(new ApiCallback.MyCallback<EditLeadResponse>() {
            @Override
            public void success(Response<EditLeadResponse> response) {
                leadUserUpdate.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        leadUserUpdate.onSuccessUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },leadUserUpdate.getActivity(),viewGroup,Boolean.TRUE);

    }

}
