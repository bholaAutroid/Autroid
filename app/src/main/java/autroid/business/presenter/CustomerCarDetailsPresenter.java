package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.CarStockResponse;
import autroid.business.view.fragment.leads.LeadInsuranceCarDetailsFragment;
import retrofit2.Response;


public class CustomerCarDetailsPresenter {

    private LeadInsuranceCarDetailsFragment mFragment;
    private ViewGroup mMainLayout;

    public CustomerCarDetailsPresenter(LeadInsuranceCarDetailsFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }

    public void getCarDetail(String carId) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<CarStockResponse> myCall = apiRequest.getCarDetails(carId);
        myCall.enqueue(new ApiCallback.MyCallback<CarStockResponse>() {
            @Override
            public void success(final Response<CarStockResponse> response) {
                mFragment.getActivity().runOnUiThread(() -> {
                    if (response.body().getResponseCode() == 200) {
                        mFragment.onSuccessCarDetail(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }


}