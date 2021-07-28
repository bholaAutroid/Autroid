package autroid.business.view.fragment.jobcard;


import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.qiscus.nirmana.Nirmana;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.BookingDetailsAdapter;
import autroid.business.adapter.booking.BookingRemarksAdapter;
import autroid.business.adapter.booking.BookingRequirementAdapter;
import autroid.business.adapter.jobcard.PaymentLogAdapter;
import autroid.business.adapter.qiscus.PhrasesAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.camera.CameraFragment;
import autroid.business.interfaces.BookingDetailCallback;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.RecyclerViewListener;
import autroid.business.model.bean.BookingDataBE;
import autroid.business.model.bean.ServiceBE;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.request.BookingUpdateRequest;
import autroid.business.model.request.UpdateStatusRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.model.response.GetTechniciansResponse;
import autroid.business.model.response.PaymentLogResponse;
import autroid.business.presenter.jobcard.JobCardDetailPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.WebViewFragment;
import autroid.business.view.fragment.booking.BookingRescheduleFragment;
import autroid.business.view.fragment.booking.BookingServiceDetailFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobCardDetailFragment extends Fragment implements View.OnClickListener, BookingDetailCallback, RecyclerViewListener {

    TextView mViewOption, bookingNo, status, name, transactionLog, convinience, paid, due,otherCost, labourCost, partCost, pickUpCharges, discount, grandTotal, coupon, bookingItems, carName, mDate, advisor, advisorContact, technician, technicianContact, driver, driverContact, surveyor, surveyorContact, qc, qcContact, insuranceCompany;
    Button mServices, mEstimate, mPayment, mRemarks, mSaveRemark, mRequisite, mPaymentReceive;
    LinearLayout mLLPayment, mLLRemarks, mDriverLayout, mSurveyorLayout, mTechnicianLayout, mAdvisorLayout, mQCLayout, statusCallChat;
    RelativeLayout userDetail, carDetail, insuranceDetail, bookingRequirements;
    EditText mEditRemark;
    ImageButton call_btn, chat_btn;
    ImageView mPlayback;
    LinearLayout mainLayout;
    SeekBar seekBar;
    AlertDialog alertDialog;
    MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    JobCardDetailPresenter detailsPresenter;
    RecyclerView serviceDetailsRecycler, estimateDetailsRecycler, mRemarkList, bookingRequirementsRecycler, logRecycler, menuRecycler;
    BookingDetailsAdapter serviceDetailsAdapter, estimateDetailsAdapter;
    String bookingId, primaryStatus, subStatus, statusForRealm;
    PopupMenu popup;
    boolean prepared, isPaused;
    ArrayList<ServiceBE> serviceArrayList, estimateArrayList;
    ArrayList<String> menuOptionsList;
    BookingDetailsResponse detailsResponse;
    private Realm realm;
    private RealmController realmController;
    private TextView mCarEagerCash;
    private RelativeLayout mLLCarEagerCash;
    private Boolean isBooking;

    private Boolean isTechnician=false;


    public JobCardDetailFragment() {
        // Required empty public constructor
    }

    /*---------------------------------------------- BASIC METHODS ---------------------------------------------*/

    @Override
    public void onResume() {
        super.onResume();
        if (detailsResponse != null && isPaused) {
            setUpPlayBack(detailsResponse.getBookingData().get(0).getRecording_address());
            seekBar.setProgress(0);
//            Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(mPlayback);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (prepared && mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPaused = true;
        }

        if (handler != null) handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_card_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);
        findViews(view);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());

        bookingItems.setText("Services");
        bookingId = getArguments().getString(Constant.KEY_ID);

        mPaymentReceive.setOnClickListener(this);

        mRemarkList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingRequirementsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        logRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        detailsPresenter = new JobCardDetailPresenter(this, mainLayout);
        detailsPresenter.getBookingDetails(bookingId, "id");
        detailsPresenter.getPaymentLog(bookingId);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (prepared && fromUser) {
                    seekBar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                    startPlayBack();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_ROLE).equalsIgnoreCase("Technician")){
            isTechnician=Boolean.TRUE;
            hideForTechnician();
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.services:
                mLLRemarks.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                estimateDetailsRecycler.setVisibility(View.GONE);
                serviceDetailsRecycler.setVisibility(View.VISIBLE);
                mServices.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mEstimate.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.estimate:
                mLLRemarks.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                serviceDetailsRecycler.setVisibility(View.GONE);
                estimateDetailsRecycler.setVisibility(View.VISIBLE);
                mEstimate.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.remarks:
                mLLRemarks.setVisibility(View.VISIBLE);
                mLLPayment.setVisibility(View.GONE);
                serviceDetailsRecycler.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                estimateDetailsRecycler.setVisibility(View.GONE);
                mRemarks.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mEstimate.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.payment:
                mLLRemarks.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.VISIBLE);
                serviceDetailsRecycler.setVisibility(View.GONE);
                estimateDetailsRecycler.setVisibility(View.GONE);
                mPayment.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mEstimate.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRequisite.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;

            case R.id.save:
                if (mEditRemark.getText().toString().length() > 0) {
                    BookingUpdateRequest bookingUpdateRequest = new BookingUpdateRequest();
                    bookingUpdateRequest.setBooking(bookingId);
                    bookingUpdateRequest.setRemark(mEditRemark.getText().toString());
                    detailsPresenter.addBookingRemark(bookingUpdateRequest);
                }
                break;

            case R.id.requisite:
                mLLRemarks.setVisibility(View.GONE);
                mLLPayment.setVisibility(View.GONE);
                serviceDetailsRecycler.setVisibility(View.GONE);
                estimateDetailsRecycler.setVisibility(View.GONE);
                bookingRequirements.setVisibility(View.VISIBLE);
                mRequisite.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mPayment.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mRemarks.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mServices.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mEstimate.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;
                case R.id.user_detail:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    if (detailsResponse.getBookingData().get(0).getUserData() != null)
                        bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getBookingData().get(0).getUserData());
                    if (detailsResponse.getBookingData().get(0).getAddress() != null)
                        bundle.putSerializable(Constant.Key_Business_address, detailsResponse.getBookingData().get(0).getAddress());
                    bundle.putString(Constant.DETAILS_TYPE, "Customer Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsUserDetails");
                }
                break;

            case R.id.car_detail:
                if (detailsResponse != null) {
                    JobsCarDetails jobsCarDetails = new JobsCarDetails();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.RESPONSE, detailsResponse);
                    bundle.putString(Constant.BOOKING_ID, bookingId);
                    jobsCarDetails.setArguments(bundle);
                    jobsCarDetails.show(getChildFragmentManager(), "JobsCarDetails");
                }
                break;

            case R.id.insurance_details:
                if (detailsResponse != null) {
                    JobsClaimDetails jobsClaimDetails = new JobsClaimDetails();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BOOKING_ID, bookingId);
                    bundle.putSerializable(Constant.INSURANCE_DETAILS, detailsResponse.getBookingData().get(0).getInsuranceData());
                    jobsClaimDetails.setArguments(bundle);
                    jobsClaimDetails.show(getChildFragmentManager(), "JobsClaimDetails");
                }
                break;

            case R.id.advisor_layout:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getBookingData().get(0).getAdvisor());
                    bundle.putString(Constant.DETAILS_TYPE, "Advisor Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsAdvisorDetails");
                }
                break;

            case R.id.technician_layout:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getBookingData().get(0).getTechnician());
                    bundle.putString(Constant.DETAILS_TYPE, "Technician Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsTechnicianDetails");
                }
                break;

            case R.id.driver_layout:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getBookingData().get(0).getDriver());
                    bundle.putString(Constant.DETAILS_TYPE, "Driver Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsDriverDetails");
                }
                break;

            case R.id.surveyor_layout:
                if (detailsResponse != null) {
                    JobsUserDetails jobsUserDetails = new JobsUserDetails();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.Key_Business_Name, detailsResponse.getBookingData().get(0).getSurveyor());
                    bundle.putString(Constant.DETAILS_TYPE, "Surveyor Information");
                    jobsUserDetails.setArguments(bundle);
                    jobsUserDetails.show(getChildFragmentManager(), "JobsSurveyorDetails");
                }
                break;

            case R.id.qc_layout:
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IS_JOBCARD_DETAILS, true);
                bundle.putSerializable(Constant.QC_LIST, detailsResponse.getBookingData().get(0).getJobsQCBEArrayList());
                ((AwsHomeActivity)getActivity()).addFragment(new JobsQualityCheckFragment(), "QualityCheckFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;

            case R.id.playback:
                if (prepared && !mediaPlayer.isPlaying()) startPlayBack();
                else if (prepared && mediaPlayer.isPlaying()) stopPlayBack();
                break;

            case R.id.textViewOptions:
                if (popup != null) popup.show();
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

    /*----------------------------------------------------- API RESPONSE ------------------------------------------------------*/

    public void onSuccessBookingDetails(BookingDetailsResponse detailsResponse) {
        this.detailsResponse = detailsResponse;
        subStatus = detailsResponse.getBookingData().get(0).getSub_status();
        if(subStatus.equalsIgnoreCase("")|| subStatus.length()==0){
            isBooking=true;
        }
        else {
            isBooking=false;
        }
        primaryStatus = detailsResponse.getBookingData().get(0).get_status();

       setUpMenu();

        setUpPrimaryData(detailsResponse);
        setUpPaymentData(detailsResponse);
        segregateData(detailsResponse.getBookingData().get(0).getService());
        addMenuOptions();

        if (detailsResponse.getBookingData().get(0).getInsuranceData() != null)
            serviceDetailsAdapter = new BookingDetailsAdapter(serviceArrayList, this::onServiceClick, getActivity(), detailsResponse.getBookingData().get(0).getInsuranceData().isClaim(),isTechnician);
        else
            serviceDetailsAdapter = new BookingDetailsAdapter(serviceArrayList, this::onServiceClick, getActivity(), false,isTechnician);

        serviceDetailsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceDetailsRecycler.setAdapter(serviceDetailsAdapter);

        if (detailsResponse.getBookingData().get(0).getInsuranceData() != null)
            estimateDetailsAdapter = new BookingDetailsAdapter(estimateArrayList, this::onServiceClick, getActivity(), detailsResponse.getBookingData().get(0).getInsuranceData().isClaim(),isTechnician);
        else
            estimateDetailsAdapter = new BookingDetailsAdapter(estimateArrayList, this::onServiceClick, getActivity(), false,isTechnician);

        estimateDetailsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        estimateDetailsRecycler.setAdapter(estimateDetailsAdapter);

        if (detailsResponse.getBookingData().get(0).getRemarks() != null) {
            BookingRemarksAdapter bookingRemarksAdapter = new BookingRemarksAdapter(detailsResponse.getBookingData().get(0).getRemarks());
            mRemarkList.setAdapter(bookingRemarksAdapter);
        }

        if (detailsResponse.getBookingData().get(0).getRequirements() != null && detailsResponse.getBookingData().get(0).getRequirements().size() > 0) {
            BookingRequirementAdapter bookingRequirementAdapter = new BookingRequirementAdapter(detailsResponse.getBookingData().get(0).getRequirements());
            bookingRequirementsRecycler.setAdapter(bookingRequirementAdapter);
        }

        if (primaryStatus.equals(Constant.JOB_INITIATED) && !detailsResponse.getBookingData().get(0).getSub_status().equals(Constant.JOB_OPEN)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("JobCard Message")
                    .setMessage("JobCard is not complete. Fill the remaining details ?")
                    .setPositiveButton("Proceed", (dialogInterface, which) -> {

                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.BOOKING_ID, bookingId);
                        bundle.putString(Constant.USER_ID, detailsResponse.getBookingData().get(0).getUserData().getId());

                        if (subStatus.equals(Constant.JOB_INITIATED)) {
                            bundle.putBoolean(Constant.IS_JOBCARD, true);

                            String jsonManifest= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_MANIFEST);
                            try {
                                JSONObject jsonObject = new JSONObject(jsonManifest);
                                int picLimit=jsonObject.getInt("job_inspection_limit");
                                if(picLimit>0){
                                    ((AwsHomeActivity) getActivity()).addFragment(new CameraFragment(), "CameraFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                                }
                                else {
                                    JobCardAddressFragment jobCardAddressFragment = new JobCardAddressFragment();
                                    jobCardAddressFragment.setArguments(bundle);
                                    jobCardAddressFragment.show(getFragmentManager(), "AddressFragment");

                                }

                            }catch (Exception e){

                            }
                        } else if (subStatus.equals(Constant.JOB_INSPECTION)) {
                            JobCardAddressFragment addressFragment = new JobCardAddressFragment();
                            addressFragment.setArguments(bundle);
                            addressFragment.show(getFragmentManager(), "AddressFragment");
                        } else if (subStatus.equals(Constant.JOB_ASSETS))
                            ((AwsHomeActivity) getActivity()).addFragment(new JobCardRequirementFragment(), "RequirementFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);

                        Fragment fragment = getFragmentManager().findFragmentByTag("JobCardDetailFragment");
                        if (fragment != null)
                            fragment.getFragmentManager().beginTransaction().remove(fragment).commit();

                    })
                    .setNegativeButton("Cancel JobCard", (dialogInterface, which) -> {
                        statusForRealm = Constant.CANCELLED;
                        BookingStatusRequest bookingStatusRequest = new BookingStatusRequest();
                        bookingStatusRequest.setId(bookingId);
                        bookingStatusRequest.setStatus("Cancelled");
                        bookingStatusRequest.setRemark("JobCard Cancelled by " + PreferenceManager.getInstance().getStringPreference(getActivity(), Constant.SP_ROLE));
                        detailsPresenter.updateStatus(bookingStatusRequest);
                    })
                    .setNeutralButton("Close", (dialogInterface, which) -> {
                        dialogInterface.dismiss();
                        getActivity().onBackPressed();
                    })
                    .setCancelable(false)
                    .show().setCanceledOnTouchOutside(false);
        }

        if (!detailsResponse.getBookingData().get(0).getRecording_address().equals("")) {
            setUpPlayBack(detailsResponse.getBookingData().get(0).getRecording_address());
            seekBar.setVisibility(View.VISIBLE);
            mPlayback.setVisibility(View.VISIBLE);
        }
    }

    public void onSuccessPaymentLogs(PaymentLogResponse response) {
        if (response.getLogsList().size() > 0) {
            logRecycler.setVisibility(View.VISIBLE);
            transactionLog.setVisibility(View.VISIBLE);
            PaymentLogAdapter paymentLogAdapter = new PaymentLogAdapter(response.getLogsList());
            logRecycler.setAdapter(paymentLogAdapter);
        }
    }

    public void onSuccessStatusUpdate(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
        if(isBooking) realmController.updateBookingStatus(bookingId, statusForRealm);
        else realmController.updateJobsStatus(bookingId, statusForRealm);
        getActivity().onBackPressed();
    }

    public void onSuccessCancelJob(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
        if(isBooking) realmController.updateBookingStatus(bookingId, statusForRealm);
        else realmController.updateJobsStatus(bookingId, statusForRealm);
        getActivity().onBackPressed();
    }

    public void onSuccessBookingRemark(BaseResponse body) {
        Utility.showResponseMessage(mainLayout, body.getResponseMessage());
    }

    public void onSuccessTechnicians(GetTechniciansResponse response) {
        JobsMemberUpdate jobsMemberUpdate = new JobsMemberUpdate();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.KEY_TYPE, true);
        bundle.putString(Constant.BOOKING_ID, bookingId);
        bundle.putSerializable(Constant.VALUE, response.getTechnicians());
        jobsMemberUpdate.setArguments(bundle);
        jobsMemberUpdate.show(getChildFragmentManager(), "JobsMemberUpdate");
    }

    public void onSuccessAdvisors(AdvisorResponse response) {
        JobsMemberUpdate jobsMemberUpdate = new JobsMemberUpdate();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.KEY_TYPE, false);
        bundle.putString(Constant.BOOKING_ID, bookingId);
        bundle.putSerializable(Constant.VALUE, response.getAdvisorsList());
        jobsMemberUpdate.setArguments(bundle);
        jobsMemberUpdate.show(getChildFragmentManager(), "JobsMemberUpdate");
    }

    /*------------------------------------------------------- UTILITY METHODS -------------------------------------------------*/

    private void setUpPrimaryData(BookingDetailsResponse detailsResponse) {
        BookingDataBE data = detailsResponse.getBookingData().get(0);

        if(isBooking) {
            bookingNo.setText("Booking #" + data.getBooking_no());
            mDate.setText(data.getDate() + " - " + data.getTime_slot());
        }
        else {
            bookingNo.setText("Job #" + data.getBooking_no());
            mDate.setText(data.getDelivery_date() + " - " + data.getDelivery_time());
        }

        status.setText(data.getStatus());
        name.setText(data.getUserData().getName());
        convinience.setText(data.getConvenience());

//        if (data.getAddress() == null) address.setVisibility(View.GONE);
//        else address.setText(data.getAddress().getAddress() + " , " + data.getAddress().getCity() + " , " + data.getAddress().getZip());

        if (data.getCarDetail() != null)
            carName.setText(data.getCarDetail().getRegistration_no() + "\n" + data.getCarDetail().getTitle());
        else carName.setVisibility(View.GONE);

        if (detailsResponse.getBookingData().get(0).getInsuranceData() != null && detailsResponse.getBookingData().get(0).getInsuranceData().isClaim()) {

            insuranceCompany.setText("Claim Details");
            //insuranceCompany.setText(data.getInsuranceData().getInsurance_company() + "\n" + data.getInsuranceData().getPolicy_no());
           /* if (popup != null && popup.getMenu().findItem(R.id.add_claim) != null)
                popup.getMenu().findItem(R.id.add_claim).setEnabled(false);*/
        } else {
            insuranceDetail.setVisibility(View.GONE);
          /*  if (popup != null && popup.getMenu().findItem(R.id.surveyor_details) != null)
                popup.getMenu().findItem(R.id.surveyor_details).setEnabled(false);*/
        }


//            if (detailsResponse.getBookingData().get(0).getSurveyor() != null || (!primaryStatus.equals(Constant.ESTIMATION) && !primaryStatus.equals(Constant.IN_PROCESS))) popup.getMenu().findItem(R.id.surveyor_details).setEnabled(false);
//        }

        advisor.setText(data.getAdvisor().getName());
        advisorContact.setText(data.getAdvisor().getContact_no());

        if (data.getTechnician() != null) {
            mTechnicianLayout.setVisibility(View.VISIBLE);
            technician.setText(data.getTechnician().getName());
            technicianContact.setText(data.getTechnician().getContact_no());
        }

        if (data.getDriver() != null) {
            mDriverLayout.setVisibility(View.VISIBLE);
            driver.setText(data.getDriver().getName());
            driverContact.setText(data.getDriver().getContact_no());
        }

        if (data.getSurveyor() != null) {
//            if (popup != null && popup.getMenu().findItem(R.id.surveyor_details) != null)
//                popup.getMenu().findItem(R.id.surveyor_details).setEnabled(false);
            mSurveyorLayout.setVisibility(View.VISIBLE);
            surveyor.setText(data.getSurveyor().getName());
            surveyorContact.setText(data.getSurveyor().getContact_no());
        }

        if (data.getJobsQCBEArrayList().size() > 0) {
            mQCLayout.setVisibility(View.VISIBLE);
            qc.setText("Quality");
            qcContact.setText("Nil");
        }

//        if (data.getSurveyor() != null) {
//            mSurveyorLayout.setVisibility(View.VISIBLE);
//            surveyor.setText(data.getSurveyor().getName());
//            surveyorContact.setText(data.getSurveyor().getContact_no());
//            mViewOption.setVisibility(View.GONE);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statusCallChat.getLayoutParams();
//            params.removeRule(RelativeLayout.LEFT_OF);
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            statusCallChat.setLayoutParams(params);
//        }

    }

    private void setUpPaymentData(BookingDetailsResponse detailsResponse) {

        BookingDataBE data = detailsResponse.getBookingData().get(0);

        labourCost.setText("₹ " +String.format("%.2f",  data.getPayment().getLabour_cost()));
        partCost.setText("₹ " + String.format("%.2f", data.getPayment().getPart_cost()));
        otherCost.setText("₹ " +String.format("%.2f",  data.getPayment().getOf_cost()));
        pickUpCharges.setText("₹ " + String.format("%.2f", data.getPayment().getPick_up_charges()));
        grandTotal.setText("₹ " + String.format("%.2f", data.getPayment().getTotal()));
        paid.setText("(-) ₹ " + String.format("%.2f", data.getPayment().getPaid_total()));

        if(data.getPayment().getCareager_cash()>0) {
            mLLCarEagerCash.setVisibility(View.VISIBLE);
            mCarEagerCash.setText("(-) ₹ " + String.format("%.2f", data.getPayment().getCareager_cash()));
        }
        else {
            mLLCarEagerCash.setVisibility(View.GONE);
        }

        if (data.getDue() != null) {
            due.setText("₹ " +String.format("%.2f", data.getDue().getDue()));
            if (data.getDue().getDue() <= 0) mPaymentReceive.setVisibility(View.GONE);
        } else {
            due.setText("₹ 0.0");
            mPaymentReceive.setVisibility(View.GONE);
        }

        if (data.getPayment().getDiscount_type().equalsIgnoreCase("coupon")) {
            discount.setText("(-) ₹ " + String.format("%.2f",data.getPayment().getDiscount_total()));
            coupon.setText("(Coupon : " + data.getPayment().getCoupon() + ")");
        } else discount.setText("(-) ₹ " + String.format("%.2f",data.getPayment().getDiscount_total()));
    }

    private void segregateData(ArrayList<ServiceBE> service) {
        serviceArrayList = new ArrayList<>();
        estimateArrayList = new ArrayList<>();
        for (ServiceBE data : service) {
            if (data.isCustomer_approval()) serviceArrayList.add(data);
            else estimateArrayList.add(data);
        }
    }

    private void addMenuOptions() {
        menuOptionsList = new ArrayList<>();
        menuOptionsList.add("Update Customer Details");
        menuOptionsList.add("Update Customer Address");
        menuOptionsList.add("Update Car Details");

        if(!isBooking) {
            menuOptionsList.add("Update Technician");
            menuOptionsList.add("Update Advisor");
            menuOptionsList.add("Update Delivery Date & Time");
            menuOptionsList.add("Update Insurance Details");
            menuOptionsList.add("Update Claim Details");
            menuOptionsList.add("Update Surveyor Details");
        }
    }

    private void setUpMenu() {
        popup = new PopupMenu(getActivity(), mViewOption);
        if(!isTechnician) {
            if (primaryStatus.equals(Constant.PENDING)) popup.inflate(R.menu.booking_pending_menu);
            else if (primaryStatus.equals(Constant.CONFIRMED))
                popup.inflate(R.menu.booking_confirmed_menu);
            else if (primaryStatus.equals(Constant.ESTIMATE_REQUESTED))
                popup.inflate(R.menu.booking_confirmed_menu);
            else if (primaryStatus.equals(Constant.JOB_OPEN)) popup.inflate(R.menu.jobcard_menu);
            else if (primaryStatus.equals(Constant.ESTIMATION))
                popup.inflate(R.menu.estimation_menu);
            else if (primaryStatus.equals(Constant.IN_PROCESS) || primaryStatus.equals("StartWork"))
                popup.inflate(R.menu.in_process_menu);
            else if (primaryStatus.equals(Constant.QUALITY_CHECK) || primaryStatus.equals("CompleteWork")) {
                String jsonManifest = PreferenceManager.getInstance().getStringPreference(getActivity(), Constant.SP_MANIFEST);
                try {
                    JSONObject jsonObject = new JSONObject(jsonManifest);
                    Boolean skipQC = jsonObject.getBoolean("skip_insurance_info");
                    if (skipQC) {
                        popup.inflate(R.menu.quality_check_menu_skip);
                    } else {
                        popup.inflate(R.menu.quality_check_menu);

                    }

                } catch (Exception e) {

                }

            } else if (primaryStatus.equals(Constant.STORE_APPROVAL)) {
                String jsonManifest = PreferenceManager.getInstance().getStringPreference(getActivity(), Constant.SP_MANIFEST);
                try {
                    JSONObject jsonObject = new JSONObject(jsonManifest);
                    Boolean skipStore = jsonObject.getBoolean("skip_qc");
                    if (skipStore) {
                        popup.inflate(R.menu.store_approval_menu_skip);
                    } else {
                        popup.inflate(R.menu.store_approval_menu);

                    }

                } catch (Exception e) {

                }


            } else if (primaryStatus.equals(Constant.GM_APPROVAL))
                popup.inflate(R.menu.gm_approval_menu);
            else if (primaryStatus.equals(Constant.READY)) popup.inflate(R.menu.ready_menu);

        }else {
            if (primaryStatus.equals(Constant.IN_PROCESS) || primaryStatus.equals("StartWork"))
                popup.inflate(R.menu.in_process_menu_technician);

        }

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.start_work:
                    statusForRealm = Constant.IN_PROCESS;
                    if (detailsResponse.getBookingData().get(0).getService().size() > 0)
                        confirmationDialog(bookingId, Constant.IN_PROCESS, Constant.START_WORK, "");
                    else Utility.showResponseMessage(mainLayout, "No Services Added");
                    break;

                case R.id.cancel_jobcard:
                    statusForRealm = Constant.CANCELLED;
                    cancelJobConfirmation(bookingId);
                    break;

                case R.id.close_work:
                    statusForRealm = Constant.READY;
                    confirmationDialog(bookingId, Constant.READY, Constant.CLOSE_WORK, "");
                    break;

                case R.id.quality_check:
                    statusForRealm = Constant.QUALITY_CHECK;
                    confirmationDialog(bookingId, Constant.QUALITY_CHECK, Constant.COMPLETE_WORK, "");
                    break;

                case R.id.gate_pass:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(Constant.KEY_TYPE,"http://www.autroid.com/gatepass/"+bookingId);
                    ((AwsHomeActivity) getActivity()).addFragment(WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId);
                    break;

                case R.id.ready:
                    statusForRealm = Constant.READY;
                    confirmationDialog(bookingId, Constant.READY, Constant.READY, "");
                    break;
                case R.id.cancel_booking:
                    statusForRealm = Constant.CANCELLED;
                    cancelJobConfirmation(bookingId);
                    break;
                case R.id.confirm_booking:
                    statusForRealm = Constant.CONFIRMED;
                    cancelJobConfirmation(bookingId);
                    break;
                case R.id.reschedule_booking:
                    BookingRescheduleFragment bookingRescheduleFragment = new BookingRescheduleFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_ID, bookingId);
                    bundle.putBoolean(Constant.IS_CRE,false);
                    bookingRescheduleFragment.setArguments(bundle);
                    bookingRescheduleFragment.show(getChildFragmentManager(), "BookingRescheduleFragment");

                    break;

              /*  case R.id.add_claim:
                    JobCardCollisionFragment jobCardCollisionFragment = new JobCardCollisionFragment();
                    Bundle collisionBundle = new Bundle();
                    collisionBundle.putBoolean(Constant.KEY_TYPE, true);
                    collisionBundle.putString(Constant.BOOKING_ID, bookingId);
                    jobCardCollisionFragment.setArguments(collisionBundle);
                    jobCardCollisionFragment.show(getChildFragmentManager(), "JobCardCollisionFragment");
                    break;*/

