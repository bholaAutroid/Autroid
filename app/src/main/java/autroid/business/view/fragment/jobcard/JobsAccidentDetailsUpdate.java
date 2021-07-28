package autroid.business.view.fragment.jobcard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.InsuranceDataBE;
import autroid.business.model.request.InsuranceAccidentUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobAccidentUpdateDetailsPresenter;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;

public class JobsAccidentDetailsUpdate extends DialogFragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    private RelativeLayout mainLayout;

    private Button update;

    private String spotSurvey="No",fir="No",bookingId="",policyType="Package Policy",strInsuranceBranch,strBranchContact,strGstin,strClaimNumber;

    private InsuranceDataBE insuranceData;

    private TextView accidentDate,accidentTime;

    private EditText driverName,accidentPlace,accidentCause;
    private EditText insuranceBranch,insuranceContact,insuranceGstin,insuranceClaim;

    private RadioButton spotSurveyYes,spotSurveyNo,firYes,firNo;
    RadioButton zeroDep,policyPackage,yesCashless, noCashless;

    private int currentDay, currentMonth, currentYear, currentHour, currentMin;

    private Calendar calendar;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private JobAccidentUpdateDetailsPresenter presenter;
    private Boolean cashlessValue;

    public JobsAccidentDetailsUpdate() {}

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
        return inflater.inflate(R.layout.fragment_jobs_accident_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        setUpAccidentData();

        presenter=new JobAccidentUpdateDetailsPresenter(this,mainLayout);
    }

    private void findViews(View view) {
        update=view.findViewById(R.id.update);
        mainLayout=view.findViewById(R.id.main_layout);
        driverName=view.findViewById(R.id.driver_name);
        accidentPlace=view.findViewById(R.id.accident_place);
        accidentDate=view.findViewById(R.id.accident_date);
        accidentTime=view.findViewById(R.id.accident_time);
        accidentCause=view.findViewById(R.id.accident_cause);
        spotSurveyYes=view.findViewById(R.id.spot_yes);
        spotSurveyNo=view.findViewById(R.id.spot_no);
        firYes=view.findViewById(R.id.fir_yes);
        firNo=view.findViewById(R.id.fir_no);
        yesCashless = view.findViewById(R.id.yes_cashless);
        noCashless = view.findViewById(R.id.no_cashless);
        zeroDep = view.findViewById(R.id.zero);
        policyPackage = view.findViewById(R.id.policy_package);

        insuranceBranch=view.findViewById(R.id.branch_name);
        insuranceContact=view.findViewById(R.id.branch_number);
        insuranceGstin=view.findViewById(R.id.gstin);
        insuranceClaim=view.findViewById(R.id.claim_number);

        spotSurveyYes.setOnClickListener(v->{
            if(spotSurveyYes.isChecked())spotSurvey="yes";
        });

        spotSurveyNo.setOnClickListener(v->{
            if(spotSurveyNo.isChecked())spotSurvey="no";
        });

        firYes.setOnClickListener(v->{
            if (firYes.isChecked())fir="yes";
        });

        firNo.setOnClickListener(v->{
            if (firNo.isChecked())fir="no";
        });

        zeroDep.setOnClickListener(v ->{
            if(zeroDep.isChecked())policyType="Zero Deprecation";
        });


        policyPackage.setOnClickListener(v->{
            if(policyPackage.isChecked())policyType="Package Policy";
        });
        
        yesCashless.setOnClickListener(v -> {
            if(yesCashless.isChecked())cashlessValue=true;
           
        });

        noCashless.setOnClickListener(v -> {
          
                if(noCashless.isChecked())cashlessValue=false;
        });


        accidentDate.setOnClickListener(v->{
            ((AwsHomeActivity) getActivity()).disableTextview(accidentDate);
            calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
            //datePickerDialog.getDatePicker().set(System.currentTimeMillis());
            datePickerDialog.show();
        });

        accidentTime.setOnClickListener(v->{
            ((AwsHomeActivity) getActivity()).disableTextview(accidentTime);
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMin = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
            timePickerDialog.show();
        });

        update.setOnClickListener(v->{
            InsuranceAccidentUpdateRequest insuranceUpdateRequest=new InsuranceAccidentUpdateRequest();
            insuranceUpdateRequest.setBooking(bookingId);
            insuranceUpdateRequest.setDriver_accident(driverName.getText().toString().trim());
            insuranceUpdateRequest.setAccident_place(accidentPlace.getText().toString().trim());
            insuranceUpdateRequest.setAccident_date(accidentDate.getText().toString().trim());
            insuranceUpdateRequest.setAccident_time(accidentTime.getText().toString().trim());
            insuranceUpdateRequest.setAccident_cause(accidentCause.getText().toString().trim());
            insuranceUpdateRequest.setSpot_survey(spotSurvey);
            insuranceUpdateRequest.setFir(fir);
            insuranceUpdateRequest.setBranch(insuranceBranch.getText().toString());
            insuranceUpdateRequest.setCashless(cashlessValue);
            insuranceUpdateRequest.setClaim(Boolean.TRUE);
            insuranceUpdateRequest.setInsurance_company(insuranceData.getInsurance_company());
            insuranceUpdateRequest.setPolicy_holder(insuranceData.getPolicy_holder());
            insuranceUpdateRequest.setPolicy_no(insuranceData.getPolicy_no());
            insuranceUpdateRequest.setPolicy_type(policyType);
            insuranceUpdateRequest.setExpire(insuranceData.getExpire());
            insuranceUpdateRequest.setPremium(insuranceData.getPremium());
            insuranceUpdateRequest.setClaim_no(insuranceClaim.getText().toString());

            insuranceUpdateRequest.setGstin(insuranceGstin.getText().toString());
            insuranceUpdateRequest.setContact_no(insuranceContact.getText().toString());
            //insuranceUpdateRequest.setPolicyType(policyType.trim());
            presenter.updateAccidentDetails(insuranceUpdateRequest);
        });
    }

    private void setUpAccidentData() {
        insuranceData= (InsuranceDataBE) getArguments().getSerializable(Constant.VALUE);
        bookingId=getArguments().getString(Constant.BOOKING_ID);
        spotSurvey=insuranceData.getSpot_survey();
        fir=insuranceData.getFir();
        driverName.setText(insuranceData.getDriver_accident());
        accidentPlace.setText(insuranceData.getAccident_place());
        accidentDate.setText(insuranceData.getAccident_date().substring(0,10));
        accidentTime.setText(insuranceData.getAccident_time());
        accidentCause.setText(insuranceData.getAccident_cause());

        policyType=insuranceData.getPolicy_type();
        cashlessValue=insuranceData.isCashless();
        strInsuranceBranch=insuranceData.getBranch();
        strBranchContact=insuranceData.getContact_no();
        strGstin=insuranceData.getGstin();
        strClaimNumber=insuranceData.getClaim_no();

        insuranceBranch.setText(strInsuranceBranch);
        insuranceContact.setText(strBranchContact);
        insuranceGstin.setText(strGstin);
        insuranceClaim.setText(strClaimNumber);

        if(cashlessValue){
            yesCashless.setChecked(true);
        }
        else {
            noCashless.setChecked(true);
        }

        if(fir.equalsIgnoreCase("yes")){
            firYes.setChecked(true);
        }
        else {
            firNo.setChecked(true);

        }

        if(spotSurvey.equalsIgnoreCase("yes")){
            spotSurveyYes.setChecked(true);
        }
        else {
            spotSurveyNo.setChecked(true);

        }

        if(policyType.equalsIgnoreCase("Zero Deprecation")){
            zeroDep.setChecked(true);
        }
        else {
            policyPackage.setChecked(true);
        }

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

        accidentDate.setText(targetdatevalue);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        accidentTime.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    public void onSuccess(BaseResponse response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }
}
