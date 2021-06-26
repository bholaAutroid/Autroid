package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.view.fragment.jobcard.JobCardCarSelectionFragment;
import retrofit2.Response;

public class CarSelectionPresenter {

    private JobCardCarSelectionFragment mFragment;
    private ViewGroup mMainLayout;

    public CarSelectionPresenter(JobCardCarSelectionFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getCarList(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserCarResponse> myCall = apiRequest.getUserCar(id);
        myCall.enqueue(new ApiCallback.MyCallback<GetUserCarResponse>() {
            @Override
            public void success(final Response<GetUserCarResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onListSuccess(response.body());
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }


}
