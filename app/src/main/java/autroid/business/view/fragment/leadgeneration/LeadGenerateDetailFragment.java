package autroid.business.view.fragment.leadgeneration;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.realm.InsuranceDueRealm;
import autroid.business.model.realm.ServiceDueRealm;
import autroid.business.model.request.CreateLeadRequest;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.presenter.leadgeneration.LeadCreatePresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.customviews.CustomSpinner;
import io.realm.Realm;

public class LeadGenerateDetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private String id = "";

    private InsuranceDueRealm insuranceDueRealm;
    private ServiceDueRealm serviceDueRealm;

    private ImageButton callButton;

    private Realm realm;

    private RealmController realmController;

    private TextView policyHolder, policyNumber, insuranceCompany, expiryDate, premium, carTitle, followUpDate, followUpTime;

    private TextView name,contact,email;

    private CustomSpinner spinnerStatus,prioritySpinner;

    private RelativeLayout followUpLayout;
    private LinearLayout llInsurance;

    private LinearLayout mMainLayout;

    private int currentDay, currentMonth, currentYear, currentHour, currentMin, priority;

    private Calendar calendar;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private EditText mRemark;

    private Button submit;

    private LeadCreatePresenter presenter;
    private Boolean isInsurance=false;

    @BindView(R.id.chat_btn)
    ImageButton chatBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insurance_due_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this,view);

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController = new RealmController(getActivity().getApplication());

        id = getArguments().getString(Constant.KEY_ID);
        isInsurance=getArguments().getBoolean(Constant.KEY_TYPE);


        findViews(view);

        if(isInsurance) {
            insuranceDueRealm = realmController.getInsuranceDueById(id);
            setUpInsuranceData();
        }
        else {
            serviceDueRealm=realmController.getServiceDueById(id);
            setUpServiceData();
            llInsurance.setVisibility(View.GONE);
        }

        attachStatusSpinner();

        attachPrioritySpinner();

        setUpDateData();

        setUpTimeData();

        presenter = new LeadCreatePresenter(this,mMainLayout);
    }

    private void findViews(View view) {

        mMainLayout = view.findViewById(R.id.main_layout);
        mRemark = view.findViewById(R.id.remark);
        spinnerStatus = view.findViewById(R.id.spn_status);
        prioritySpinner = view.findViewById(R.id.priority_spinner);
        followUpLayout = view.findViewById(R.id.follow_up_layout);
        llInsurance=view.findViewById(R.id.ll_insurance);
        followUpDate = view.findViewById(R.id.follow_up_date);
        followUpTime = view.findViewById(R.id.follow_up_time);
        carTitle = view.findViewById(R.id.car_title);
        callButton = view.findViewById(R.id.call_btn);
        policyHolder = view.findViewById(R.id.policy_holder_name);
        policyNumber = view.findViewById(R.id.policy_number);
        insuranceCompany = view.findViewById(R.id.ins_company_name);
        expiryDate = view.findViewById(R.id.expiry_date);
        premium = view.findViewById(R.id.premium);
        submit = view.findViewById(R.id.update_status);
        name = view.findViewById(R.id.name);
        contact = view.findViewById(R.id.mobile_number);
        email = view.findViewById(R.id.email);

        followUpDate.setOnClickListener(v->datePickerDialog.show());

        followUpTime.setOnClickListener(v->timePickerDialog.show());

        submit.setOnClickListener(v->{
            if(validate()){
                if(isInsurance) {
                    CreateLeadRequest createLeadRequest = new CreateLeadRequest();
                    createLeadRequest.setName(insuranceDueRealm.getUserName());
                    createLeadRequest.setEmail(insuranceDueRealm.getEmail());
                    createLeadRequest.setContact_no(insuranceDueRealm.getContactNumber());
                    createLeadRequest.setModel(insuranceDueRealm.getId());
                    createLeadRequest.setSource("Lead Generation");
                    createLeadRequest.setStatus(spinnerStatus.getSelectedItem().toString().trim());
                    createLeadRequest.setDate(followUpDate.getText().toString().trim());
                    createLeadRequest.setTime(followUpTime.getText().toString().trim());
                    createLeadRequest.setPriority(priority);
                    createLeadRequest.setRemark(mRemark.getText().toString().trim());
                    createLeadRequest.setCategory("Insurance");
                    presenter.createLead(createLeadRequest);
                }
                else {
                    CreateLeadRequest createLeadRequest = new CreateLeadRequest();
                    createLeadRequest.setName(serviceDueRealm.getUserName());
                    createLeadRequest.setEmail(serviceDueRealm.getEmail());
                    createLeadRequest.setContact_no(serviceDueRealm.getContactNumber());
                    createLeadRequest.setModel(serviceDueRealm.getId());
                    createLeadRequest.setSource("Lead Generation");
                    createLeadRequest.setStatus(spinnerStatus.getSelectedItem().toString().trim());
                    createLeadRequest.setDate(followUpDate.getText().toString().trim());
                    createLeadRequest.setTime(followUpTime.getText().toString().trim());
                    createLeadRequest.setPriority(priority);
                    createLeadRequest.setRemark(mRemark.getText().toString().trim());
                    createLeadRequest.setCategory("Booking");
                    presenter.createLead(createLeadRequest);
                }
            }
        });
    }

    private void setUpInsuranceData() {

        carTitle.setText(insuranceDueRealm.getTitle());


        policyHolder.setText(insuranceDueRealm.getPolicyHolder());
        policyNumber.setText(insuranceDueRealm.getPolicyNumber());
        insuranceCompany.setText(insuranceDueRealm.getInsuranceCompany());
        expiryDate.setText(insuranceDueRealm.getExpire());
        premium.setText("₹ " + insuranceDueRealm.getPremium());

        name.setText(insuranceDueRealm.getUserName());
        contact.setText(insuranceDueRealm.getContactNumber());
        email.setText(insuranceDueRealm.getEmail());

        chatBtn.setOnClickListener(v ->
                    Utility.whatsAppChat(insuranceDueRealm.getContactNumber(), getActivity()));


        callButton.setOnClickListener(v -> Utility.onCallClick(insuranceDueRealm.getContactNumber(), getActivity()));
    }

    private void setUpServiceData() {

        carTitle.setText(serviceDueRealm.getTitle());

        name.setText(serviceDueRealm.getUserName());
        contact.setText(serviceDueRealm.getContactNumber());
        email.setText(serviceDueRealm.getEmail());

        chatBtn.setOnClickListener(v ->
                Utility.whatsAppChat(serviceDueRealm.getContactNumber(), getActivity()));

        callButton.setOnClickListener(v -> Utility.onCallClick(serviceDueRealm.getContactNumber(), getActivity()));
    }

    private void attachStatusSpinner() {
        ArrayList<String> list=new ArrayList<>();
        list.add("Select Status");
        list.add(Constant.FOLLOW_UP);
        list.add(Constant.CLOSED);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_remark,list);
        adapterStatus.setDropDownViewResource(R.layout.layout_spinner_remark);
        spinnerStatus.setAdapter(adapterStatus);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0) followUpLayout.setVisibility(View.GONE);
                else if(position==1) followUpLayout.setVisibility(View.VISIBLE);
                else if (position==2) followUpLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void attachPrioritySpinner(){
        ArrayList<String> list=new ArrayList<>();
        list.add("Select Priority");
        list.add("High");
        list.add("Medium");
        list.add("Low");

        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark,list);
        adapterSource.setDropDownViewResource(R.layout.layout_spinner_remark);
        prioritySpinner.setAdapter(adapterSource);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==1)priority=3;
                else if(position==2)priority=2;
                else if(position==3) priority=1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpDateData() {
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void setUpTimeData() {
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMin = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
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

        followUpDate.setText(targetdatevalue);

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        followUpTime.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    private String zeroPrefix(int quantity) {
        if (quantity < 10) return "0" + quantity;
        return "" + quantity;
    }


    private boolean validate() {

        if (spinnerStatus.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select status");
            return false;
        } else if (spinnerStatus.getSelectedItem().equals(Constant.FOLLOW_UP) && followUpDate.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select Follow-Up Date");
            return false;
        } else if (prioritySpinner.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select Priority");
            return false;
        } else if (mRemark.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Remark");
            return false;
        }

        return true;
    }

    public void onSuccess(CreateLeadResponse response) {
        getActivity().onBackPressed();
    }
}
