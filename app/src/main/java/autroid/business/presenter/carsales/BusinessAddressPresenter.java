package autroid.business.presenter.carsales;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BusinessPinResponse;
import autroid.business.view.fragment.carsales.BusinessAddressFragment;
import retrofit2.Response;

public class BusinessAddressPresenter {

    private BusinessAddressFragment addressFragment;
    private ViewGroup viewGroup;

    public BusinessAddressPresenter(BusinessAddressFragment addressFragment, ViewGroup viewGroup){
        this.addressFragment=addressFragment;
        this.viewGroup=viewGroup;
    }

    public void getPinData(String zip) {
        ApiRequest apiRequest= ApiFactory.createService(addressFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<BusinessPinResponse> myCall=apiRequest.getBusinessPinData(zip);
        myCall.enqueue(new ApiCallback.MyCallback<BusinessPinResponse>() {
            @Override
            public void success(Response<BusinessPinResponse> response) {
                addressFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200) addressFragment.onSuccessPinResponse(response.body());
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },addressFragment.getActivity(),viewGroup,Boolean.TRUE);
    }
}
