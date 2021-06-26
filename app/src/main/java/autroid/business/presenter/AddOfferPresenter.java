package autroid.business.presenter;

import android.view.ViewGroup;

import java.io.File;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddOfferRequest;
import autroid.business.model.request.UploadMultipleImagesRequest;
import autroid.business.model.response.AddOfferResponse;
import autroid.business.view.fragment.AddOfferFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class AddOfferPresenter {

    private AddOfferFragment mFragment;
    private ViewGroup mMainLayout;

    public AddOfferPresenter(AddOfferFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void postOffer(final AddOfferRequest addOfferRequest,String imagePath) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        File file = new File(imagePath);

        String filenameArray[] = file.getName().split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/"+extension), file);
        MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media", file.getName(), requestBody);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), addOfferRequest.getDescription());
        RequestBody offer = RequestBody.create(MediaType.parse("text/plain"), addOfferRequest.getOffer());
        RequestBody validTill = RequestBody.create(MediaType.parse("text/plain"), addOfferRequest.getValid_till());
        ApiCallback.MyCall<AddOfferResponse> myCall = apiRequest.postOffer(imageFileBody,description,offer,validTill);
        myCall.enqueue(new ApiCallback.MyCallback<AddOfferResponse>() {
            @Override
            public void success(final Response<AddOfferResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddOfferResponse addOfferResponse = response.body();
                            mFragment.onSuccess(addOfferResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void editOffer(final AddOfferRequest addOfferRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddOfferResponse> myCall = apiRequest.editOffer(addOfferRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddOfferResponse>() {
            @Override
            public void success(final Response<AddOfferResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddOfferResponse addOfferResponse = response.body();
                            mFragment.onEditSuccess(addOfferResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void editOfferImage(String imagePath,final UploadMultipleImagesRequest addOfferRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        File file = new File(imagePath);
        String filenameArray[] = file.getName().split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/"+extension), file);
        MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media", file.getName(), requestBody);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"),addOfferRequest.getCar_id());
        ApiCallback.MyCall<AddOfferResponse> myCall = apiRequest.editOfferImage(imageFileBody,id);
        myCall.enqueue(new ApiCallback.MyCallback<AddOfferResponse>() {
            @Override
            public void success(final Response<AddOfferResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddOfferResponse baseResponse = response.body();
                            mFragment.onSuccessImage(baseResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
