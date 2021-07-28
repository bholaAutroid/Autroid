package autroid.business.view.fragment.carsales;


import android.app.Application;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import autroid.business.aws.AwsHomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.BookingReviewAdapter;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.PaymentDetailRequest;
import autroid.business.model.response.AddBookingResponse;
import autroid.business.model.response.PaymentDetailResponse;
import autroid.business.presenter.BookingCheckoutPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */

public class BookingCheckoutFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.total_amount)
    TextView mTotalAmount;
    @BindView(R.id.coins_amount)
    TextView mCoinAmount;
    @BindView(R.id.coupon_amount)
    TextView mCouponAmount;
    @BindView(R.id.final_amount)
    TextView mFinalAmount;
    @BindView(R.id.remove_coupon)
    TextView mRemoveCoupon;

    @BindView(R.id.ll_transparent)
    View mTransparentView;

    @BindView(R.id.paid_amount)
    TextView mPaidAmount;

    @BindView(R.id.charges_text)
    TextView mChargesText;

    @BindView(R.id.ll_payment_details)
    LinearLayout mPaymentDetail;

    @BindView(R.id.booking_list)
    RecyclerView mBookingList;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    @BindView(R.id.txt_terms)
    TextView mTerms;

    @BindView(R.id.ll_pickup)
    RelativeLayout mLLPickup;

    @BindView(R.id.pickup_amount)
    TextView mPickupAmount;

    @BindView(R.id.cb_terms)
    AppCompatCheckBox mCBTerms;

    @BindView(R.id.book_now)
    Button mBookNow;
    @BindView(R.id.pay_later)
    Button mPayLater;



    BookingCheckoutPresenter mPresenter;

    String id="";
    private Dialog dialog;
    private float paidAmount=-1;

    private Realm realm;
    RealmController realmController;

   // private PayUmoneySdkInitializer.PaymentParam mPaymentParams;

    String termsMessage="",pickupMessage="";
    private double advanceAmount=0.0;

    public BookingCheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mPresenter=new BookingCheckoutPresenter(this,mMainLayout);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llm);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);
        RealmResults<SelectedBookingDataRealm> selectedBookingDataRealm=realmController.getBookingData();
        BookingReviewAdapter bookingReviewAdapter=new BookingReviewAdapter(selectedBookingDataRealm,true,getActivity());
        mBookingList.setAdapter(bookingReviewAdapter);
        mBookingList.setNestedScrollingEnabled(false);

        id=getArguments().getString(Constant.KEY_ID);

        mBookNow.setOnClickListener(this);
        mRemoveCoupon.setOnClickListener(this);
        mTerms.setOnClickListener(this);
        mPayLater.setOnClickListener(this);
        mLLPickup.setOnClickListener(this);

        /*PaymentDetailRequest paymentDetailRequest=new PaymentDetailRequest();
        paymentDetailRequest.setId(id);
        mPresenter.getAmountDetail(paymentDetailRequest);*/

        mTitle.setText(R.string.review_booking);

        PaymentDetailRequest paymentDetailRequest=new PaymentDetailRequest();
        paymentDetailRequest.setId(id);
        paymentDetailRequest.setCoupon("");
        paymentDetailRequest.setType("");
        mPresenter.getDiscountApply(paymentDetailRequest);

    }

    public void onSuccess(PaymentDetailResponse paymentDetailResponse) {

        mPaymentDetail.setVisibility(View.VISIBLE);

        mTotalAmount.setText("₹ "+paymentDetailResponse.getGetPaymentData().getGetPaymentData().getTotal()+"");
        mFinalAmount.setText("₹ "+paymentDetailResponse.getGetPaymentData().getDue().getDue()+"");
        mPaidAmount.setText("₹ "+paymentDetailResponse.getGetPaymentData().getDue().getDue()+"");
        paidAmount=paymentDetailResponse.getGetPaymentData().getDue().getDue();

        if(paymentDetailResponse.getGetPaymentData().getGetPaymentData().getPick_up_charges()>0){
            mLLPickup.setVisibility(View.VISIBLE);
            mPickupAmount.setText("+ ₹ "+paymentDetailResponse.getGetPaymentData().getGetPaymentData().getPick_up_charges()+"");
            //  mChargesText.setText("(There are no Pickup/Doorstep charges if booking/invoice value exceeds ₹"+Math.round(paymentDetailResponse.getGetPaymentData().getGetPaymentData().getPick_up_limit())+")");
        }
        else {
            mLLPickup.setVisibility(View.GONE);
        }

        mCoinAmount.setText("- ₹ "+paymentDetailResponse.getGetPaymentData().getGetPaymentData().getCareager_cash());
        mCouponAmount.setText("- ₹ "+paymentDetailResponse.getGetPaymentData().getGetPaymentData().getDiscount_total());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.pay_later: {

                ((AwsHomeActivity)getActivity()).disableButton(mPayLater);

                PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest();
                paymentDetailRequest.setId(id);
                mPresenter.bookingPayLater(paymentDetailRequest);
               /* if(mCBTerms.isChecked()){
                    if(paidAmount!=-1) {


                    }
                }*/
                break;
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //Log.i(tag, "keyCode: " + keyCode);
                    if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        getActivity().onBackPressed();
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e){

        }
    }


    public void onBookingSuccess(AddBookingResponse addBookingResponse) {
        Toast.makeText(getActivity(), addBookingResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
        ((AwsHomeActivity) getActivity()).clearStackLocal();
        ((AwsHomeActivity) getActivity()).addFragment(new MyBookingsFragment(), "MyBookingsFragment", true, false, null, ((AwsHomeActivity) getActivity()).currentFrameId);

    }
}
