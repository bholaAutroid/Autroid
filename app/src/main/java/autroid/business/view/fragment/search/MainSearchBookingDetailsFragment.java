package autroid.business.view.fragment.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.BookingDetailsAdapter;
import autroid.business.adapter.booking.BookingRemarksAdapter;
import autroid.business.adapter.booking.BookingRequirementAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingDetailCallback;
import autroid.business.model.bean.BookingDataBE;
import autroid.business.model.bean.ServiceBE;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.request.BookingUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.presenter.BookingDetailsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.booking.BookingCarDetails;
import autroid.business.view.fragment.booking.BookingRescheduleFragment;
import autroid.business.view.fragment.booking.BookingScheduleFragment;
import autroid.business.view.fragment.booking.BookingReviewFragment;
import autroid.business.view.fragment.jobcard.JobCardCarFragment;
import autroid.business.view.fragment.jobcard.JobCardPaymentReceiveFragment;
import autroid.business.view.fragment.jobcard.JobsUserDetails;

public class  MainSearchBookingDetailsFragment extends Fragment implements View.OnClickListener, BookingDetailCallback {

    TextView bookingNo,status,name,convinience,paid,due,address,labourCost,partCost,pickUpCharges,mCarEagerCash,discount,grandTotal,coupon,bookingItems,carName,mDate;

    Button mServices,mPayment,mRemarks,mSaveRemark,mRequisite;

    Button confirm,reschedule, cancel,jobOpen,reminder,feedback,schedule,mPaymentReceive;

    LinearLayout mLLPayment,mLLRemarks;

    RelativeLayout userDetail, carDetail;

    EditText mEditRemark;
    ImageButton call_btn,chat_btn;

    AlertDialog alertDialog;

    LinearLayout mainLayout;

    RecyclerView bookingDetailsRecycler,mRemarkList,bookingRequirements;

    BookingDetailsAdapter bookingDetailsAdapter;

    String bookingId,userId,contactNo;

    boolean isCRE,isMainSearch;

    Bundle bundle;

    ArrayList<ServiceBE> arrayList;

    RealmController mRealmController;

    BookingDetailsResponse bookingDetailsResponse=null;

