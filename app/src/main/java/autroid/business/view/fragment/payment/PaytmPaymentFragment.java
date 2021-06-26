package autroid.business.view.fragment.payment;


import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paytm.pgsdk.Log;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.BuildConfig;
import autroid.business.R;
import autroid.business.model.response.ChecksumResponse;
import autroid.business.model.response.PaymentRecheckResponse;
import autroid.business.presenter.PaytmPaymentPresenter;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.carsales.MyGarageFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaytmPaymentFragment extends Fragment {

    String id;
    PaytmPaymentPresenter mPresenter;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    private Dialog dialog;

    @BindView(R.id.ll_header)
    RelativeLayout llHeader;

    @BindView(R.id.booking_ok)
    Button btnOk;

    public PaytmPaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paytm_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        id=getArguments().getString(Constant.KEY_ID);
        String pay = getArguments().getString(Constant.KEY_TYPE);

        mPresenter=new PaytmPaymentPresenter(this,mMainLayout);

        mPresenter.getChecksum(id);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        }


    public void onChecksumSuccess(ChecksumResponse checksumResponse) {
        if(BuildConfig.DEBUG){
            initPaytmStaging(checksumResponse);
        }
        else {
            initPaytm(checksumResponse);
        }
    }

    private void initPaytmStaging(final ChecksumResponse checksumResponse){

        try {

            PaytmPGService Service = PaytmPGService.getStagingService(); //Staging
            //PaytmPGService Service = PaytmPGService.getProductionService(); //Production

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", "Victor33169731061326");
// Key in your staging and production MID available in your dashboard
            paramMap.put("ORDER_ID", checksumResponse.getGet().getORDER_ID());
            paramMap.put("CUST_ID", checksumResponse.getGet().getCUST_ID());
            paramMap.put("MOBILE_NO", checksumResponse.getGet().getMOBILE_NO());
            paramMap.put("EMAIL", checksumResponse.getGet().getEMAIL());
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", checksumResponse.getGet().getTXN_AMOUNT());
            paramMap.put("WEBSITE", "APPSTAGING");
// This is the staging value. Production value is available in your dashboard
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
// This is the staging value. Production value is available in your dashboard
            paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + checksumResponse.getGet().getORDER_ID());
            paramMap.put("CHECKSUMHASH", checksumResponse.getGet().getCHECKSUMHASH());
            PaytmOrder Order = new PaytmOrder(paramMap);

            Service.initialize(Order, null);

            Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
                /*Call Backs*/
                public void someUIErrorOccurred(String inErrorMessage) {
                    Log.d("someUIErrorOccurred", inErrorMessage);
                    llHeader.setVisibility(View.VISIBLE);
                }

                public void onTransactionResponse(Bundle inResponse) {
                    Log.d("onTransactionResponse", inResponse.toString());
                    try {


                            mPresenter.paymentRecheck(inResponse.getString("ORDERID"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                public void networkNotAvailable() {
                    //getActivity().onBackPressed();
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("networkNotAvailable", "networkNotAvailable");
                }

                public void clientAuthenticationFailed(String inErrorMessage) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("clientAuthenticationFailed", inErrorMessage);
                }

                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onErrorLoadingWebPage", inErrorMessage);
                }

                public void onBackPressedCancelTransaction() {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onBackPressedCancelTransaction", "onBackPressedCancelTransaction");
                }

                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onTransactionCancel", inErrorMessage);
                }
            });
        }catch (NullPointerException e){

        }catch (Exception e){

        }

        }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }


    public void showDialogPatym(final String status, String bookingId, String message){
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_payment_status);
        //getView().setBackgroundColor(getResources().getColor(R.color.hint_color_opacity40));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // mTransparentView.setVisibility(View.VISIBLE);

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView mIconStatus=dialog.findViewById(R.id.img_payment_status);
        TextView mBookingHeading=dialog.findViewById(R.id.booking_heading);
        TextView mBookingText=dialog.findViewById(R.id.booking_text);
        TextView mBookingId=dialog.findViewById(R.id.booking_id);
        TextView mTransactionId=dialog.findViewById(R.id.transaction_id);
        TextView mAmount=dialog.findViewById(R.id.amount);
        mBookingId.setVisibility(View.GONE);
        mTransactionId.setVisibility(View.GONE);
        mAmount.setVisibility(View.GONE);
        Button mBookingDone=dialog.findViewById(R.id.booking_ok);


        if(status.equalsIgnoreCase("TXN_SUCCESS")){
            mIconStatus.setBackgroundResource(R.mipmap.ic_payment_success);
            mBookingHeading.setText("AWESOME!");
            mBookingText.setText(message);
            //  mBookingId.setText("Booking ID: "+bookingId);
        }
        else if(status.equalsIgnoreCase("TXN_FAILURE")){
            mIconStatus.setBackgroundResource(R.mipmap.ic_payment_failed);
            mBookingHeading.setText("SORRY!");
            mBookingText.setText(message);
            // mBookingId.setText("Booking ID: "+bookingId);
        }
        else if(status.equalsIgnoreCase("PENDING")){
            mIconStatus.setBackgroundResource(R.mipmap.ic_payment_failed);
            mBookingHeading.setText("SORRY!");
            mBookingText.setText(message);
            //  mBookingId.setText("Booking ID: "+bookingId);
        }

        mBookingDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((HomeScreenActivity)getActivity()).clearStackLocal();
                if(status.equalsIgnoreCase("TXN_SUCCESS")) {
                    dialog.dismiss();

                    ((HomeScreen) getActivity()).clearStackLocal();
                    ((HomeScreen) getActivity()).addFragment(new MyGarageFragment(), "MyGarageFragment", true, false, null, ((HomeScreen) getActivity()).currentFrameId);
                }
                else if(status.equalsIgnoreCase("TXN_FAILURE")) {
                   // mTransparentView.setVisibility(View.GONE);
                    getActivity().onBackPressed();
                    dialog.dismiss();
                }
                else if(status.equalsIgnoreCase("PENDING")) {
                  //  mTransparentView.setVisibility(View.GONE);
                    //  mTransparentView.setVisibility(View.GONE);
                    getActivity().onBackPressed();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void initPaytm(final ChecksumResponse checksumResponse){

        try {

            //  PaytmPGService Service = PaytmPGService.getStagingService(); //Staging
            PaytmPGService Service = PaytmPGService.getProductionService(); //Production

            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", "Victor10565487522591");
// Key in your staging and production MID available in your dashboard
            paramMap.put("ORDER_ID", checksumResponse.getGet().getORDER_ID());
            paramMap.put("CUST_ID", checksumResponse.getGet().getCUST_ID());
            paramMap.put("MOBILE_NO", checksumResponse.getGet().getMOBILE_NO());
            paramMap.put("EMAIL", checksumResponse.getGet().getEMAIL());
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", checksumResponse.getGet().getTXN_AMOUNT());
            paramMap.put("WEBSITE", "APPPROD");
// This is the staging value. Production value is available in your dashboard
            paramMap.put("INDUSTRY_TYPE_ID", "Retail109");
// This is the staging value. Production value is available in your dashboard
            paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + checksumResponse.getGet().getORDER_ID());
            paramMap.put("CHECKSUMHASH", checksumResponse.getGet().getCHECKSUMHASH());
            PaytmOrder Order = new PaytmOrder(paramMap);

            Service.initialize(Order, null);

            Service.startPaymentTransaction(getActivity(), true, true, new PaytmPaymentTransactionCallback() {
                /*Call Backs*/
                public void someUIErrorOccurred(String inErrorMessage) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("someUIErrorOccurred", inErrorMessage);
                }

                public void onTransactionResponse(Bundle inResponse) {
                    Log.d("onTransactionResponse", inResponse.toString());
                    try {

                        mPresenter.paymentRecheck(inResponse.getString("ORDERID"));

                       // mPresenter.paymentRecheck(inResponse.getString("ORDERID"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                public void networkNotAvailable() {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("networkNotAvailable", "networkNotAvailable");
                }

                public void clientAuthenticationFailed(String inErrorMessage) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("clientAuthenticationFailed", inErrorMessage);
                }

                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onErrorLoadingWebPage", inErrorMessage);
                }

                public void onBackPressedCancelTransaction() {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onBackPressedCancelTransaction", "onBackPressedCancelTransaction");
                }

                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    llHeader.setVisibility(View.VISIBLE);
                    Log.d("onTransactionCancel", inErrorMessage);
                }
            });
        }catch (NullPointerException e){

        }catch (Exception e){

        }
    }

    public void onRecheckSuccess(PaymentRecheckResponse paymentRecheckResponse) {
        String status = paymentRecheckResponse.getGetPaymentStatus().getSTATUS();
        showDialogPatym(status, paymentRecheckResponse.getGetPaymentStatus().getORDERID(), paymentRecheckResponse.getResponseMessage());
    }
}
