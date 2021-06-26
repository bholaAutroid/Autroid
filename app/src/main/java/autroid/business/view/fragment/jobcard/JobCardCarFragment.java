package autroid.business.view.fragment.jobcard;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.camera.CameraFragment;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.bean.CompanyDataBE;
import autroid.business.model.request.JobCardBookingRequest;
import autroid.business.model.response.GetCompanyResponse;
import autroid.business.model.response.GetUserBookingResponse;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.presenter.jobcard.JobCardCarPresenter;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.search.SearchCarFragment;


public class JobCardCarFragment extends Fragment implements DatePickerDialog.OnDateSetListener, SeekBar.OnSeekBarChangeListener {

    TextView title, customer_name, fuel_textview, expiry_date, car_maker;

    EditText car_vin_no, car_eng_no,odometer;

    AppCompatAutoCompleteTextView comapany_name;

    EditText carRegistration;

    ImageView edit_car;

    LinearLayout linearLayout, fields_holder;

    Button proceed_btn;

    ArrayList<CarDetailBE> carDetails;

    Bundle bundle;

    JobCardCarPresenter jobCardCarPresenter;

    int currentDay, currentMonth, currentYear, progress = 50;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    String booking_id = "", user_id = "", car_id = "", variant_id = "";

    SeekBar fuel;

    boolean booking_exists = false,mutex=false;

    public JobCardCarFragment() {}

    /* ----------------------------------------------------------- BASIC METHODS -------------------------------------------------------*/

