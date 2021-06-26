package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateCustomerDetailsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.jobcard.JobCustomerUpdateFragment;
import retrofit2.Response;

public class JobCustomerUpdatePresenter {

    private JobCustomerUpdateFragment updateFragment;
    private ViewGroup mainLayout;

    public JobCustomerUpdatePresenter(JobCustomerUpdateFragment fragment, ViewGroup layout){
        updateFragment=fragment;
        mainLayout=layout;
    }

    public void updateDetails(UpdateCustomerDetailsRequest updateCustomerDetailsRequest){
        ApiRequest apiRequest= ApiFactory.createService(updateFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateCustomerDetails(updateCustomerDetailsRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                updateFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        updateFragment.onSuccess(response.body());
                    }
                });

            }

            @Override
            public void error(String errorMessage) {

            }
        },updateFragment.getActivity(),mainLayout,Boolean.TRUE);
    }
}
