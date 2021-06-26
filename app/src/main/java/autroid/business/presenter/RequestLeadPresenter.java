package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.RequestLeadResponse;
import autroid.business.view.fragment.RequestLeadsFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 08/10/17.
 */

public class RequestLeadPresenter {

    private RequestLeadsFragment mFragment;
    private ViewGroup mMainLayout;

    public RequestLeadPresenter(RequestLeadsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getAllLeads() {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<RequestLeadResponse> myCall = apiRequest.getLeads();
        myCall.enqueue(new ApiCallback.MyCallback<RequestLeadResponse>() {
            @Override
            public void success(final Response<RequestLeadResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            RequestLeadResponse requestLeadResponse = response.body();
                            mFragment.onItemSuccess(requestLeadResponse);
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
