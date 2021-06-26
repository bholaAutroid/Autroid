package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PointsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.JobsQCResponse;
import autroid.business.view.fragment.jobcard.JobsQualityCheckFragment;
import retrofit2.Response;

public class JobsQualityCheckPresenter {
    private JobsQualityCheckFragment mFragment;
    private ViewGroup mMainLayout;

    public JobsQualityCheckPresenter(JobsQualityCheckFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getQC(String bookingId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<JobsQCResponse> myCall = apiRequest.getQCquestions(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<JobsQCResponse>() {
            @Override
            public void success(final Response<JobsQCResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                        if (response.body().getResponseCode() == 200) {
                            JobsQCResponse baseResponse = response.body();
                              mFragment.onSuccess(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void setPoints(PointsRequest pointsRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.putQCquestions(pointsRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessRework(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }
}
