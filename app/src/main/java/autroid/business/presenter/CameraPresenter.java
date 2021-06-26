package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.camera.CameraFragment;
import autroid.business.model.response.InspectionImageResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CameraPresenter {

    private CameraFragment mFragment;
    private ViewGroup mMainLayout;

    public CameraPresenter(CameraFragment myFragment, ViewGroup mainLayout) {
        mFragment = myFragment;
        mMainLayout = mainLayout;
    }

    public void addInspectionImages(RequestBody index, RequestBody bookingId, MultipartBody.Part imageFile) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<InspectionImageResponse> myCall = apiRequest.addInspectionImages(index, bookingId, imageFile);
        myCall.enqueue(new ApiCallback.MyCallback<InspectionImageResponse>() {
            @Override
            public void success(Response<InspectionImageResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccess(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {}

        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }


    public void addApprovalImages(RequestBody index, RequestBody bookingId, MultipartBody.Part imageFile) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<InspectionImageResponse> myCall = apiRequest.addApprovalImages(index, bookingId, imageFile);
        myCall.enqueue(new ApiCallback.MyCallback<InspectionImageResponse>() {
            @Override
            public void success(Response<InspectionImageResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}

        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

    public void addAdditionalImages(RequestBody index, RequestBody bookingId, MultipartBody.Part imageFile) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<InspectionImageResponse> myCall = apiRequest.addAdditionalImages(index, bookingId, imageFile);
        myCall.enqueue(new ApiCallback.MyCallback<InspectionImageResponse>() {
            @Override
            public void success(Response<InspectionImageResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccess(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}

        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

}
