package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.SavedBusinessResponse;
import autroid.business.view.fragment.SavedBusinessesFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 03/07/18.
 */

public class SavedBusinessesPresenter {

    private SavedBusinessesFragment mFragment;
    private ViewGroup mMainLayout;

    public SavedBusinessesPresenter(SavedBusinessesFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getSearchedBusiness() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<SavedBusinessResponse> myCall = apiRequest.getSaveBusiness();
        myCall.enqueue(new ApiCallback.MyCallback<SavedBusinessResponse>() {
            @Override
            public void success(final Response<SavedBusinessResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SavedBusinessResponse searchBusinessResponse = response.body();
                            mFragment.onSuccess(searchBusinessResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }
}
