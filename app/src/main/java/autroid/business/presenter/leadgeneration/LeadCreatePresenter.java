package autroid.business.presenter.leadgeneration;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.CreateLeadRequest;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.view.fragment.leadgeneration.LeadGenerateDetailFragment;
import retrofit2.Response;

public class LeadCreatePresenter {

    private LeadGenerateDetailFragment mFragment;
    private ViewGroup mMainLayout;

    public LeadCreatePresenter(LeadGenerateDetailFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void createLead(CreateLeadRequest createLeadRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CreateLeadResponse> myCall = apiRequest.createLead(createLeadRequest);
        myCall.enqueue(new ApiCallback.MyCallback<CreateLeadResponse>() {
            @Override
            public void success(final Response<CreateLeadResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) mFragment.onSuccess(response.body());
                });
            }
            @Override
            public void error(String errorMessage) {
            }

        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }


}
