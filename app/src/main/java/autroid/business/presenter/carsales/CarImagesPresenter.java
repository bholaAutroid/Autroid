package autroid.business.presenter.carsales;

import android.view.ViewGroup;

import java.io.IOException;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddImageResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.fragment.carsales.CarImagesFragment;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CarImagesPresenter {

    private CarImagesFragment mFragment;
    private ViewGroup mMainLayout;

    public CarImagesPresenter(CarImagesFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void removeCarPic(PublishUnpublishRequest publishUnpublishRequest, final String pos) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeCarPic(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                        if (response.body().getResponseCode() == 200)mFragment.onDeleteSuccess(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {
            }

        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void postImageCar(MultipartBody.Part imageFileBody, RequestBody carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postImageCar(imageFileBody,carId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {

                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200){
                        try {
                            mFragment.onSuccessImageUpload(response.body());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        },mFragment.getActivity(), mMainLayout,Boolean.TRUE);


    }


}
