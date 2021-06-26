package autroid.business.view.fragment.orders;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import autroid.business.R;
import autroid.business.adapter.orders.OrderDetailsAdapter;
import autroid.business.model.bean.OrderDetailBE;
import autroid.business.model.response.OrderDetailsResponse;
import autroid.business.presenter.orders.OrderDetailsPresenter;
import autroid.business.utils.Constant;
import autroid.business.view.fragment.jobcard.JobsUserDetails;

public class OrderDetailsFragment extends Fragment implements View.OnClickListener{

    OrderDetailsPresenter detailsPresenter;

    OrderDetailsResponse detailsResponse;

    Button items,payment,paymentReceive;

    LinearLayout main_layout, paymentDetails;

    RelativeLayout carLayout,userDetail;

    String orderId;

    TextView carName, date, orderNumber, status, name, convenience, paid, due, discount, coupon;

    TextView partCost, labourCost, pickUpCharges,grandTotal;

    RecyclerView details_recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);
        getBundleData();

        detailsPresenter = new OrderDetailsPresenter(this, main_layout);
        detailsPresenter.getOrderDetails(orderId);
    }

    private void findViews(View view) {
        main_layout = view.findViewById(R.id.main_layout);
        paymentDetails = view.findViewById(R.id.layout_payment);
        userDetail = view.findViewById(R.id.user_detail);
        items = view.findViewById(R.id.items);
        payment = view.findViewById(R.id.payment);
        orderNumber = view.findViewById(R.id.order_no);
        status = view.findViewById(R.id.status);
        date = view.findViewById(R.id.date);
        name = view.findViewById(R.id.name);
        carLayout = view.findViewById(R.id.car_detail);
        carName = view.findViewById(R.id.car_name);
        convenience = view.findViewById(R.id.convinience);
        details_recycler = view.findViewById(R.id.order_details_recycler);
        details_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        partCost = view.findViewById(R.id.part_cost);
        labourCost = view.findViewById(R.id.labour_cost);
        pickUpCharges = view.findViewById(R.id.convenience);
        grandTotal = view.findViewById(R.id.total);
        paid = view.findViewById(R.id.paid);
        due = view.findViewById(R.id.due);

        discount = view.findViewById(R.id.discount);
        coupon = view.findViewById(R.id.coupon);

        paymentReceive=view.findViewById(R.id.payment_receive);

        items.setOnClickListener(this);
        payment.setOnClickListener(this);
        userDetail.setOnClickListener(this);
    }

    private void getBundleData() {
        Bundle bundle = getArguments();
        orderId = bundle.getString(Constant.KEY_ID);
    }

    public void onSuccessOrderDetails(OrderDetailsResponse detailsResponse) {

        this.detailsResponse=detailsResponse;

        setUpPrimaryData(detailsResponse);

        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(detailsResponse.getOrderDetails().getItemData());
        details_recycler.setAdapter(orderDetailsAdapter);
        setOrderPayment(detailsResponse.getOrderDetails());
    }

    private void setUpPrimaryData(OrderDetailsResponse response) {

        OrderDetailBE data = response.getOrderDetails();

        orderNumber.setText("ORDER #" + data.getOrder_no());

        if(!data.getDelivery_date().equalsIgnoreCase("Invalid date"))date.setText(data.getDelivery_date());
        else date.setText("Not Available");

        name.setText(data.getUserData().getName());
        status.setText(data.getStatus());
        convenience.setText(data.getConvenience());

        if (data.getCarDetail() != null) carName.setText(data.getCarDetail().getRegistration_no() + "\n" + data.getCarDetail().getTitle());
        else carLayout.setVisibility(View.GONE);

    }

    private void setOrderPayment(OrderDetailBE data) {

        labourCost.setText("₹ " + data.getPayment().getLabour_cost());
        partCost.setText("₹ " + data.getPayment().getPart_cost());
        pickUpCharges.setText("₹ " + data.getPayment().getPick_up_charges());
        grandTotal.setText("₹ " + data.getPayment().getTotal());
        paid.setText("₹ " + data.getPayment().getPaid_total());

        if (data.getDue() != null) {
            due.setText("₹ " + data.getDue().getDue());
            if (data.getDue().getDue() <= 0) paymentReceive.setVisibility(View.GONE);
        } else {
            due.setText("₹ 0.0");
            paymentReceive.setVisibility(View.GONE);
        }

        if (data.getPayment().getDiscount_type().equalsIgnoreCase("coins")) {
            discount.setText("₹ " + data.getPayment().getDiscount_total());
            coupon.setText("(CarEagerCash) ");
        } else if (data.getPayment().getDiscount_type().equalsIgnoreCase("coupon")) {
            discount.setText("₹ " + data.getPayment().getDiscount_total());
            coupon.setText("(Coupon : " + data.getPayment().getCoupon() + ")");
        } else discount.setText("₹ " + data.getPayment().getDiscount_total());

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.items:
                paymentDetails.setVisibility(View.GONE);
                details_recycler.setVisibility(View.VISIBLE);
                items.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                payment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.payment:
                details_recycler.setVisibility(View.GONE);
                paymentDetails.setVisibility(View.VISIBLE);
                payment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                items.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.user_detail:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    if (detailsResponse.getOrderDetails().getUserData() != null)
                        bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getOrderDetails().getUserData());
                    if (detailsResponse.getOrderDetails().getAddressData() != null)
                        bundle.putSerializable(Constant.Key_Business_address, detailsResponse.getOrderDetails().getAddressData());
                    bundle.putString(Constant.DETAILS_TYPE, "Customer Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsUserDetails");
                }
                break;
        }
    }
}