    BookingDetailsPresenter detailsPresenter;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.booking_details_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);
        getDataFromBundle();

        GlobalBus.getBus().register(this);

        mRealmController = RealmController.getInstance();

        arrayList=new ArrayList<>();

        bookingItems.setText("Services");


        bookingDetailsAdapter=new BookingDetailsAdapter(arrayList,this::onServiceClick,getActivity(),false,false);
        bookingDetailsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingDetailsRecycler.setAdapter(bookingDetailsAdapter);

        mRemarkList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingRequirements.setLayoutManager(new LinearLayoutManager(getActivity()));

        detailsPresenter=new BookingDetailsPresenter(this,mainLayout);
        detailsPresenter.getBookingDetails(bookingId,"id");

    }

    private void findViews(View view){
        mainLayout=view.findViewById(R.id.main_layout);
        bookingNo=view.findViewById(R.id.booking_no);
        call_btn=view.findViewById(R.id.call_btn);
        chat_btn=view.findViewById(R.id.chat_btn);
        status=view.findViewById(R.id.status);
        name=view.findViewById(R.id.name);
        convinience=view.findViewById(R.id.convinience);
        address=view.findViewById(R.id.address);
        paid=view.findViewById(R.id.paid);
        due=view.findViewById(R.id.due);
        labourCost=view.findViewById(R.id.labour_cost);
        partCost=view.findViewById(R.id.part_cost);
        pickUpCharges=view.findViewById(R.id.convenience);
        mCarEagerCash=view.findViewById(R.id.careager_cash);
        discount=view.findViewById(R.id.discount);
        grandTotal=view.findViewById(R.id.total);
        coupon=view.findViewById(R.id.coupon);
        bookingDetailsRecycler=view.findViewById(R.id.booking_details_recycler);
        mRemarkList=view.findViewById(R.id.remark_list);
        bookingRequirements=view.findViewById(R.id.booking_requirements);
        mPaymentReceive = view.findViewById(R.id.payment_receive);

        bookingItems=view.findViewById(R.id.booking_items);
        carName=view.findViewById(R.id.car_name);
        mDate=view.findViewById(R.id.date);

        mSaveRemark=view.findViewById(R.id.save);
        mEditRemark=view.findViewById(R.id.remark);

        mServices=view.findViewById(R.id.services);
        mRemarks=view.findViewById(R.id.remarks);
        mPayment=view.findViewById(R.id.payment);
        mRequisite=view.findViewById(R.id.requisite);
        confirm=view.findViewById(R.id.confirm);
        reschedule=view.findViewById(R.id.reschedule);
        cancel =view.findViewById(R.id.cancel);
        jobOpen=view.findViewById(R.id.jobcard);
        reminder=view.findViewById(R.id.reminder);
        feedback=view.findViewById(R.id.feedback);
        schedule=view.findViewById(R.id.schedule);


        mServices.setOnClickListener(this);
        mRemarks.setOnClickListener(this);
        mPayment.setOnClickListener(this);
        mSaveRemark.setOnClickListener(this);
        mRequisite.setOnClickListener(this);
        confirm.setOnClickListener(this);
        reschedule.setOnClickListener(this);
        cancel.setOnClickListener(this);
        jobOpen.setOnClickListener(this);
        reminder.setOnClickListener(this);
        call_btn.setOnClickListener(this);
        chat_btn.setOnClickListener(this);
        feedback.setOnClickListener(this);
        schedule.setOnClickListener(this);
        mPaymentReceive.setOnClickListener(this);

        mLLPayment=view.findViewById(R.id.layout_payment);
        mLLRemarks=view.findViewById(R.id.layout_remarks);
        carDetail=view.findViewById(R.id.car_detail);
        userDetail=view.findViewById(R.id.user_detail);

        carDetail.setOnClickListener(this);
        userDetail.setOnClickListener(this);

    }

    public void onSuccessBookingDetails(BookingDetailsResponse detailsResponse){
        userId=detailsResponse.getBookingData().get(0).getUserData().getId();
        contactNo=detailsResponse.getBookingData().get(0).getUserData().getContact_no();
        bookingDetailsResponse=detailsResponse;

        setUpOptions();
        setUpPrimaryData(detailsResponse);
        setUpPaymentData(detailsResponse);

        arrayList.addAll(detailsResponse.getBookingData().get(0).getService());
        bookingDetailsAdapter.notifyDataSetChanged();

        if(detailsResponse.getBookingData().get(0).getRemarks()!=null) {
            BookingRemarksAdapter bookingRemarksAdapter = new BookingRemarksAdapter(detailsResponse.getBookingData().get(0).getRemarks());
            mRemarkList.setAdapter(bookingRemarksAdapter);
        }

        if(detailsResponse.getBookingData().get(0).getRequirements()!=null && detailsResponse.getBookingData().get(0).getRequirements().size()>0){
            BookingRequirementAdapter bookingRequirementAdapter=new BookingRequirementAdapter(detailsResponse.getBookingData().get(0).getRequirements());
            bookingRequirements.setAdapter(bookingRequirementAdapter);
        }
    }

    private void setUpOptions() {
        if(bundle.getString(Constant.KEY_TYPE).equals(Constant.PENDING) && !isCRE){
            confirm.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
        } else if(bundle.getString(Constant.KEY_TYPE).equals(Constant.CONFIRMED) && !isCRE){
            cancel.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
            jobOpen.setVisibility(View.VISIBLE);
        }else if(bundle.getString(Constant.KEY_TYPE).equals(Constant.CONFIRMED) && isCRE){
            reschedule.setVisibility(View.VISIBLE);
        } else if(bundle.getString(Constant.KEY_TYPE).equals(Constant.MISSED) && !isCRE){
            cancel.setVisibility(View.VISIBLE);
            reschedule.setVisibility(View.VISIBLE);
        }else if(bundle.getString(Constant.KEY_TYPE).equals(Constant.ESTIMATE_REQUESTED) || bundle.getString(Constant.KEY_TYPE).equals(Constant.APPROVAL)){
            cancel.setVisibility(View.VISIBLE);
            reminder.setVisibility(View.VISIBLE);
        }else if(bundle.getString(Constant.KEY_TYPE).equals(Constant.PSF)){
            feedback.setVisibility(View.VISIBLE);
        }else if(bundle.get(Constant.KEY_TYPE).equals(Constant.APPROVED) || bundle.get(Constant.KEY_TYPE).equals(Constant.REWORK) ){
            schedule.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        }
    }

    private void setUpPrimaryData(BookingDetailsResponse detailsResponse){
        BookingDataBE data=detailsResponse.getBookingData().get(0);

        bookingNo.setText("BOOKING #"+data.getBooking_no());

        if(!data.getDate().equalsIgnoreCase("Invalid date"))mDate.setText(data.getDate()+" | "+data.getTime_slot());
        else mDate.setText("Not Scheduled");

       /* if(data.getRequirements()!=null && data.getRequirements().size()>0)
            mRequirements.setText("Customer Requirements : "+data.getRequirements().get(0).getRequirement());
        else
            mRequirements.setVisibility(View.GONE);*/

        status.setText(data.getStatus());
        name.setText(data.getUserData().getName());
        convinience.setText(data.getConvenience());

        if (data.getCarDetail() != null) carName.setText(data.getCarDetail().getRegistration_no() + "\n" + data.getCarDetail().getTitle());
        else carName.setVisibility(View.GONE);

//        if (data.getAddress() == null) address.setVisibility(View.GONE);
//        else address.setText(data.getAddress().getAddress() + " , " + data.getAddress().getCity() + " , " + data.getAddress().getZip());
//
    }

    public void setUpPaymentData(BookingDetailsResponse detailsResponse){

        BookingDataBE data=detailsResponse.getBookingData().get(0);
        if(null!=data) {
            if(null!=data.getPayment()) {

                labourCost.setText("₹ " + data.getPayment().getLabour_cost());
                partCost.setText("₹ " + data.getPayment().getPart_cost());
                pickUpCharges.setText("₹ " + data.getPayment().getPick_up_charges());
                paid.setText("₹ " + data.getPayment().getPaid_total());
                grandTotal.setText("₹ " + data.getPayment().getTotal());
                mCarEagerCash.setText("₹ " + data.getPayment().getCareager_cash());

                if(null!=data.getDue()) due.setText("₹ " + data.getDue().getDue());


                if (data.getPayment().getDiscount_type().equalsIgnoreCase("coupon")) {
                    discount.setText("₹ " + data.getPayment().getDiscount_total());
                    coupon.setText("(Coupon : " + data.getPayment().getCoupon() + ")");
                } else discount.setText("₹ " + data.getPayment().getDiscount_total());
            }
        }
    }

    private void getDataFromBundle(){
        bundle=getArguments();
        bookingId=bundle.getString(Constant.KEY_ID);
        isCRE=bundle.getBoolean(Constant.IS_CRE);
        isMainSearch=bundle.getBoolean(Constant.IS_MAIN_SEARCH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.services:
                mLLRemarks.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                bookingDetailsRecycler.setVisibility(View.VISIBLE);
                mServices.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.remarks:
                mLLRemarks.setVisibility(View.VISIBLE);
                mLLPayment.setVisibility(View.GONE);
                bookingDetailsRecycler.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                mRemarks.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.payment:
                mLLRemarks.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.VISIBLE);
                bookingDetailsRecycler.setVisibility(View.GONE);
                mPayment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.save:
                if(mEditRemark.getText().toString().length()>0){
                    ((AwsHomeActivity) getActivity()).disableButton(mSaveRemark);
                    BookingUpdateRequest bookingUpdateRequest=new BookingUpdateRequest();
                    bookingUpdateRequest.setBooking(bookingId);
                    bookingUpdateRequest.setRemark(mEditRemark.getText().toString());
                    detailsPresenter.addBookingRemark(bookingUpdateRequest);
                }
                break;

            case R.id.requisite:
                mLLRemarks.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.GONE);
                bookingDetailsRecycler.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.VISIBLE);
                mRequisite.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.confirm:
                ((AwsHomeActivity) getActivity()).disableButton(confirm);
                BookingStatusRequest bookingStatusRequest = new BookingStatusRequest();
                bookingStatusRequest.setId(bookingId);
                bookingStatusRequest.setStatus(Constant.CONFIRMED);
                detailsPresenter.updateStatus(bookingStatusRequest);
                break;

            case R.id.reschedule:
                ((AwsHomeActivity) getActivity()).disableButton(reschedule);
                BookingRescheduleFragment bookingRescheduleFragment = new BookingRescheduleFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, bookingId);
                bundle.putBoolean(Constant.IS_CRE,isCRE);
                bookingRescheduleFragment.setArguments(bundle);
                bookingRescheduleFragment.show(getChildFragmentManager(), "BookingRescheduleFragment");
                break;

            case R.id.cancel:
                ((AwsHomeActivity) getActivity()).disableButton(cancel);
                rejectBooking(bookingId);
                break;

            case R.id.schedule:
                ((AwsHomeActivity) getActivity()).disableButton(schedule);
                Bundle bookingBundle = new Bundle();
                AddBookingRequest addBookingRequest=new AddBookingRequest();
                addBookingRequest.setServices(bookingDetailsResponse.getBookingData().get(0).getService());
                bookingBundle.putSerializable("AddBookingRequest", addBookingRequest);
                bookingBundle.putString(Constant.KEY_ID, bookingId);
                bookingBundle.putString(Constant.USER_ID, userId);
                bookingBundle.putBoolean(Constant.Is_Category, false);
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                ((AwsHomeActivity) getActivity()).addFragment(new BookingScheduleFragment(), "BookingScheduleFragment", true, false, bookingBundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;

            case R.id.jobcard:
                ((AwsHomeActivity) getActivity()).disableButton(jobOpen);
                Bundle bun = new Bundle();
                bun.putBoolean(Constant.IS_BOOKING, true);
                bun.putString(Constant.USER_ID, userId);
                bun.putString(Constant.BOOKING_ID, bookingId);
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarFragment(), "CarFragment", true, false, bun, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;

            case R.id.user_detail:
                if(bookingDetailsResponse!=null){
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle userBundle = new Bundle();
                    if (bookingDetailsResponse.getBookingData().get(0).getUserData() != null)
                        userBundle.putSerializable(Constant.Key_Business_Name, bookingDetailsResponse.getBookingData().get(0).getUserData());
                    if (bookingDetailsResponse.getBookingData().get(0).getAddress() != null)
                        userBundle.putSerializable(Constant.Key_Business_address, bookingDetailsResponse.getBookingData().get(0).getAddress());
                    userBundle.putString(Constant.DETAILS_TYPE, "Customer Information");
                    jobsUserDetails.setArguments(userBundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsUserDetails");
                }
                break;

            case R.id.car_detail:
                if (bookingDetailsResponse != null) {
                    BookingCarDetails bookingCarDetails = new BookingCarDetails();
                    Bundle carBundle = new Bundle();
                    carBundle.putSerializable(Constant.RESPONSE, bookingDetailsResponse);
                    bookingCarDetails.setArguments(carBundle);
                    bookingCarDetails.show(getChildFragmentManager(), "JobsCarDetails");
                }
                break;

            case R.id.call_btn:
                Utility.onCallClick(contactNo,getActivity());
                break;

            case R.id.chat_btn:
                Utility.onChatClick(userId,getActivity());
                break;

            case R.id.feedback:
                ((AwsHomeActivity) getActivity()).disableButton(feedback);
                Bundle reviewBundle=new Bundle();
                reviewBundle.putString(Constant.BOOKING_ID,bookingId);
                ((AwsHomeActivity) getActivity()).addFragment(new BookingReviewFragment(), "BookingReviewFragment", true, false, reviewBundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;
            case R.id.payment_receive:
                JobCardPaymentReceiveFragment jobCardPaymentReceiveFragment = new JobCardPaymentReceiveFragment();
                Bundle paymentBundle = new Bundle();
                paymentBundle.putString(Constant.KEY_ID, bookingId);
                paymentBundle.putString(Constant.KEY_TYPE, due.getText().toString());
                jobCardPaymentReceiveFragment.setArguments(paymentBundle);
                jobCardPaymentReceiveFragment.show(getChildFragmentManager(), "JobCardPaymentReceiveFragment");
                break;
        }
    }

    private void rejectBooking(String bookingId) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.reject_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancel Remark");
        builder.setView(view);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText text=view.findViewById(R.id.remarks);
                if(text.getText().toString().trim().length()!=0) {
                    BookingStatusRequest bookingStatusRequest=new BookingStatusRequest();
                    bookingStatusRequest.setId(bookingId);
                    bookingStatusRequest.setStatus("Cancelled");
                    bookingStatusRequest.setRemark(text.getText().toString().trim());
                    detailsPresenter.updateStatus(bookingStatusRequest);
                }
                else Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog=builder.create();

        alertDialog.show();

        /*alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{

        });*/
    }

    public void onSuccessBookingRemark(BaseResponse body) {
        Utility.showResponseMessage(mainLayout,body.getResponseMessage());
    }

    public void onSuccessStatusUpdate(BaseResponse baseResponse, BookingStatusRequest bookingStatusRequest) {

        Utility.showResponseMessage(mainLayout, baseResponse.getResponseMessage());

        if(alertDialog!=null)alertDialog.dismiss();

        if(!isMainSearch) {
            if (isCRE)
                mRealmController.updateLeadBookingStatus(bookingStatusRequest.getId(), bookingStatusRequest.getStatus());
            else
                mRealmController.updateBookingStatus(bookingStatusRequest.getId(), bookingStatusRequest.getStatus());
        }else {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_REFRESH_BOOKING);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            GlobalBus.getBus().post(sendEvent);
        }

        getActivity().onBackPressed();
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_RESCHEDULE_BOOKING) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onServiceClick(ServiceBE serviceBE) {

    }
}
