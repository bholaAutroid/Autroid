package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddOwnerRequest;
import autroid.business.model.response.AddOwnerResponse;
import autroid.business.model.response.GetUserResponse;
import autroid.business.view.fragment.jobcard.JobCardUserFragment;
import retrofit2.Response;

public class JobCardUserPresenter{

    private JobCardUserFragment mFragment;
    private ViewGroup mMainLayout;

    public JobCardUserPresenter(JobCardUserFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void addOwner(AddOwnerRequest addOwnerRequest){
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<AddOwnerResponse> myCall=apiRequest.addOwner(addOwnerRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddOwnerResponse>() {
            @Override
            public void success(Response<AddOwnerResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.OnSuccessAddOwner(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }

    public void getUser(String mobile) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserResponse> myCall = apiRequest.verifyUser(mobile, "contact_no");
        myCall.enqueue(new ApiCallback.MyCallback<GetUserResponse>() {
            @Override
            public void success(Response<GetUserResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseData().size()>0) {
                        GetUserResponse getUserResponse = response.body();
                        mFragment.onSuccessGetUser(getUserResponse);
                    }else if(response.body().getResponseData().size()==0){
                        mFragment.notFound();
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}