package autroid.business.presenter.orders;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.OrderDetailsResponse;
import autroid.business.view.fragment.orders.OrderDetailsFragment;
import retrofit2.Response;

public class OrderDetailsPresenter {

    private OrderDetailsFragment fragment;
    private ViewGroup viewGroup;

    public OrderDetailsPresenter(OrderDetailsFragment fragment,ViewGroup mainLayout){
        this.fragment=fragment;
        viewGroup=mainLayout;
    }

    public void getOrderDetails(String orderId){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<OrderDetailsResponse> myCall=apiRequest.getOrdersDetailsResponse(orderId);
        myCall.enqueue(new ApiCallback.MyCallback<OrderDetailsResponse>() {
            @Override
            public void success(Response<OrderDetailsResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessOrderDetails(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }
}
