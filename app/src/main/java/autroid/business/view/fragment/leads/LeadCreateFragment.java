package autroid.business.view.fragment.leads;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.bean.AdvisorBE;
import autroid.business.model.realm.LeadsRealm;
import autroid.business.model.request.CreateLeadRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.CategoryResponse;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.presenter.LeadCreatePresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class LeadCreateFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.contact)
    EditText mContact;
    @BindView(R.id.source)
    AppCompatSpinner mSource;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.follow_up_layout)
    LinearLayout followUpLayout;
    @BindView(R.id.advisor_layout)
    TextInputLayout advisorLayout;
    @BindView(R.id.spn_advisor)
    AppCompatSpinner mAdvisor;
    @BindView(R.id.spn_status)
    AppCompatSpinner mSpnStatus;
    @BindView(R.id.priority)
    AppCompatSpinner prioritySpinner;
    @BindView(R.id.category)
    AppCompatSpinner categorySpinner;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.other_details)
    TextInputLayout other_details;
    @BindView(R.id.source_details)
    EditText source_details;
    @BindView(R.id.follow_up_date)
    TextView followUpDate;
    @BindView(R.id.follow_up_time)
    TextView followUpTime;
    @BindView(R.id.advisor_required)
    CheckBox advisor;

    int currentDay, currentMonth, currentYear, currentHour, currentMin, priority;

    DatePickerDialog datePickerDialog;

    TimePickerDialog timePickerDialog;

    Calendar calendar;

    LeadCreatePresenter mPresenter;

    ArrayList<AdvisorBE> advisorsList;

    String advisorId="";

    Realm realm;

    RealmController realmController;

    public LeadCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity80)));
        return inflater.inflate(R.layout.fragment_lead_create, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController=new RealmController(getActivity().getApplication());

        setUpDate();
        setUpTime();

        advisorsList=new ArrayList<>();
        followUpDate.setOnClickListener(this);
        followUpTime.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        advisor.setOnClickListener(this);

        mPresenter=new LeadCreatePresenter(this,mMainLayout);
        //mPresenter.getAdvisor();

        mPresenter.getLeadCategory();

        attachSourceSpinner();
        attachStatusSpinner();
        attachPrioritySpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (validate(mName.getText().toString().trim(), mEmail.getText().toString().trim(), mContact.getText().toString().trim(), mSource.getSelectedItemPosition(), mSpnStatus.getSelectedItemPosition(), prioritySpinner.getSelectedItemPosition(), remark.getText().toString().trim())) {
                    CreateLeadRequest createLeadRequest = new CreateLeadRequest();
                    createLeadRequest.setName(mName.getText().toString().trim());
                    createLeadRequest.setEmail(mEmail.getText().toString().trim());
                    createLeadRequest.setContact_no(mContact.getText().toString().trim());
                    if(mSource.getSelectedItemPosition()!=8)createLeadRequest.setSource(mSource.getSelectedItem().toString().trim());
                    else createLeadRequest.setSource(source_details.getText().toString().trim());
                    createLeadRequest.setStatus(mSpnStatus.getSelectedItem().toString().trim());
                    createLeadRequest.setDate(followUpDate.getText().toString().trim());
                    createLeadRequest.setTime(followUpTime.getText().toString().trim());
                    createLeadRequest.setPriority(priority);
                    createLeadRequest.setRemark(remark.getText().toString().trim());
                    createLeadRequest.setAdvisor(advisorId);
                    createLeadRequest.setCategory(categorySpinner.getSelectedItem().toString().trim());
                    mPresenter.createLead(createLeadRequest);
                }
                break;

            case R.id.follow_up_date:
                datePickerDialog.show();
                break;

            case R.id.follow_up_time:
                timePickerDialog.show();
                break;

            case R.id.advisor_required:
                if(advisor.isChecked())advisorLayout.setVisibility(View.VISIBLE);
                else{
                    advisorId="";
                    advisorLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void onSuccess(CreateLeadResponse baseResponse) {
        Toast.makeText(getActivity(),baseResponse.getResponseMessage(),Toast.LENGTH_SHORT).show();
        LeadsRealm leadsRealm=new LeadsRealm();
        leadsRealm.setId(baseResponse.getmLeadsBE().getId());
        leadsRealm.setName(baseResponse.getmLeadsBE().getName());
        leadsRealm.setContactNo(baseResponse.getmLeadsBE().getContact_no());
        leadsRealm.setEmail(baseResponse.getmLeadsBE().getEmail());
        leadsRealm.setPrimaryStatus(mSpnStatus.getSelectedItem().toString().trim());
        leadsRealm.setUpdatedAt(baseResponse.getmLeadsBE().getUpdated_at());
        leadsRealm.setCreatedAt(baseResponse.getmLeadsBE().getCreated_at());
        leadsRealm.setSource(baseResponse.getmLeadsBE().getSource());
        leadsRealm.setUserId(baseResponse.getmLeadsBE().getUser());
        if(baseResponse.getmLeadsBE().getRemark()!=null) {
            leadsRealm.setStatus(baseResponse.getmLeadsBE().getRemark().getStatus());
            leadsRealm.setAssignee_remark(baseResponse.getmLeadsBE().getRemark().getAssignee_remark());
        }

        realm.beginTransaction();
        realm.copyToRealm(leadsRealm);
        realm.commitTransaction();

        getDialog().dismiss();
    }

    public void onSuccessAdvisors(AdvisorResponse response) {

        AdvisorBE advisor=new AdvisorBE();
        advisor.setId("");
        advisor.setName("Select Advisor");
        advisor.setContact_no("");
        advisor.setEmail("");

        advisorsList.add(advisor);
        advisorsList.addAll(response.getAdvisorsList());

        ArrayList<String> advisorNames=new ArrayList<>();
        for (AdvisorBE data: advisorsList)advisorNames.add(data.getName());

        ArrayAdapter<String> advisorAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text,advisorNames);
        advisorAdapter.setDropDownViewResource(R.layout.layout_spinner_text);
        mAdvisor.setAdapter(advisorAdapter);

        mAdvisor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                advisorId=advisorsList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onSuccessCategory(CategoryResponse response) {

        ArrayList<String> list=new ArrayList<>();
        list.add("Select Category");
        list.addAll(response.getCategoryList());

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text,list);
        adapterCategory.setDropDownViewResource(R.layout.layout_spinner_text);
        categorySpinner.setAdapter(adapterCategory);
    }

    private Boolean validate(String name,String email,String mobile,int source,int status,int priority,String remark){

        if(name.length()==0){
            Utility.showResponseMessage(mMainLayout,"Name is required");
            return false;
        }else if(email.length()>0 && !Utility.isEmailValid(mMainLayout,email)){
            Utility.showResponseMessage(mMainLayout,"Invalid email address");
            return false;
        }else if(categorySpinner.getSelectedItem()==null){
            Utility.showResponseMessage(mMainLayout,"Please refresh the screen");
            return false;
        }else if(categorySpinner.getSelectedItemPosition()==0){
            Utility.showResponseMessage(mMainLayout,"Please select lead category");
            return false;
        }else if(source==0){
            Utility.showResponseMessage(mMainLayout,"Select source");
            return false;
        }else if(source!=3 && source!=4 && source!=8 && mobile.length()==0){
            Utility.showResponseMessage(mMainLayout,"Invalid contact number");
            return false;
        }else if(mobile.length()>0 && mobile.length()!=10){
            Utility.showResponseMessage(mMainLayout,"Invalid contact number");
            return false;
        } else if(source==8 && source_details.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mMainLayout,"Please specify source details");
            return false;
        }else if(status==0){
            Utility.showResponseMessage(mMainLayout,"Please select status");
            return false;
        }else if(status==2 && followUpDate.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mMainLayout,"Please select follow-up date");
            return false;
        }else if(advisor.isChecked() && advisorId.equals("")){
            Utility.showResponseMessage(mMainLayout,"Please select advisor");
            return false;
        }else if(priority==0){
            Utility.showResponseMessage(mMainLayout,"Please select priority");
            return false;
        }else if(remark.length()==0){
            Utility.showResponseMessage(mMainLayout,"Please enter remark");
            return false;
        }

        return true;
    }

    public void attachStatusSpinner() {
        ArrayList<String> list=new ArrayList<>();
        list.add("Select Status");
        list.add(Constant.OPEN);
        list.add(Constant.FOLLOW_UP);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_text,list);
        adapterStatus.setDropDownViewResource(R.layout.layout_spinner_text);
        mSpnStatus.setAdapter(adapterStatus);

        mSpnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==1) followUpLayout.setVisibility(View.GONE);
                else if(position==2) followUpLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void attachSourceSpinner(){
        ArrayList<String> list=new ArrayList<>();
        list.add("Select Source");
        list.add("Quikr");
        list.add("Justdial");
        list.add("Instagram");
        list.add("Facebook");
        list.add("Mail");
        list.add("Website");
        list.add("Call");
        list.add("Other");
        list.add("Feedback");

        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text,list);
        adapterSource.setDropDownViewResource(R.layout.layout_spinner_text);
        mSource.setAdapter(adapterSource);

        mSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==8){
                    other_details.setVisibility(View.VISIBLE);
                }else if(position!=8 && other_details.getVisibility()==View.VISIBLE){
                    other_details.setVisibility(View.GONE);
                }
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

        ArrayAdapter<String> adapterSource = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text,list);
        adapterSource.setDropDownViewResource(R.layout.layout_spinner_text);
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

    public void setUpDate(){
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    public void setUpTime(){
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMin = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String targetDateValue = null;
        try {
            Date sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetDateValue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        followUpDate.setText(targetDateValue);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        followUpTime.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }
}
