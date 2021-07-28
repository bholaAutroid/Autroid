package autroid.business.view.fragment.orders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import autroid.business.R;
import autroid.business.adapter.orders.OrdersAdapter;
import autroid.business.interfaces.OrdersListener;
import autroid.business.model.bean.OrderDataBE;
import autroid.business.model.realm.OrdersRealm;
import autroid.business.model.response.OrdersResponse;
import autroid.business.presenter.orders.OrdersPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

public class OrdersFragment extends Fragment implements OrdersListener, SwipeRefreshLayout.OnRefreshListener {

    int pageNo=0;

    TextView title;

    RecyclerView orders_recycler;

    OrdersPresenter ordersPresenter;

    ConstraintLayout mainLayout;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.orders_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title=view.findViewById(R.id.title);
        orders_recycler=view.findViewById(R.id.orders_recycler);
        mainLayout=view.findViewById(R.id.main_layout);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        title.setText("Orders");

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController=new RealmController(getActivity().getApplication());

        realmController.clearAllOrders();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        orders_recycler.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);

        mScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                ordersPresenter.getOrders(pageNo);
            }
        };

        ordersPresenter=new OrdersPresenter(this,mainLayout);
        ordersPresenter.getOrders(pageNo);
    }

    public void onSuccessOrders(OrdersResponse ordersResponse){

        swipeRefreshLayout.setRefreshing(false);

        title.setText("Orders"+"("+ordersResponse.getTotalResponse().getTotalResult()+")");

        for (OrderDataBE data:ordersResponse.getOrdersList().getOrderData()) {

            OrdersRealm ordersRealm=new OrdersRealm();

            ordersRealm.setId(data.getId());
            ordersRealm.setContact_no(ordersRealm.getContact_no());
            ordersRealm.setName(data.getUser().getName());
            ordersRealm.setDeliveredBy(data.getDelivery_date());
            ordersRealm.setOrderNumber(data.getOrder_no());
            ordersRealm.setStatus(data.getStatus());

            realm.beginTransaction();
            realm.copyToRealm(ordersRealm);
            realm.commitTransaction();
        }
        
        if(pageNo==0) {
            OrdersAdapter adapter = new OrdersAdapter(realmController.getOrders(), true, this,"listOrder");
            orders_recycler.setAdapter(adapter);
        }
    }

    @Override
    public void onCallClick(String contact_no) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact_no));
        if(intent.resolveActivity(getActivity().getPackageManager())!=null) startActivity(intent);
        else Toast.makeText(getActivity(),"No suitable application available...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCardClick(String orderId) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,orderId);
        ((AwsHomeActivity)getActivity()).addFragment(new OrderDetailsFragment(),"DetailsFragment",true,false,bundle,(((AwsHomeActivity) getActivity()).currentFrameId));
    }

    @Override
    public void onRefresh() {
        realmController.clearAllOrders();
        pageNo=0;
        ordersPresenter.getOrders(pageNo);
    }
}