    @Override
    public void onResume() {
        super.onResume();
        Utility.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_card_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = view.findViewById(R.id.toolbar_title);
        customer_name = view.findViewById(R.id.optional_text);
        customer_name.setVisibility(View.VISIBLE);

        title.setText("Car Details");

        GlobalBus.getBus().register(this);
        Utility.hideSoftKeyboard(getActivity());

        viewFinding(view);
        infoFromBundle();

        disableViews();

        car_maker.setEnabled(false);
        carDetails = new ArrayList<>();

        expiry_date.setOnClickListener(v -> {
            ((HomeScreen) getActivity()).disableTextview(expiry_date);
            calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
            datePickerDialog.show();
        });

        edit_car.setOnClickListener(v -> {

            if (fields_holder.getVisibility() == View.VISIBLE) {
                fields_holder.setVisibility(View.GONE);
                car_maker.setText("");
                carRegistration.setText("");
                carRegistration.setEnabled(false);
            }

            if (carDetails.size() == 0) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("car_details", true);
                ((HomeScreen) getActivity()).addFragment(new SearchCarFragment(), "SearchCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("car_details", carDetails);
                ((HomeScreen) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            }
        });

        fuel.setOnSeekBarChangeListener(this);
        
        proceed_btn.setOnClickListener(v -> {
            ((HomeScreen) getActivity()).disableButton(proceed_btn);
            if (booking_exists && validateRegNo(carRegistration.getText().toString().trim()) && validateOthers(car_maker.getText().toString().trim(), car_vin_no.getText().toString().trim(), car_eng_no.getText().toString().trim(),odometer.getText().toString().trim()) && validateInsuranceInfo(comapany_name.getText().toString().trim(),expiry_date.getText().toString().trim())) {
                jobCardCarPresenter.addBookingJob(createBookingRequest());
            } else if (validateRegNo(carRegistration.getText().toString().trim()) && validateOthers(car_maker.getText().toString().trim(), car_vin_no.getText().toString().trim(), car_eng_no.getText().toString().trim(),odometer.getText().toString().trim()) && validateInsuranceInfo(comapany_name.getText().toString().trim(),expiry_date.getText().toString().trim())) {
                jobCardCarPresenter.addBooking(createBookingRequest());
            }
        });

    }

    /* ---------------------------------------------------------- API RESPONSES ------------------------------------------------------*/

    public void onSuccessCarData(GetUserCarResponse getUserCarResponse) {
        for (CarDetailBE detailBE : getUserCarResponse.getGetCarDetails()) carDetails.add(detailBE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("car_details", carDetails);
        ((HomeScreen) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }

    public void onSuccessEmptyCarData() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("car_details", true);
        ((HomeScreen) getActivity()).addFragment(new SearchCarFragment(), "SearchFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }

    public void onSuccessAddBooking(GetUserBookingResponse getUserBookingResponse) {
        disableViews();
        booking_id = getUserBookingResponse.getBookingCreatedDetailsBE().getBookingId();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BOOKING_ID, booking_id);
        bundle.putString(Constant.USER_ID,user_id);
        bundle.putBoolean(Constant.IS_JOBCARD,true);
        String jsonManifest= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_MANIFEST);
        try {
            JSONObject jsonObject = new JSONObject(jsonManifest);
            int picLimit=jsonObject.getInt("job_inspection_limit");
            if(picLimit>0){
                ((HomeScreen) getActivity()).addFragment(new CameraFragment(), "CameraFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            }
            else {
                JobCardAddressFragment jobCardAddressFragment = new JobCardAddressFragment();
                jobCardAddressFragment.setArguments(bundle);
                jobCardAddressFragment.show(getFragmentManager(), "AddressFragment");

            }

        }catch (Exception e){

        }    }

    public void onSuccessBookingJob(GetUserBookingResponse bookingResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BOOKING_ID, booking_id);
        bundle.putString(Constant.USER_ID,user_id);
        bundle.putBoolean(Constant.IS_JOBCARD,true);

        String jsonManifest= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_MANIFEST);
        try {
            JSONObject jsonObject = new JSONObject(jsonManifest);
            int picLimit=jsonObject.getInt("job_inspection_limit");
            if(picLimit>0){
                ((HomeScreen) getActivity()).addFragment(new CameraFragment(), "CameraFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            }
            else {

                    JobCardAddressFragment jobCardAddressFragment = new JobCardAddressFragment();
                    jobCardAddressFragment.setArguments(bundle);
                    jobCardAddressFragment.show(getFragmentManager(), "AddressFragment");

            }

        }catch (Exception e){

        }
    }

    public void onSuccessCompanyName(GetCompanyResponse companyResponse) {
        ArrayList<String> comapanyNames = new ArrayList<>();
        for (CompanyDataBE data : companyResponse.getCompanyResponse()) comapanyNames.add(data.getCompanyName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, comapanyNames);
        comapany_name.setAdapter(arrayAdapter);
    }

    public void onSuccessBookingCar(GetUserCarResponse carResponse) {
        CarDetailBE carDetailBE = carResponse.getGetCarDetails().get(0);
        edit_car.setVisibility(View.GONE);
        fields_holder.setVisibility(View.VISIBLE);
        car_id = carDetailBE.getId();
        variant_id = carDetailBE.getVariant();
        customer_name.setText(carDetailBE.getUser().getName());
        car_maker.setText(carDetailBE.getTitle());
        setRegistrationNo(carDetailBE.getRegistration_no());
        car_vin_no.setText(carDetailBE.getVin());
        car_eng_no.setText(carDetailBE.getEngine_no());
        if (carDetailBE.getInsuranceDataBE() != null) {
          //  policy_holder.setText(carDetailBE.getInsuranceDataBE().getPolicy_holder());
            comapany_name.setText(carDetailBE.getInsuranceDataBE().getInsurance_company());
          //  policy_no.setText(carDetailBE.getInsuranceDataBE().getPolicy_no());
            if(carDetailBE.getInsuranceDataBE().getExpire()!=null)expiry_date.setText(carDetailBE.getInsuranceDataBE().getExpire().substring(0,10));
            else expiry_date.setText("");
          //  premium.setText(Integer.toString(carDetailBE.getInsuranceDataBE().getPremium()));
        }
    }


    public void onError(String errorMessage) {
        Utility.showResponseMessage(linearLayout,errorMessage);
    }

    /* ------------------------------------------------------------ VALIDATIONS ------------------------------------------------------*/

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date sourceDate = null;
        String targetdatevalue = null;
        try {
            sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetdatevalue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        expiry_date.setText(targetdatevalue);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    private boolean validateRegNo(String registration) {
        if (registration.length() == 0) {
            Utility.showResponseMessage(linearLayout, "Invalid Registration Number");
            return false;
        }
        return true;
    }

    private boolean validateOthers(String model_no, String vin_no, String eng_no, String odometer) {
        if (model_no.length() == 0) {
            Utility.showResponseMessage(linearLayout, "Invalid Car");
            return false;
        }else if(odometer.length()==0){
            Utility.showResponseMessage(linearLayout, "Invalid Odometer");
            return false;
        } /*else if (vin_no.length() == 0) {
            Utility.showResponseMessage(linearLayout, "Invalid VIN Number");
            return false;
        } else if (eng_no.length() == 0) {
            Utility.showResponseMessage(linearLayout, "Invalid Engine Number");
            return false;
        }
*/
        return true;
    }

    private boolean validateInsuranceInfo(String insuranceCompany,String expire) {

        if(insuranceCompany.length()!=0  && expire.length()!=0){
            mutex=false;
            return true;
        }else if(insuranceCompany.length()==0 && expire.length()==0){
            mutex=true;
            return true;
        }else Utility.showResponseMessage(linearLayout,"Fill all insurance details");

        return false;
    }


    /* ----------------------------------- WHEN CAR SELECTED ------------------------------------------------*/

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_POST_CAR) {
            car_id = null;
            variant_id = intent.getStringExtra("variant_id");
            car_maker.setText(intent.getStringExtra("variant_name"));
            fields_holder.setVisibility(View.VISIBLE);
            setEmpty();
            enable(carRegistration);

            carRegistration.requestFocus();

        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SEND_CAR) {
            fields_holder.setVisibility(View.VISIBLE);
            setUpFeilds(intent);
            disableViews();
            carRegistration.requestFocus();
        }
    }

    /* ------------------------------------------------------- SEEKBAR METHODS -------------------------------------------------------*/

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        this.progress = progress;
        fuel_textview.setText("Fuel is : " + progress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /* ------------------------------------------------------- REQUEST TO SERVER ---------------------------------------------*/

    public JobCardBookingRequest createBookingRequest() {
        JobCardBookingRequest jobCarBookingRequest = new JobCardBookingRequest();
        jobCarBookingRequest.setBooking(booking_id);
        jobCarBookingRequest.setCar(car_id);
        jobCarBookingRequest.setVariant(variant_id);
        jobCarBookingRequest.setUser(user_id);
        jobCarBookingRequest.setRegistration_no(sendRegistrationNumber());
        jobCarBookingRequest.setOdometer(Integer.valueOf(odometer.getText().toString().trim()));
        jobCarBookingRequest.setFuel_level(progress);
        jobCarBookingRequest.setVin(car_vin_no.getText().toString().trim());
        jobCarBookingRequest.setEngine_no(car_eng_no.getText().toString().trim());
        if(!mutex){
            jobCarBookingRequest.setInsurance_company(comapany_name.getText().toString().trim());
            jobCarBookingRequest.setPremium(0);
        }else {
            jobCarBookingRequest.setInsurance_company("");
            jobCarBookingRequest.setPremium(0);
        }

        jobCarBookingRequest.setPolicy_holder("");
        jobCarBookingRequest.setPolicy_no("");
        jobCarBookingRequest.setExpire(expiry_date.getText().toString().trim());

        return jobCarBookingRequest;
    }

    /* ---------------------------------------------------------- UTILITY METHODS ----------------------------------------------------*/

    private void setRegistrationNo(String reg_no) {
            carRegistration.setText(reg_no);
    }

    private String sendRegistrationNumber() {
        return carRegistration.getText().toString().trim().toUpperCase();
    }

    private void viewFinding(View view) {
        proceed_btn = view.findViewById(R.id.jobcard_proceed);
        fields_holder = view.findViewById(R.id.fields_holder);
        car_vin_no = view.findViewById(R.id.jobcard_car_vin);
        car_eng_no = view.findViewById(R.id.jobcard_engine_no);
        car_maker = view.findViewById(R.id.car_title);
        carRegistration = view.findViewById(R.id.car_registration);
        fuel_textview = view.findViewById(R.id.fuel_textview);
        linearLayout = view.findViewById(R.id.linear);
        odometer = view.findViewById(R.id.odometer);

        expiry_date = view.findViewById(R.id.jobcard_expiry_date);
        comapany_name = view.findViewById(R.id.jobcard_company_name);
        edit_car = view.findViewById(R.id.edit_car);

        fuel = view.findViewById(R.id.jobcard_fuel);
        fuel.setProgress(50);
        car_vin_no.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    private void disableViews() {
        carRegistration.setEnabled(false);
    }

    public void enable(EditText editText) {
        if (!editText.isEnabled()) {
            editText.setEnabled(true);
        }
    }

    public void setEmpty() {
        carRegistration.setText("");
        car_vin_no.setText("");
        car_eng_no.setText("");
        comapany_name.setText("");

        expiry_date.setText("");
    }

    public void setUpFeilds(Intent intent) {
        car_id = intent.getStringExtra("car_id");
        variant_id = intent.getStringExtra("variant_id");
        car_maker.setText(intent.getStringExtra("variant_name"));
        setRegistrationNo(intent.getStringExtra("reg_no"));
        car_vin_no.setText(intent.getStringExtra("vin_no"));
        car_eng_no.setText(intent.getStringExtra("eng_no"));
        comapany_name.setText(intent.getStringExtra("company"));
        expiry_date.setText(intent.getStringExtra("expire").substring(0,10));
    }

    /* ----------------------------------------------------------- API CALLING -------------------------------------------------------*/

    private void infoFromBundle() {
        bundle = getArguments();
        jobCardCarPresenter = new JobCardCarPresenter(this, linearLayout);
        jobCardCarPresenter.getCompanyName();

        if (bundle.getBoolean(Constant.IS_BOOKING)) {
            booking_exists = true;
            user_id = bundle.getString(Constant.USER_ID);
            booking_id = bundle.getString(Constant.BOOKING_ID);
            jobCardCarPresenter.getBookingCar(booking_id);
        } else {
            customer_name.setText(bundle.getString(Constant.USER_NAME));
            user_id = bundle.getString(Constant.USER_ID);
            booking_id = "";
            jobCardCarPresenter.getUserCars(user_id);
        }
    }
}
