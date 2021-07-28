package autroid.business.view.fragment.booking;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.AdvisorBE;
import autroid.business.model.bean.BookingsBE;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.realm.LeadBookingRealm;
import autroid.business.model.realm.LeadsAssignedRealm;
import autroid.business.model.request.JobCardBookingRequest;
import autroid.business.model.request.LeadBookingRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.model.response.LeadCarsResponse;
import autroid.business.presenter.ManualPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.jobcard.JobCardCarSelectionFragment;
import autroid.business.view.fragment.search.SearchCarFragment;
import io.realm.Realm;

public class ManualBookingFragment extends Fragment {

    TextView customer_name,title;

    EditText carMaker,carRegNo1,vinNo,engNo,requirement;

    ImageView editCar;

    String userId = "", carId = "", variantId = "",leadId="",advisorId="";

    Button proceed_btn;

    ArrayList<AdvisorBE> advisorsList;

    ManualPresenter manualPresenter;

    LinearLayout linearLayout,fieldsHolder,advisorSpinnerLayout,engVinHolder;

    ArrayList<CarDetailBE> carDetails;

    boolean leadEstimate,fromLeadList,fromLeadAssigned;

    AppCompatSpinner advisorSpinner;

    Realm mRealm;

    LeadsAssignedRealm leadsAssignedRealm;

    RealmController realmController;

    public ManualBookingFragment(){}

    /* ----------------------------------------------------------- BASIC METHODS -------------------------------------------------------*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.manual_fragment, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());

        customer_name = view.findViewById(R.id.optional_text);
        customer_name.setVisibility(View.VISIBLE);
        title=view.findViewById(R.id.toolbar_title);
        linearLayout=view.findViewById(R.id.linear);
        fieldsHolder=view.findViewById(R.id.fields_holder);
        carMaker=view.findViewById(R.id.car_title);
        carRegNo1=view.findViewById(R.id.car_registration_no_1);
        vinNo=view.findViewById(R.id.jobcard_car_vin);
        engNo=view.findViewById(R.id.jobcard_engine_no);
        editCar=view.findViewById(R.id.edit_car);
        proceed_btn = view.findViewById(R.id.jobcard_proceed);
        advisorSpinnerLayout = view.findViewById(R.id.advisor_spinner_layout);
        advisorSpinner= view.findViewById(R.id.advisor_spinner);
        engVinHolder= view.findViewById(R.id.eng_vin_holder);
        requirement= view.findViewById(R.id.requirement);

        title.setText("Car Details");
        customer_name.setText(getArguments().getString(Constant.USER_NAME));

        carMaker.setEnabled(false);
        carDetails=new ArrayList<>();
        advisorsList=new ArrayList<>();

        disableViews();
        getBundleData();

        editCar.setOnClickListener(v->{

            if (fieldsHolder.getVisibility() == View.VISIBLE) {
                fieldsHolder.setVisibility(View.GONE);
                carMaker.setText("");
                carRegNo1.setText("");
                carRegNo1.setEnabled(false);

            }

            if (carDetails.size() == 0) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IS_MANUAL, true);
                ((AwsHomeActivity) getActivity()).addFragment(new SearchCarFragment(), "SearchCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.CAR_DETAILS, carDetails);
                bundle.putBoolean(Constant.IS_MANUAL, true);
                ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
            }
        });

       /* carRegNo1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (carRegNo1.getText().toString().trim().length() >= 2) {
                    carRegNo1.clearFocus();
                    carRegNo2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        carRegNo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (carRegNo2.getText().toString().trim().length() >= 2) {
                    carRegNo2.clearFocus();
                    carRegNo3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        carRegNo3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (carRegNo3.getText().toString().trim().length() >= 2) {
                    carRegNo3.clearFocus();
                    carRegNo4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        proceed_btn.setOnClickListener(v->{
            ((AwsHomeActivity) getActivity()).disableButton(proceed_btn);
            if(!leadEstimate && validate(carMaker.getText().toString().trim(),requirement.getText().toString().trim()) && validateRegNo(carRegNo1.getText().toString().trim())){
                manualPresenter.addManualBooking(createBookingRequest());
            }else if(validate(carMaker.getText().toString().trim(),requirement.getText().toString().trim()) && validateRegNo(carRegNo1.getText().toString().trim())){
                manualPresenter.leadBookingAdd(createLeadBookingRequest());
            }
        });

    }

    /* ---------------------------------------------------------- API RESPONSES ------------------------------------------------------*/

