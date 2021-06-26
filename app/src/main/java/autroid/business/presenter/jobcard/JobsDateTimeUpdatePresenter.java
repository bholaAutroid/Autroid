package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateDeliveryDetailsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.jobcard.JobsDateTimeUpdate;
import retrofit2.Response;

public class JobsDateTimeUpdatePresenter {

    private JobsDateTimeUpdate mFragment;
    private ViewGroup mMainLayout;

    public JobsDateTimeUpdatePresenter(JobsDateTimeUpdate dateTimeUpdate,ViewGroup mainLayout ){
        mFragment=dateTimeUpdate;
        mMainLayout=mainLayout;
    }

    public void updateDeliveryDetails(UpdateDeliveryDetailsRequest updateDeliveryDetailsRequest){
        ApiRequest apiRequest= ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateDeliveryDetails(updateDeliveryDetailsRequest);
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
