package autroid.business.view.fragment.carsales;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.R;
import autroid.business.adapter.cars.BookingAprovedDetailAdapter;
import autroid.business.adapter.cars.MyBookingEstimateAdapter;
import autroid.business.model.bean.ServiceBE;
import autroid.business.model.realm.EstimateTaxCalculationRealm;
import autroid.business.model.request.ServiceApproveRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingEstimateResponse;
import autroid.business.presenter.carsales.BookingDetailDialogPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */


public class BookingDetailDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.booking_list)
    RecyclerView mBookingList;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;//layout_bottom

    @BindView(R.id.layout_bottom)
    LinearLayout mBottom;//

    @BindView(R.id.btn_approve)
    Button mApprove;

    @BindView(R.id.default_message)
    TextView mDefMessage;
    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.gst)
    TextView mGST;
    @BindView(R.id.total)
    TextView mTotal;

    String bookingId;
    RealmController mRealmController;
    Realm mRealm;

    BookingDetailDialogPresenter mPresenter;
    private MyBookingEstimateAdapter bookingEstimateAdapter;
    private BookingAprovedDetailAdapter bookingDetailAdapter;
    private ArrayList<ServiceBE> listApproved;
    private ArrayList<ServiceBE> listEstimates;

    Boolean isServices;

    ArrayList<String> list=new ArrayList<>();

    public BookingDetailDialogFragment() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_booking_dialog_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();
        mRealmController.clearGSTInfo();

        mPresenter=new BookingDetailDialogPresenter(this,mMainLayout);
        mApprove.setOnClickListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llm);

        bookingId=getArguments().getString(Constant.KEY_ID);
        isServices=getArguments().getBoolean(Constant.KEY_IS_VIEW);
        mPresenter.getBookingDetails(bookingId,"id");

        /*try {
            RealmList<BookingDetailRealm> selectedBookingDataRealm = mRealmController.getBookingsById(bookingId);
            if (null != selectedBookingDataRealm) {

            }
        }catch (Exception e){

        }*/


    }

    public void onSuccessBookingDetails(BookingEstimateResponse body) {
       listApproved=new ArrayList<>();
        listEstimates=new ArrayList<>();
        Double total=0.0;
        EstimateTaxCalculationRealm estimateTaxCalculationRealm=new EstimateTaxCalculationRealm();
        for(int i=0;i<body.getServiceBES().size();i++){


            if(body.getServiceBES().get(i).isCustomer_approval()){
                listApproved.add(body.getServiceBES().get(i));
            }
            else{
                if(!isServices){
                    total=total+body.getServiceBES().get(i).getCost();
                }

                listEstimates.add(body.getServiceBES().get(i));
                for(int j=0;j<body.getServiceBES().get(i).getParts().size();j++){
                    estimateTaxCalculationRealm.setAmount(body.getServiceBES().get(i).getParts().get(j).getTax_amount());
                    estimateTaxCalculationRealm.setTax(body.getServiceBES().get(i).getParts().get(j).getTax_rate());
                    mRealm.beginTransaction();
                    mRealm.copyToRealm(estimateTaxCalculationRealm);
                    mRealm.commitTransaction();

                    if(!list.contains(body.getServiceBES().get(i).getParts().get(j).getTax_rate())){
                        list.add(body.getServiceBES().get(i).getParts().get(j).getTax_rate()+"");
                    }
                }
                for(int j=0;j<body.getServiceBES().get(i).getLabour().size();j++){
                    estimateTaxCalculationRealm.setAmount(body.getServiceBES().get(i).getLabour().get(j).getTax_amount());
                    estimateTaxCalculationRealm.setTax(body.getServiceBES().get(i).getLabour().get(j).getTax_rate());
                    mRealm.beginTransaction();
                    mRealm.copyToRealm(estimateTaxCalculationRealm);
                    mRealm.commitTransaction();

                    if(!list.contains(body.getServiceBES().get(i).getLabour().get(j).getTax_rate())){
                        list.add(body.getServiceBES().get(i).getLabour().get(j).getTax_rate()+"");
                    }
                }
                for(int j=0;j<body.getServiceBES().get(i).getOpening_fitting().size();j++){
                    estimateTaxCalculationRealm.setAmount(body.getServiceBES().get(i).getOpening_fitting().get(j).getTax_amount());
                    estimateTaxCalculationRealm.setTax(body.getServiceBES().get(i).getOpening_fitting().get(j).getTax_rate());
                    mRealm.beginTransaction();
                    mRealm.copyToRealm(estimateTaxCalculationRealm);
                    mRealm.commitTransaction();

                    if(!list.contains(body.getServiceBES().get(i).getOpening_fitting().get(j).getTax_rate())){
                        list.add(body.getServiceBES().get(i).getOpening_fitting().get(j).getTax_rate()+"");
                    }
                }
            }
        }


        if(isServices){
            mTitle.setText("Booking Details");
            showService();
        }
        else {
            showEstimate();
            voidFindGST();
            mTitle.setText("Estimate");
            if(total>0)
                mTotal.setText("Total ₹ "+String.format("%.2f", total));
        }
    }

    private void voidFindGST() {

        String gst="";
        for(int i=0;i<list.size();i++){
            float totalTax=0;
                RealmResults<EstimateTaxCalculationRealm> estimateTaxCalculationRealms= mRealmController.getGSTInfo(list.get(i));
                for(int j=0;j<estimateTaxCalculationRealms.size();j++){
                   totalTax=totalTax+estimateTaxCalculationRealms.get(j).getAmount();
                }

           //Log.d(list.get(i),totalTax+"");
               gst=gst+"\n"+"GST @"+list.get(i)+"%  ₹ "+String.format(" %.2f", totalTax);

        }

        mGST.setText(gst);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_approve:
                ServiceApproveRequest serviceApproveRequest=new ServiceApproveRequest();
                serviceApproveRequest.setBooking(bookingId);
                ArrayList<ServiceBE> serviceBES=new ArrayList<>();
                Boolean isApproved=false;
                for(int i=0;i<bookingEstimateAdapter.arrayList.size();i++){
                    if(bookingEstimateAdapter.arrayList.get(i).isSelected()){
                        isApproved=true;
                       // serviceBES.add(bookingReviewAdapter.arrayList.get(i));
                    }
                }

                if(isApproved) {
                    ArrayList<ServiceBE> services=new ArrayList<>();
                    services.addAll(bookingEstimateAdapter.arrayList);
                    services.addAll(listApproved);
                    serviceApproveRequest.setServices(services);
                    mPresenter.approveService(serviceApproveRequest);
                }
                else {
                    Toast.makeText(getActivity(), "Please select the service", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showService(){

        if(listApproved.size()>0) {
            mDefMessage.setVisibility(View.GONE);
           // bookingDetailAdapter = new BookingAprovedDetailAdapter(listApproved, getActivity());
            bookingEstimateAdapter = new MyBookingEstimateAdapter(listApproved, getActivity(),isServices);
            mBookingList.setAdapter(bookingEstimateAdapter);
        }
        else {
            mBookingList.setAdapter(null);
            mDefMessage.setVisibility(View.VISIBLE);
            mDefMessage.setText("No Service Available");
        }
        mApprove.setVisibility(View.GONE);
    }

    private void showEstimate(){

        if(listEstimates.size()>0) {
            mDefMessage.setVisibility(View.GONE);

            bookingEstimateAdapter = new MyBookingEstimateAdapter(listEstimates, getActivity(),isServices);
            mBookingList.setAdapter(bookingEstimateAdapter);
            mApprove.setVisibility(View.VISIBLE);

            mBottom.setVisibility(View.VISIBLE);
        }
        else {
            mBookingList.setAdapter(null);
            mDefMessage.setVisibility(View.VISIBLE);
            mDefMessage.setText("No Estimate Available");
        }
    }

    public void onSuccessApprove(BaseResponse baseResponse) {
            getActivity().onBackPressed();
    }
}
