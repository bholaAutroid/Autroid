package autroid.business.view.fragment.carsales;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.adapter.jobcard.PaymentLogAdapter;
import autroid.business.model.bean.BookingDataBE;
import autroid.business.model.response.PaymentLogResponse;
import autroid.business.presenter.carsales.PaymentDetailsPresenter;
import autroid.business.utils.Constant;

public class PaymentDetailsFragment extends DialogFragment {

    PaymentDetailsPresenter presenter;

    TextView pickUpCharges, grandTotal, paid, discount, carEagerCash, coupon, due, transactionLog;

    RecyclerView recyclerView;

    BookingDataBE bookingData;

    RelativeLayout mainLayout;

    String bookingId="";

    public PaymentDetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_payment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        getBundlePaymentData();
        setUpPaymentData();

        presenter=new PaymentDetailsPresenter(this,mainLayout);
        presenter.getPaymentLog(bookingId);
    }

    private void findViews(View view) {
        mainLayout =view.findViewById(R.id.main_layout);
        pickUpCharges =view.findViewById(R.id.convenience);
        grandTotal =view.findViewById(R.id.total);
        paid =view.findViewById(R.id.paid);
        discount =view.findViewById(R.id.discount);
        carEagerCash =view.findViewById(R.id.careager_cash);
        coupon =view.findViewById(R.id.coupon);
        due =view.findViewById(R.id.due);
        transactionLog =view.findViewById(R.id.transaction_log);
        recyclerView =view.findViewById(R.id.transaction_log_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void getBundlePaymentData() {
        bookingData = (BookingDataBE) getArguments().getSerializable(Constant.VALUE);
        bookingId=getArguments().getString(Constant.BOOKING_ID);
    }

    private void setUpPaymentData() {

        pickUpCharges.setText("+ ₹ " + bookingData.getPayment().getPick_up_charges());
        grandTotal.setText("₹ " + bookingData.getPayment().getTotal());
        paid.setText("₹ " + bookingData.getPayment().getPaid_total());

        if (bookingData.getDue()!=null)due.setText("₹ " + bookingData.getDue().getDue());

        if (bookingData.getPayment().getDiscount_type().equalsIgnoreCase("coupon")) {
            discount.setText("- ₹ " + bookingData.getPayment().getDiscount_total());
            coupon.setText("(Coupon : " + bookingData.getPayment().getCoupon() + ")");
        } else discount.setText("- ₹ " + bookingData.getPayment().getDiscount_total());

        carEagerCash.setText("- ₹ " + bookingData.getPayment().getCareager_cash());
    }

    public void onPaymentLogSuccess(PaymentLogResponse response) {
        if(response.getLogsList().size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            transactionLog.setVisibility(View.VISIBLE);
            PaymentLogAdapter paymentLogAdapter=new PaymentLogAdapter(response.getLogsList());
            recyclerView.setAdapter(paymentLogAdapter);
        }
    }
}
