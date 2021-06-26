package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.view.activity.editCar.EditNewCarActivity;
import retrofit2.Response;

public class EditNewCarPresenter {

    private EditNewCarActivity mActivity;
    private ViewGroup mMainLayout;

    public EditNewCarPresenter(EditNewCarActivity frag, ViewGroup mainLayout) {
        mActivity = frag;
        mMainLayout = mainLayout;
    }


    public void removeCarPic(PublishUnpublishRequest publishUnpublishRequest, final String pos) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.removeCarPic(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BaseResponse baseResponse = response.body();
                            mActivity.onDeleteSuccess(baseResponse,pos);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }


    public void editCar(AddCarRequest addCarRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mActivity.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<AddCarResponse> myCall = apiRequest.editCarStock(addCarRequest);
        myCall.enqueue(new ApiCallback.MyCallback<AddCarResponse>() {
            @Override
            public void success(final Response<AddCarResponse> response) {
                mActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            AddCarResponse addCarResponse = response.body();
                            mActivity.onEditSuccess(addCarResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mActivity.getActivity(), mMainLayout,Boolean.TRUE);
    }
}