    public void onSuccessCarData(GetUserCarResponse response) {
        for (CarDetailBE detailBE : response.getGetCarDetails()) carDetails.add(detailBE);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CAR_DETAILS, carDetails);
        bundle.putBoolean(Constant.IS_MANUAL,true);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public void onSuccessEmptyCarData() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_MANUAL, true);
        ((AwsHomeActivity) getActivity()).addFragment(new SearchCarFragment(), "SearchFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public void onSuccessLeadCarData(LeadCarsResponse response) {
        for (CarDetailBE detailBE : response.getCars()) carDetails.add(detailBE);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.CAR_DETAILS, carDetails);
        bundle.putBoolean(Constant.IS_MANUAL,true);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public void onSuccessEmptyLeadCarData() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_MANUAL, true);
        ((AwsHomeActivity) getActivity()).addFragment(new SearchCarFragment(), "SearchFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public void onSuccessAddLeadBooking(BookingsResponse response) {

        FragmentManager fm=getFragmentManager();
        int curentIndex=fm.getBackStackEntryCount()-1;

        if(fromLeadAssigned){
            realmController.updateAssignedLead(leadId,"Assigned","EstimateRequested","","","");
            for (BookingsBE data : response.getGetBookings()) {
                BookingRealm bookingRealm = new BookingRealm();
                bookingRealm.setBookingId(data.getId());
                bookingRealm.setVehicleTitle(data.getCar().getTitle());
                bookingRealm.setConvenience(data.getConvenience());
                bookingRealm.setDated(data.getDate());
                bookingRealm.setPrice(data.getPayment().getTotal());
                bookingRealm.setPaidTotal(data.getPayment().getPaid_total());
                bookingRealm.setCoupon(data.getPayment().getCoupon());
                bookingRealm.setDiscount_type(data.getPayment().getDiscount_type());
                bookingRealm.setDiscount(data.getPayment().getDiscount());
                bookingRealm.setProviderName(data.getUser().getName());
                bookingRealm.setShortId(data.getBooking_no());
                bookingRealm.setTimeSlot(data.getTime_slot());
                bookingRealm.setRegistrationNumber(data.getCar().getRegistration_no());
                bookingRealm.setmUserName(data.getUser().getName());
                bookingRealm.setmUserNumber(data.getUser().getContact_no());
                bookingRealm.setmUserId(data.getUser().getId());
                bookingRealm.setStatus(Constant.APPROVAL);

                mRealm.beginTransaction();
                mRealm.copyToRealm(bookingRealm);
                mRealm.commitTransaction();
            }

        }
        else{
            realmController.updateLead(leadId,"EstimateRequested","EstimateRequested","","","");
            for (BookingsBE data : response.getGetBookings()) {
                LeadBookingRealm leadBookingRealm = new LeadBookingRealm();
                leadBookingRealm.setId(data.getId());
                leadBookingRealm.setName(data.getUser().getName());
                leadBookingRealm.setEmail(data.getUser().getEmail());
                leadBookingRealm.setContactNo(data.getUser().getContact_no());
                leadBookingRealm.setStatus(data.getStatus());
                leadBookingRealm.setBookingDate(data.getDate());
                leadBookingRealm.setBookingTime(data.getTime_slot());
                leadBookingRealm.setCreatedAt(data.getCreated_at());
                leadBookingRealm.setUpdatedAt(data.getUpdated_at());
                leadBookingRealm.setConvenience(data.getConvenience());
                leadBookingRealm.setBookingNo(data.getBooking_no());
                leadBookingRealm.setCarName(data.getCar().getTitle());
                leadBookingRealm.setRegistrationNo(data.getCar().getRegistration_no());

                mRealm.beginTransaction();
                mRealm.copyToRealm(leadBookingRealm);
                mRealm.commitTransaction();
            }
        }

        if(fm.getBackStackEntryAt(curentIndex).getName().equalsIgnoreCase("ManualBookingFragment"))fm.popBackStack();

        if(fm.getBackStackEntryAt(curentIndex-1).getName().equalsIgnoreCase("JobCardUserFragment"))fm.popBackStack();

        if(fm.getBackStackEntryAt(curentIndex-1).getName().equalsIgnoreCase("LeadDetailFragment"))fm.popBackStack();


        // getActivity().onBackPressed();
    }

    public void onSuccessAdvisors(AdvisorResponse response) {

        AdvisorBE advisor = new AdvisorBE();
        advisor.setId("");
        advisor.setName("Select Advisor");
        advisor.setContact_no("");
        advisor.setEmail("");

        advisorsList.add(advisor);
        advisorsList.addAll(response.getAdvisorsList());

        ArrayList<String> advisorNames = new ArrayList<>();
        for (AdvisorBE data : advisorsList) advisorNames.add(data.getName());

        ArrayAdapter<String> advisorAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, advisorNames);
        advisorAdapter.setDropDownViewResource(R.layout.layout_spinner_remark);
        advisorSpinner.setAdapter(advisorAdapter);

        advisorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                advisorId = advisorsList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onSuccessAddBooking(BookingsResponse response) {

        FragmentManager fm=getFragmentManager();
        int curentIndex=fm.getBackStackEntryCount()-1;

        for (BookingsBE data : response.getGetBookings()) {
            BookingRealm bookingRealm = new BookingRealm();
            bookingRealm.setBookingId(data.getId());
            bookingRealm.setVehicleTitle(data.getCar().getTitle());
            bookingRealm.setConvenience(data.getConvenience());
            bookingRealm.setDated(data.getDate());
            bookingRealm.setPrice(data.getPayment().getTotal());
            bookingRealm.setPaidTotal(data.getPayment().getPaid_total());
            bookingRealm.setCoupon(data.getPayment().getCoupon());
            bookingRealm.setDiscount_type(data.getPayment().getDiscount_type());
            bookingRealm.setDiscount(data.getPayment().getDiscount());
            bookingRealm.setProviderName(data.getUser().getName());
            bookingRealm.setShortId(data.getBooking_no());
            bookingRealm.setTimeSlot(data.getTime_slot());
            bookingRealm.setRegistrationNumber(data.getCar().getRegistration_no());
            bookingRealm.setmUserName(data.getUser().getName());
            bookingRealm.setmUserNumber(data.getUser().getContact_no());
            bookingRealm.setmUserId(data.getUser().getId());
            bookingRealm.setPrimaryStatus(Constant.APPROVAL);
            bookingRealm.setStatus(Constant.ESTIMATE_REQUESTED);

            mRealm.beginTransaction();
            mRealm.copyToRealm(bookingRealm);
            mRealm.commitTransaction();

//            getActivity().onBackPressed();

//            Fragment manualFragment = getFragmentManager().findFragmentByTag("ManualBookingFragment");
//            if (manualFragment!=null)manualFragment.getFragmentManager().beginTransaction().remove(manualFragment).commit();
//
//            Fragment userFragment = getFragmentManager().findFragmentByTag("JobCardUserFragment");
//            if (userFragment!=null)userFragment.getFragmentManager().beginTransaction().remove(userFragment).commit();
        }

        if(fm.getBackStackEntryAt(curentIndex).getName().equalsIgnoreCase("ManualBookingFragment"))fm.popBackStack();

        if(fm.getBackStackEntryAt(curentIndex-1).getName().equalsIgnoreCase("JobCardUserFragment"))fm.popBackStack();

        if(fm.getBackStackEntryAt(curentIndex-1).getName().equalsIgnoreCase("LeadDetailFragment"))fm.popBackStack();

    }

    /* ------------------------------------------------------------ WHEN CAR SELECTED ------------------------------------------------*/

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_POST_MANUAL_CAR) {
            carId = null;
            variantId = intent.getStringExtra("variant_id");
            carMaker.setText(intent.getStringExtra("variant_name"));
            fieldsHolder.setVisibility(View.VISIBLE);
            setEmpty();
            enable(carRegNo1);
            carRegNo1.requestFocus();
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SEND_MANUAL_CAR) {
            fieldsHolder.setVisibility(View.VISIBLE);
            setUpFeilds(intent);
            disableViews();
            carRegNo1.requestFocus();
        }
    }

    /* ---------------------------------------------------------- UTILITY METHODS ----------------------------------------------------*/

    private void setRegistrationNo(String reg_no) {
        carRegNo1.setText(reg_no);

      /*  String registration[] = reg_no.split(" ");
        if (registration.length == 4) {

            carRegNo2.setText(registration[1]);
            carRegNo3.setText(registration[2]);
            carRegNo4.setText(registration[3]);
        }*/
    }

    public void setEmpty() {
        carRegNo1.setText("");
        vinNo.setText("");
        engNo.setText("");
    }


    public void setUpFeilds(Intent intent) {
        carId = intent.getStringExtra("car_id");
        variantId = intent.getStringExtra("variant_id");
        carMaker.setText(intent.getStringExtra("variant_name"));
        setRegistrationNo(intent.getStringExtra("reg_no"));
        vinNo.setText(intent.getStringExtra("vin_no"));
        engNo.setText(intent.getStringExtra("eng_no"));
    }

    private void disableViews() {
        carRegNo1.setEnabled(false);
       /* carRegNo2.setEnabled(false);
        carRegNo3.setEnabled(false);
        carRegNo4.setEnabled(false);*/
    }

    public void enable(EditText editText) {
        if (!editText.isEnabled()) editText.setEnabled(true);
    }

    private String sendRegistrationNumber() {
        return carRegNo1.getText().toString().trim();
    }

    private boolean validateRegNo(String reg_no1) {
        if (reg_no1.length() < 0) {
            Utility.showResponseMessage(linearLayout, "Invalid Registration Number");
            return false;
        }

        return true;
    }

    private boolean validate(String model_no,String requirement) {

        if (model_no.length() == 0) {
            Utility.showResponseMessage(linearLayout, "Invalid Car");
            return false;
        }else if(leadEstimate && advisorId.length()==0){
            Utility.showResponseMessage(linearLayout, "Please select advisor");
            return false;
        }else if(requirement.length()==0){
            Utility.showResponseMessage(linearLayout, "Please enter remark");
            return false;
        }

        return true;
    }


    /* ------------------------------------------------------- REQUEST TO SERVER ---------------------------------------------*/

    public JobCardBookingRequest createBookingRequest() {
        JobCardBookingRequest jobCarBookingRequest = new JobCardBookingRequest();
        jobCarBookingRequest.setBooking("");
        jobCarBookingRequest.setCar(carId);
        jobCarBookingRequest.setVariant(variantId);
        jobCarBookingRequest.setUser(userId);
        jobCarBookingRequest.setRegistration_no(sendRegistrationNumber());
        jobCarBookingRequest.setRequirement(requirement.getText().toString().trim());
        jobCarBookingRequest.setOdometer(0);
        jobCarBookingRequest.setFuel_level(0);
        jobCarBookingRequest.setVin(vinNo.getText().toString().trim());
        jobCarBookingRequest.setEngine_no(engNo.getText().toString().trim());
        jobCarBookingRequest.setInsurance_company("");
        jobCarBookingRequest.setPolicy_holder("");
        jobCarBookingRequest.setPolicy_no("");
        jobCarBookingRequest.setPremium(0);
        jobCarBookingRequest.setExpire("");

        return jobCarBookingRequest;
    }

    public LeadBookingRequest createLeadBookingRequest(){

        LeadBookingRequest leadBookingRequest=new LeadBookingRequest();
        leadBookingRequest.setAdvisor(advisorId);
        leadBookingRequest.setEstimation_requested("Yes");
        leadBookingRequest.setLead(leadId);
        leadBookingRequest.setRegistration_no(sendRegistrationNumber());
        leadBookingRequest.setVariant(variantId);
        leadBookingRequest.setRequirement(requirement.getText().toString().trim());

        return leadBookingRequest;

    }

    /* ----------------------------------------------------------- API CALLING -------------------------------------------------------*/

    private void getBundleData() {

        manualPresenter=new ManualPresenter(this,linearLayout);

        if(getArguments().getBoolean(Constant.IS_LEAD)){
            leadEstimate=true;
            if(getArguments().get(Constant.VALUE).equals("LeadList"))fromLeadList=true;
            else if(getArguments().get(Constant.VALUE).equals("LeadAssigned"))fromLeadAssigned=true;
            engVinHolder.setVisibility(View.GONE);
            leadId=getArguments().getString(Constant.KEY_ID);
            advisorId=getArguments().getString(Constant.ADVISOR_ID);
            if(advisorId.trim().length()==0)advisorSpinnerLayout.setVisibility(View.VISIBLE);
            manualPresenter.getAdvisor();
            manualPresenter.getCarsByLead(leadId);
        }else {
            userId=getArguments().getString(Constant.USER_ID);
            manualPresenter.getUserCars(userId);
            advisorSpinnerLayout.setVisibility(View.GONE);
        }

    }
}
