package autroid.business.presenter.orders;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.OrdersResponse;
import autroid.business.view.fragment.orders.OrdersFragment;
import retrofit2.Response;

public class OrdersPresenter {

    private OrdersFragment fragment;
    private ViewGroup viewGroup;

    public OrdersPresenter(OrdersFragment fragment,ViewGroup mainLayout){
        this.fragment=fragment;
        viewGroup=mainLayout;
    }

    public void getOrders(int pageNo){
        ApiRequest apiRequest= ApiFactory.createService(fragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<OrdersResponse> myCall=apiRequest.getOrdersResponse(pageNo);
        myCall.enqueue(new ApiCallback.MyCallback<OrdersResponse>() {
            @Override
            public void success(Response<OrdersResponse> response) {
                fragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        fragment.onSuccessOrders(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },fragment.getActivity(),viewGroup,Boolean.TRUE);
    }

}
