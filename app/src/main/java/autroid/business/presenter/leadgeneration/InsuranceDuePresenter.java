package autroid.business.presenter.leadgeneration;

import android.view.ViewGroup;
import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.InsuranceDueResponse;
import autroid.business.view.fragment.leadgeneration.InsuranceDueFragment;
import retrofit2.Response;

public class InsuranceDuePresenter {

    private InsuranceDueFragment mFragment;
    private ViewGroup mMainLayout;

    public InsuranceDuePresenter(InsuranceDueFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getInsuranceData(int page,Boolean isLoading) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<InsuranceDueResponse> myCall = apiRequest.getInsuranceDue(page);
        myCall.enqueue(new ApiCallback.MyCallback<InsuranceDueResponse>() {
            @Override
            public void success(final Response<InsuranceDueResponse> response) {
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
