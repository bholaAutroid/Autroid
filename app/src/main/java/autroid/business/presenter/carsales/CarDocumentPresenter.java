package autroid.business.presenter.carsales;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddImageResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarDocumentResponse;
import autroid.business.model.response.DocumentResponse;
import autroid.business.view.fragment.carsales.CarDocumentFragment;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class CarDocumentPresenter {

    private CarDocumentFragment mFragment;
    private ViewGroup mMainLayout;

    public CarDocumentPresenter(CarDocumentFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void postRc(MultipartBody.Part imageFileBody, RequestBody carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postRCImage(imageFileBody,carId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddImageResponse AddImageResponse = response.body();
                            mFragment.onRCSuccess(AddImageResponse);
                        } else {

                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

    public void postInsurance(MultipartBody.Part imageFileBody, RequestBody carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postInsuranceImage(imageFileBody,carId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddImageResponse AddImageResponse = response.body();
                            mFragment.onInsuranceSuccess(AddImageResponse);
                        } else {

                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

    public void postDocuments(MultipartBody.Part imageFileBody, RequestBody carId,RequestBody caption) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<DocumentResponse> myCall = apiRequest.postCarDocument(imageFileBody,carId,caption);
        myCall.enqueue(new ApiCallback.MyCallback<DocumentResponse>() {
            @Override
            public void success(final Response<DocumentResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                        if (response.body().getResponseCode() == 200) {
                            mFragment.onSuccessDocument(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

    public void deleteDocument(PublishUnpublishRequest publishUnpublishRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeDocument(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessDocumentDelete(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

    public void getCarDocuments(String carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarDocumentResponse> myCall = apiRequest.getCarDocuments(carId);
        myCall.enqueue(new ApiCallback.MyCallback<CarDocumentResponse>() {
            @Override
            public void success(final Response<CarDocumentResponse> response) {
                mFragment.getActivity().runOnUiThread(()-> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessDocuments(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);

    }

}
