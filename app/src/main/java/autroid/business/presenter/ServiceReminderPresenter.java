package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.ServiceDueResponse;
import autroid.business.view.fragment.leadgeneration.ServiceReminderFragment;
import retrofit2.Response;

public class ServiceReminderPresenter {

    private ServiceReminderFragment mFragment;
    private ViewGroup mMainLayout;

    public ServiceReminderPresenter(ServiceReminderFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getService(int page,Boolean isLoading) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ServiceDueResponse> myCall = apiRequest.getService(page);
        myCall.enqueue(new ApiCallback.MyCallback<ServiceDueResponse>() {
            @Override
            public void success(final Response<ServiceDueResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) mFragment.onSuccess(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,isLoading);
    }
}
