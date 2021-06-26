package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BusinessOverviewResponse;
import autroid.business.view.fragment.BusinessOverviewFragment;
import retrofit2.Response;

public class BusinessOverviewPresenter {

    private BusinessOverviewFragment fragment;

    private ViewGroup viewGroup;


    public BusinessOverviewPresenter(BusinessOverviewFragment fragment, ViewGroup viewGroup) {
        this.fragment = fragment;
        this.viewGroup = viewGroup;
    }

    public void getOverview(String type,String query) {
        ApiRequest apiRequest = ApiFactory.createService(fragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BusinessOverviewResponse> myCall = apiRequest.getOverview(type,query);
        myCall.enqueue(new ApiCallback.MyCallback<BusinessOverviewResponse>() {
            @Override
            public void success(final Response<BusinessOverviewResponse> response) {
                fragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        fragment.onSuccessOverview(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
            }

        }, fragment.getActivity(), viewGroup, Boolean.TRUE);
    }
}
