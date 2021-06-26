package autroid.business.presenter;

import android.view.ViewGroup;

import java.io.File;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddBusinessRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.RegistrationDataResponse;
import autroid.business.view.fragment.AddNewBusinessFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/08/17.
 */

public class AddNewBusinessPresenter {

    private AddNewBusinessFragment mFragment;
    private ViewGroup mMainLayout;

    public AddNewBusinessPresenter(AddNewBusinessFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getRegistrationsItems() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<RegistrationDataResponse> myCall = apiRequest.getRegistrationData();
        myCall.enqueue(new ApiCallback.MyCallback<RegistrationDataResponse>() {
            @Override
            public void success(final Response<RegistrationDataResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            RegistrationDataResponse registrationDataResponse = response.body();
                            mFragment.onItemSuccess(registrationDataResponse);
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

    public void addLocalBussiness(String imagePath,AddBusinessRequest addBusinessRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        MultipartBody.Part imageFileBody;
        if(imagePath.length()>0) {
            File file = new File(imagePath);
            String filenameArray[] = file.getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/" + extension), file);
            imageFileBody = MultipartBody.Part.createFormData("media", file.getName(), requestBody);
        }
        else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/*"), "");
             imageFileBody = MultipartBody.Part.createFormData("media", "", requestBody);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getName());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getEmail());
        RequestBody contact = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getContact_no());
        RequestBody category = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getCategory());
        RequestBody company = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getCompany());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getLocation());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getCountry_code());
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), addBusinessRequest.getUsername());

        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.addLocalBusiness(imageFileBody,name,email,contact,category,company,location,addBusinessRequest.getLatitude(),addBusinessRequest.getLongitude(),country,username);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mFragment.addBussSuccess(baseResponse);
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