//                case R.id.gm_approval:
//                    statusForRealm = Constant.GM_APPROVAL;
//                    confirmationDialog(bookingId, Constant.GM_APPROVAL, Constant.GM_APPROVAL,"");
//                    break;

                case R.id.back_to_qc:
                    statusForRealm = Constant.QUALITY_CHECK;
                    confirmationDialog(bookingId, Constant.QUALITY_CHECK, Constant.COMPLETE_WORK, "enable");
                    break;

//                case R.id.back_to_gm:
//                    statusForRealm = Constant.GM_APPROVAL;
//                    confirmationDialog(bookingId, Constant.GM_APPROVAL, Constant.GM_APPROVAL,"enable");
//                    break;

                case R.id.back_to_store:
                    statusForRealm = Constant.STORE_APPROVAL;
                    confirmationDialog(bookingId, Constant.STORE_APPROVAL, Constant.STORE_APPROVAL, "enable");
                    break;

                case R.id.back_to_in_process:
                    statusForRealm = Constant.IN_PROCESS;
                    confirmationDialog(bookingId, Constant.IN_PROCESS, Constant.START_WORK, "enable");
                    break;

                case R.id.start_quality_check:
                    Bundle qualityBundle = new Bundle();
                    qualityBundle.putString(Constant.BOOKING_ID, bookingId);
                    qualityBundle.putBoolean(Constant.IS_JOBCARD_DETAILS, false);
                    ((AwsHomeActivity)getActivity()).addFragment(new JobsQualityCheckFragment(), "QualityCheckFragment", true, false, qualityBundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                    break;

                case R.id.job_update: {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.jobcard_menu_dialog, null);
                    menuRecycler = view.findViewById(R.id.menu_list);
                    PhrasesAdapter adapter = new PhrasesAdapter(menuOptionsList, this);
                    menuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    menuRecycler.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view);
                   // builder.setTitle("Select an option");

                    alertDialog = builder.create();

                    alertDialog.show();
                }



                break;

                case R.id.update_booking: {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.jobcard_menu_dialog, null);
                    menuRecycler = view.findViewById(R.id.menu_list);
                    PhrasesAdapter adapter = new PhrasesAdapter(menuOptionsList, this);
                    menuRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    menuRecycler.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setView(view);
                    // builder.setTitle("Select an option");

                    alertDialog = builder.create();

                    alertDialog.show();
                }
                break;
                case R.id.skip_qc:
                    statusForRealm = Constant.STORE_APPROVAL;
                    confirmationDialog(bookingId, Constant.STORE_APPROVAL, Constant.STORE_APPROVAL, "enable");
                    break;
                case R.id.skip_store_approval:
                    statusForRealm = Constant.READY;
                    confirmationDialog(bookingId, Constant.READY, Constant.READY, "enable");
                    break;
            }

            return false;
        });
    }

    private void startPlayBack() {
        if (handler == null) handler = new Handler();
//        Nirmana.getInstance().get().load(R.drawable.ic_qiscus_pause_audio).into(mPlayback);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(v -> {
//            Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(mPlayback);
            handler.removeCallbacks(runnable);
        });

        updateSeekBar();
    }

    private void stopPlayBack() {
//        Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(mPlayback);
        mediaPlayer.pause();
    }

    private void setUpPlayBack(String link) {
        if (mediaPlayer == null) { // if api is refreshed from within the screen then setup should not happen again
            mediaPlayer = new MediaPlayer();
            runnable = () -> updateSeekBar();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(link);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    prepared = true;
                    seekBar.setMax(mediaPlayer.getDuration());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.postDelayed(runnable, 1000);
    }

    private void findViews(View view) {
        mPaymentReceive = view.findViewById(R.id.payment_receive);
        mainLayout = view.findViewById(R.id.main_layout);
        bookingNo = view.findViewById(R.id.booking_no);
        call_btn = view.findViewById(R.id.call_btn);
        chat_btn = view.findViewById(R.id.chat_btn);
        status = view.findViewById(R.id.status);
        name = view.findViewById(R.id.name);
        convinience = view.findViewById(R.id.convinience);
        transactionLog = view.findViewById(R.id.transaction_log);
        //address = view.findViewById(R.id.address);
        paid = view.findViewById(R.id.paid);

        mCarEagerCash=view.findViewById(R.id.careager_cash);
        mLLCarEagerCash=view.findViewById(R.id.ll_careager_cash);//

        due = view.findViewById(R.id.due);
        mViewOption = view.findViewById(R.id.textViewOptions);
        labourCost = view.findViewById(R.id.labour_cost);
        otherCost=view.findViewById(R.id.other_cost);

        partCost = view.findViewById(R.id.part_cost);
        pickUpCharges = view.findViewById(R.id.convenience);
        discount = view.findViewById(R.id.discount);
        grandTotal = view.findViewById(R.id.total);
        coupon = view.findViewById(R.id.coupon);
        serviceDetailsRecycler = view.findViewById(R.id.booking_details_recycler);
        serviceDetailsRecycler.setNestedScrollingEnabled(false);
        estimateDetailsRecycler = view.findViewById(R.id.estimate_details_recycler);
        estimateDetailsRecycler.setNestedScrollingEnabled(false);
        mRemarkList = view.findViewById(R.id.remark_list);
        bookingRequirementsRecycler = view.findViewById(R.id.booking_requirements_recycler);
        logRecycler = view.findViewById(R.id.transaction_log_recycler);
        bookingRequirements = view.findViewById(R.id.booking_requirements);
        insuranceCompany = view.findViewById(R.id.insurance_company);

        advisor = view.findViewById(R.id.advisor);
        advisorContact = view.findViewById(R.id.advisor_contact);
        driver = view.findViewById(R.id.driver);
        driverContact = view.findViewById(R.id.driver_contact);
        technician = view.findViewById(R.id.technician);
        technicianContact = view.findViewById(R.id.technician_contact);
        surveyor = view.findViewById(R.id.surveyor);
        surveyorContact = view.findViewById(R.id.surveyor_contact);
        qc = view.findViewById(R.id.qc);
        qcContact = view.findViewById(R.id.qc_contact);


        bookingItems = view.findViewById(R.id.booking_items);
        carName = view.findViewById(R.id.car_name);
        mDate = view.findViewById(R.id.date);

        mSaveRemark = view.findViewById(R.id.save);
        mEditRemark = view.findViewById(R.id.remark);

        mServices = view.findViewById(R.id.services);
        mEstimate = view.findViewById(R.id.estimate);
        mRemarks = view.findViewById(R.id.remarks);
        mPayment = view.findViewById(R.id.payment);
        mRequisite = view.findViewById(R.id.requisite);
        mPlayback = view.findViewById(R.id.playback);
        userDetail = view.findViewById(R.id.user_detail);
        carDetail = view.findViewById(R.id.car_detail);
        insuranceDetail = view.findViewById(R.id.insurance_details);

        mServices.setOnClickListener(this);
        mEstimate.setOnClickListener(this);
        mRemarks.setOnClickListener(this);
        mPayment.setOnClickListener(this);
        mSaveRemark.setOnClickListener(this);
        mRequisite.setOnClickListener(this);
        mPlayback.setOnClickListener(this);
        mViewOption.setOnClickListener(this);
        userDetail.setOnClickListener(this);
        carDetail.setOnClickListener(this);
        insuranceDetail.setOnClickListener(this);

        mLLPayment = view.findViewById(R.id.layout_payment);
        mLLRemarks = view.findViewById(R.id.layout_remarks);
        mAdvisorLayout = view.findViewById(R.id.advisor_layout);
        mDriverLayout = view.findViewById(R.id.driver_layout);
        mTechnicianLayout = view.findViewById(R.id.technician_layout);
        mSurveyorLayout = view.findViewById(R.id.surveyor_layout);
        mQCLayout = view.findViewById(R.id.qc_layout);
        statusCallChat = view.findViewById(R.id.status_call_chat);

        mAdvisorLayout.setOnClickListener(this);
        mDriverLayout.setOnClickListener(this);
        mTechnicianLayout.setOnClickListener(this);
        mSurveyorLayout.setOnClickListener(this);
        mQCLayout.setOnClickListener(this);

        seekBar = view.findViewById(R.id.seekbar);
    }

    private void confirmationDialog(String bookingId, String stage, String statusToUpdate, String enable) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation Message")
                .setMessage("Update the car status ?")
                .setPositiveButton("Confirm", (dialogInterface, which) -> {

                    UpdateStatusRequest updateRequest = new UpdateStatusRequest();
                    updateRequest.setBookingId(bookingId);
                    updateRequest.setStage(stage);
                    updateRequest.setStatusToUpdate(statusToUpdate);
                    updateRequest.setBack(enable);
                    detailsPresenter.updateStatus(updateRequest);

                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show().setCanceledOnTouchOutside(false);

    }

    private void cancelJobConfirmation(String bookingId) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reject_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cancel Remark");
        builder.setView(view);
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());

        builder.setPositiveButton("Submit", (dialogInterface, which) -> {
            EditText text = view.findViewById(R.id.remarks);
            if (text.getText().toString().trim().length() != 0) {
                BookingStatusRequest bookingStatusRequest = new BookingStatusRequest();
                bookingStatusRequest.setId(bookingId);
                bookingStatusRequest.setStatus("Cancelled");
                bookingStatusRequest.setRemark(text.getText().toString().trim());
                detailsPresenter.updateStatus(bookingStatusRequest);
            } else Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_SHORT).show();
        });

        alertDialog = builder.create();

        alertDialog.show();
    }

    @Override
    public void onServiceClick(ServiceBE serviceBE) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_TYPE, serviceBE);
        ((AwsHomeActivity) getActivity()).addFragment(new BookingServiceDetailFragment(), "BookingServiceDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    /*----------------------------------------------------- EVENT RECEIVING ---------------------------------------------------*/

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_UPDATE) detailsPresenter.getBookingDetails(bookingId, "id");
    }

    @Override
    public void onDocumentClick(Uri downloadUri, String sender, String attachmentName) {

    }

    @Override
    public void onImageClick(Uri uri) {

    }

    @Override
    public void onMapClick(String address) {

    }

    @Override
    public void onLinkClick(String url) {

    }

    @Override
    public void onPhrasesClick(int position) {

        alertDialog.dismiss();

        switch (position) {

            case 0: {
                if (detailsResponse != null) {
                    JobCustomerUpdateFragment updateFragment = new JobCustomerUpdateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.USER_DATA, detailsResponse.getBookingData().get(0).getUserData());
                    updateFragment.setArguments(bundle);
                    updateFragment.show(getChildFragmentManager(), "JobCustomerUpdateFragment");
                }
            }
            break;

            case 1:
                if (detailsResponse != null) {
                    JobCardAddressFragment addressFragment = new JobCardAddressFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BOOKING_ID, bookingId);
                    bundle.putString(Constant.USER_ID, detailsResponse.getBookingData().get(0).getUserData().getId());
                    bundle.putBoolean(Constant.KEY_TYPE, true);
                    addressFragment.setArguments(bundle);
                    addressFragment.show(getChildFragmentManager(), "AddressFragment");
                }
                break;

            case 2: {
                Bundle carBundle = new Bundle();
                detailsResponse.getBookingData().get(0).getCarDetail().setOdometer(detailsResponse.getBookingData().get(0).getOdometer()+"");
                detailsResponse.getBookingData().get(0).getCarDetail().setFuel_type(detailsResponse.getBookingData().get(0).getFuel()+"");
                carBundle.putSerializable(Constant.CAR_DETAILS, detailsResponse.getBookingData().get(0).getCarDetail());
                carBundle.putString(Constant.BOOKING_ID, bookingId);
                carBundle.putString(Constant.USER_ID, detailsResponse.getBookingData().get(0).getUserData().getId());
                carBundle.putString(Constant.KEY_VARIANT_ID, detailsResponse.getBookingData().get(0).getCarDetail().getVariant());
                carBundle.putString(Constant.KEY_CAR_ID, detailsResponse.getBookingData().get(0).getCarDetail().getId());


                JobCarUpdateFragment carUpdateFragment = new JobCarUpdateFragment();
                carUpdateFragment.setArguments(carBundle);
                carUpdateFragment.show(getChildFragmentManager(), "JobsCarUpdate");
            }
            break;
            case 3:
                detailsPresenter.getTechnicians();
                break;

            case 4:
                detailsPresenter.getAdvisors();
                break;

            case 5:
                if (detailsResponse != null) {
                    JobsDateTimeUpdate updateFragment = new JobsDateTimeUpdate();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BOOKING_ID, bookingId);
                    bundle.putString(Constant.DATE, detailsResponse.getBookingData().get(0).getDelivery_date());
                    bundle.putString(Constant.TIME, detailsResponse.getBookingData().get(0).getDelivery_time());
                    updateFragment.setArguments(bundle);
                    updateFragment.show(getChildFragmentManager(), "JobDateTimeUpdateFragment");
                }
                break;

            case 6: {
                Bundle insuranceBundle = new Bundle();
                insuranceBundle.putSerializable(Constant.INSURANCE_DETAILS, detailsResponse.getBookingData().get(0).getInsuranceData());
                insuranceBundle.putString(Constant.BOOKING_ID, bookingId);
                JobsInsuranceUpdate jobsInsuranceUpdate = new JobsInsuranceUpdate();
                jobsInsuranceUpdate.setArguments(insuranceBundle);
                jobsInsuranceUpdate.show(getChildFragmentManager(), "JobsInsuranceUpdate");
            }
            break;

            case 7: {
                JobsAccidentDetailsUpdate update = new JobsAccidentDetailsUpdate();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKING_ID, bookingId);
                bundle.putSerializable(Constant.VALUE, detailsResponse.getBookingData().get(0).getInsuranceData());
                update.setArguments(bundle);
                update.show(getChildFragmentManager(), "Accident Update");
            }
            break;
            case 8: {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, bookingId);
                if (detailsResponse.getBookingData().get(0).getSurveyor() != null) {
                    bundle.putBoolean(Constant.VALUE, true);
                    bundle.putSerializable(Constant.DETAILS_TYPE, detailsResponse.getBookingData().get(0).getSurveyor());
                } else bundle.putBoolean(Constant.VALUE, false);

                ((AwsHomeActivity)getActivity()).addFragment(new JobCardSurveyorFragment(), "SurveyorFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
            }
            break;

        }
    }

    private void hideForTechnician(){
        userDetail.setVisibility(View.GONE);
        insuranceDetail.setVisibility(View.GONE);
        mEstimate.setVisibility(View.GONE);
        mPayment.setVisibility(View.GONE);


    }
}
