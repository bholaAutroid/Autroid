package autroid.business.view.fragment.jobcard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import org.greenrobot.eventbus.Subscribe;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import autroid.business.R;
import autroid.business.adapter.jobcard.RequirementsAdapter;
import autroid.business.adapter.jobcard.ServicesAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.RequirementBE;
import autroid.business.model.bean.ServicesDataBE;
import autroid.business.model.bean.TechniciansDataBE;
import autroid.business.model.request.RequirementRequest;
import autroid.business.model.response.GetBookedServicesResponse;
import autroid.business.model.response.GetDriverResponse;
import autroid.business.model.response.GetTechniciansResponse;
import autroid.business.presenter.jobcard.JobCardRequirementsPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class JobCardRequirementFragment extends Fragment implements TextWatcher, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private RecyclerView recyclerView, services_recycler;
    private ArrayList<RequirementBE> arrayList;
    private ArrayList<TechniciansDataBE> techList;
    private ArrayList<String> techNameList;
    private ArrayList<ServicesDataBE> servicesList;
    private RequirementsAdapter requirementsAdapter;
    private ServicesAdapter servicesAdapter;
    private TextView textView, commitment_date, commitment_time, services;
    private LinearLayout mainLayout;
    private RadioButton yes, no;
    private CheckBox driver;
    private EditText driver_name, driver_phone;
    private JobCardRequirementsPresenter requirementsPresenter;
    private LinearLayout add_item;
    private Spinner technicianSpinner;
    private String techId = "", driverId = "", claim = "no", cashlessValue = "", bookingId = "", driverAccidental = "", accidentPlace = "", accidentDate = "", manufacturingDate, accidentTime = "", accidentCause = "", spotSurvey = "", fir = "";
    private Button proceed;
    private int currentDay, currentMonth, currentYear, currentHour, currentMin;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private boolean isDriver, collisionRequired;
    private JobCardCollisionFragment collisionFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_car_requirement, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);
        Utility.hideSoftKeyboard(getActivity());

        bookingId = getArguments().getString(Constant.BOOKING_ID);

        textView = view.findViewById(R.id.toolbar_title);
        textView.setText("Requirement Details");

        arrayList = new ArrayList<>();
        arrayList.add(new RequirementBE());
        techList = new ArrayList<>();
        techNameList = new ArrayList<>();
        servicesList = new ArrayList<>();

        mainLayout = view.findViewById(R.id.main_layout);
        requirementsPresenter = new JobCardRequirementsPresenter(this, mainLayout);
        collisionFragment = new JobCardCollisionFragment();
        requirementsPresenter.getTechnicians();
        requirementsPresenter.getServices(bookingId);

        technicianSpinner = view.findViewById(R.id.technician_name);
        services_recycler = view.findViewById(R.id.services_recycler);
        recyclerView = view.findViewById(R.id.requirement_recycler);
        add_item = view.findViewById(R.id.add);
        //cashless = view.findViewById(R.id.cashless);
        yes = view.findViewById(R.id.yes);
        no = view.findViewById(R.id.no);
        //yesCashless = view.findViewById(R.id.yes_cashless);
        //noCashless = view.findViewById(R.id.no_cashless);
        driver_name = view.findViewById(R.id.driver_name);
        driver_phone = view.findViewById(R.id.driver_phone);
        driver = view.findViewById(R.id.is_driver);
        proceed = view.findViewById(R.id.jobcard_proceed);
        //radioGroup = view.findViewById(R.id.cashless_radiogroup);
        commitment_date = view.findViewById(R.id.commitment_date);
        commitment_time = view.findViewById(R.id.commitment_time);
        services = view.findViewById(R.id.services);
        requirementsAdapter = new RequirementsAdapter(arrayList);
        servicesAdapter = new ServicesAdapter(servicesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(requirementsAdapter);
        services_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        services_recycler.setAdapter(servicesAdapter);


        technicianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                techId = techList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add_item.setOnClickListener(v -> {
            RequirementBE requirementBE = new RequirementBE();
            requirementBE.setFocused(true);
            arrayList.add(requirementBE);
            Utility.hideSoftKeyboard(getActivity());
            requirementsAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(arrayList.size() - 1);
        });

        yes.setOnClickListener(v -> {
            if (yes.isChecked()) {
                claim = "yes";
                collisionRequired = true;
                collisionFragment.show(getFragmentManager(), "Collision Fragment");
            }
        });

        no.setOnClickListener(v -> {
            if (no.isChecked()) {
                claim = "no";
                collisionRequired = false;
            }
        });

        driver.setOnClickListener(v -> {
            if (driver.isChecked()) {
                driver_phone.setVisibility(View.VISIBLE);
                driver_phone.addTextChangedListener(this);
                isDriver = true;
                driver_phone.requestFocus();
                Utility.showSoftKeyboard(getActivity(), driver_phone);
            } else {
                driverId = "";
                driver_phone.setVisibility(View.GONE);
                driver_name.setVisibility(View.GONE);
                driver_phone.removeTextChangedListener(this);
                driver_phone.setText("");
                driver_name.setText("");
                isDriver = false;
            }
        });

        commitment_date.setOnClickListener(v -> {
            ((AwsHomeActivity) getActivity()).disableTextview(commitment_date);
            calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        commitment_time.setOnClickListener(v -> {
            ((AwsHomeActivity) getActivity()).disableTextview(commitment_time);
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMin = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
            timePickerDialog.show();
        });

        proceed.setOnClickListener(v -> {
            ((AwsHomeActivity) getActivity()).disableButton(proceed);
            if (validateData()) requirementsPresenter.putRequirements(createReqRequest());
        });
    }

    public void onSuccessTechnician(GetTechniciansResponse techniciansResponse) {

        TechniciansDataBE tech = new TechniciansDataBE();
        tech.setId("");
        tech.setName("Select Technician");
        techList.add(tech);
        techNameList.add(tech.getName());
        for (TechniciansDataBE data : techniciansResponse.getTechnicians()) {
            techList.add(data);
            techNameList.add(data.getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_remark, techNameList);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        technicianSpinner.setAdapter(arrayAdapter);
    }

    public void onSuccessDriver(GetDriverResponse driverResponse) {

        driver_name.setVisibility(View.VISIBLE);

        if (driverResponse.getDriverDataBE().getName() == null) {
            driverId = "";
            driver_name.setEnabled(true);
            driver_name.requestFocus();
            Utility.showSoftKeyboard(getActivity(), driver_name);
        } else {
            driver_name.setEnabled(false);
            driverId = driverResponse.getDriverDataBE().getDriverId();
            driver_name.setText(driverResponse.getDriverDataBE().getName());
            Utility.hideSoftKeyboard(getActivity());
        }
    }

    public void onSuccessServices(GetBookedServicesResponse bookingResponse) {
        if (bookingResponse.getServicesList().size() != 0) {
            services.setVisibility(View.VISIBLE);
            servicesList.addAll(bookingResponse.getServicesList());
            servicesAdapter.notifyDataSetChanged();
        }
    }

    public void onSuccess() {
        ((AwsHomeActivity) getActivity()).clearStackLocal();
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().length() == 10) {
            requirementsPresenter.getDriver(driver_phone.getText().toString().trim());
            driver_phone.clearFocus();
            driver_name.setText("");
        } else if (charSequence.toString().length() < 10 && driver_name.getVisibility() == View.VISIBLE) {
            driver_name.setVisibility(View.GONE);
            driver_name.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private RequirementRequest createReqRequest() {

        RequirementRequest request = new RequirementRequest();
        request.setBookingId(bookingId);
        request.setArrayList(validArrayList());
        request.setTechnicianId(techId);
        request.setDelivery_date(commitment_date.getText().toString().trim());
        request.setClaim(claim);
        request.setIs_driver(isDriver);
        request.setTime(commitment_time.getText().toString().trim());
        request.setCashless(cashlessValue);
        request.setDriverId(driverId);
        request.setName(driver_name.getText().toString().trim());
        request.setContact_no(driver_phone.getText().toString().trim());
        request.setDriver_accident(driverAccidental);
        request.setAccident_place(accidentPlace);
        request.setAccident_date(accidentDate);
        request.setManufacture_year(manufacturingDate);
        request.setAccident_time(accidentTime);
        request.setAccident_cause(accidentCause);
        request.setSpot_survey(spotSurvey);
        request.setFir(fir);

        return request;
    }

    private boolean validateData() {

        if (claim.length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please select claim...");
            return false;
        } else if (collisionRequired) {
            collisionFragment.show(getFragmentManager(), "CollisionFragment");
            return false;
        }/*else if (cashless.getVisibility() == View.VISIBLE && cashlessValue.length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please select cashless...");
            return false;
        }*/ else if (techId.length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please select technician...");
            return false;
        } else if (commitment_date.getText().toString().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please select date...");
            return false;
        } else if (commitment_time.getText().toString().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please select time...");
            return false;
        } else if (isDriver && driver_phone.getText().toString().length() != 10) {
            Utility.showResponseMessage(mainLayout, "Invalid phone details...");
            return false;
        } else if (isDriver && driver_name.getText().toString().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Please fill driver name...");
            return false;
        }

        return true;
    }

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

        commitment_date.setText(targetdatevalue);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        commitment_time.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public ArrayList<RequirementBE> validArrayList() {
        for (Iterator<RequirementBE> iterator = arrayList.iterator(); iterator.hasNext(); ) {
            RequirementBE data = iterator.next();
            if (data.getText().trim().equals("")) {
                iterator.remove();
            }
        }

        return arrayList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        collisionRequired = false;
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_COLLISION) {
            driverAccidental = intent.getStringExtra("driverName");
            accidentPlace = intent.getStringExtra("accidentPlace");
            accidentDate = intent.getStringExtra("accidentDate");
            accidentTime = intent.getStringExtra("accidentTime");
            manufacturingDate = intent.getStringExtra("manufacturingYear");
            accidentCause = intent.getStringExtra("accidentCause");
            spotSurvey = intent.getStringExtra("spotSurvey");
            fir = intent.getStringExtra("fir");
            cashlessValue = intent.getStringExtra("cashless");
        }
    }
}