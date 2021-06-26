package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.MemberRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.RolesResponse;
import autroid.business.view.fragment.AddTeamMember;
import retrofit2.Response;

public class TeamMemberPresenter {

    private AddTeamMember fragment;
    private ViewGroup mMainLayout;

    public TeamMemberPresenter(AddTeamMember frag, ViewGroup mainLayout) {
        fragment = frag;
        mMainLayout = mainLayout;
    }

    public void getRoles(){
        ApiRequest apiRequest = ApiFactory.createService(fragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<RolesResponse> myCall = apiRequest.getRoles();
        myCall.enqueue(new ApiCallback.MyCallback<RolesResponse>() {
            @Override
            public void success(final Response<RolesResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                        if (response.body().getResponseCode() == 200) {
                            fragment.onSuccessRoles(response.body());
                        }
                    });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, fragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void setTeamMember(MemberRequest memberRequest) {
        ApiRequest apiRequest = ApiFactory.createService(fragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.setMember(memberRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        fragment.onSuccessMemberAdded(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, fragment.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
