package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.UpdateAssetsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarInspectionResponse;
import autroid.business.view.fragment.jobcard.JobsCarDetails;
import retrofit2.Response;

public class JobCardImagesPresenter {

    private JobsCarDetails fragment;
    private ViewGroup viewGroup;

    public JobCardImagesPresenter(JobsCarDetails fragment, ViewGroup viewGroup){
        this.fragment=fragment;
        this.viewGroup=viewGroup;
    }

    public void getCarImages(String bookingId){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<CarInspectionResponse> myCall=apiRequest.getInspectionImages(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<CarInspectionResponse>() {
            @Override
            public void success(Response<CarInspectionResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        fragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

    public void updateAssets(UpdateAssetsRequest assetsRequest) {
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall=apiRequest.updateAssets(assetsRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(Response<BaseResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode()==200){
                        fragment.onSuccessAssetsUpdate(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }
}
