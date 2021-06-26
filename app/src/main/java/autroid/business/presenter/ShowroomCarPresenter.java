package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ShowroomCarsResponse;
import autroid.business.view.fragment.profile.ShowroomCarsFragment;
import retrofit2.Response;

public class ShowroomCarPresenter {

    private ShowroomCarsFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomCarPresenter(ShowroomCarsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getAllCars(String showroomID, final int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomCarsResponse> myCall = apiRequest.getShowroomCars(showroomID,page);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomCarsResponse>() {
            @Override
            public void success(final Response<ShowroomCarsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomCarsResponse showroomCarsResponse = response.body();
                            mFragment.onSuccess(showroomCarsResponse,page);
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
