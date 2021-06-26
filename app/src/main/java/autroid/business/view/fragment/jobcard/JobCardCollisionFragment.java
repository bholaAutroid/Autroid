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
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.request.RequirementRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobCardCollisionPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;

public class JobCardCollisionFragment extends DialogFragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    LinearLayout mainLayout;

    EditText driverName, accidentPlace,accidentCause;

    TextView date,time;

    Button save;

    RadioButton spotYes,spotNo,firYes,firNo,yesCashless, noCashless;

    String bookingId="",spotSurvey="",fir="",cashlessValue="";

    int currentDay, currentMonth, currentYear, currentHour, currentMin;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    TimePickerDialog timePickerDialog;

    AppCompatSpinner manufacturingYear;

    JobCardCollisionPresenter mPresenter;

    boolean isJobOpened;

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
        return inflater.inflate(R.layout.job_card_collision_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mainLayout=view.findViewById(R.id.main_layout);
        save=view.findViewById(R.id.save);
        driverName=view.findViewById(R.id.driver_collision);
        accidentPlace=view.findViewById(R.id.place);
        accidentCause=view.findViewById(R.id.accident_cause);
        date=view.findViewById(R.id.date);
        time=view.findViewById(R.id.time);
        manufacturingYear=view.findViewById(R.id.manufacturing_year);

        mPresenter=new JobCardCollisionPresenter(this,mainLayout);

        spotYes=view.findViewById(R.id.spot_yes);
        spotNo=view.findViewById(R.id.spot_no);
        firYes=view.findViewById(R.id.fir_yes);
        firNo=view.findViewById(R.id.fir_no);

        yesCashless = view.findViewById(R.id.yes_cashless);
        noCashless = view.findViewById(R.id.no_cashless);

        if(getArguments()!=null && getArguments().getBoolean(Constant.KEY_TYPE)){
            isJobOpened=true;
            bookingId=getArguments().getString(Constant.BOOKING_ID);
        }

        setUpYear();



        spotYes.setOnClickListener(this);
        spotNo.setOnClickListener(this);
        firYes.setOnClickListener(this);
        firNo.setOnClickListener(this);
        yesCashless.setOnClickListener(this);
        noCashless.setOnClickListener(this);

        date.setOnClickListener(v -> {
            ((HomeScreen)getActivity()).disableTextview(date);
            calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        time.setOnClickListener(v -> {
            ((HomeScreen)getActivity()).disableTextview(time);
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMin = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
            timePickerDialog.show();
        });

        save.setOnClickListener(v->{
            if(validate()){
                if(!isJobOpened) {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_COLLISION);
                    broadcastIntent.putExtra("driverName", driverName.getText().toString().trim());
                    broadcastIntent.putExtra("accidentPlace", accidentPlace.getText().toString().trim());
                    broadcastIntent.putExtra("accidentDate", date.getText().toString().trim());
                    broadcastIntent.putExtra("accidentTime", time.getText().toString().trim());
                    broadcastIntent.putExtra("manufacturingYear", manufacturingYear.getSelectedItem().toString().trim());
                    broadcastIntent.putExtra("accidentCause", accidentCause.getText().toString().trim());
                    broadcastIntent.putExtra("spotSurvey", spotSurvey.trim());
                    broadcastIntent.putExtra("fir", fir.trim());
                    broadcastIntent.putExtra("cashless",cashlessValue.trim());
                    Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                    GlobalBus.getBus().post(sendEvent);
                    getDialog().dismiss();
                    Toast.makeText(getActivity(), "Details Saved...", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getActivity(), "Details Saved...", Toast.LENGTH_LONG).show();
                 /*   Intent broadcastIntent = new Intent();
                    broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_COLLISION);
                    broadcastIntent.putExtra("driverName", driverName.getText().toString().trim());
                    broadcastIntent.putExtra("accidentPlace", accidentPlace.getText().toString().trim());
                    broadcastIntent.putExtra("accidentDate", date.getText().toString().trim());
                    broadcastIntent.putExtra("accidentTime", time.getText().toString().trim());
                    broadcastIntent.putExtra("manufacturingYear", manufacturingYear.getSelectedItem().toString());
                    broadcastIntent.putExtra("accidentCause", accidentCause.getText().toString().trim());
                    broadcastIntent.putExtra("spotSurvey", spotSurvey.trim());
                    broadcastIntent.putExtra("fir", fir.trim());
                    Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                    GlobalBus.getBus().post(sendEvent);
                    getDialog().dismiss();*/

                    RequirementRequest requirementRequest=new RequirementRequest();
                    requirementRequest.setBookingId(bookingId);
                    requirementRequest.setClaim("yes");
                    requirementRequest.setAccident_cause(accidentCause.getText().toString().trim());
                    requirementRequest.setDriver_accident(driverName.getText().toString().trim());
                    requirementRequest.setAccident_place( accidentPlace.getText().toString().trim());
                    requirementRequest.setAccident_date(date.getText().toString().trim());
                    requirementRequest.setAccident_time(time.getText().toString().trim());
                    requirementRequest.setManufacture_year(manufacturingYear.getSelectedItem().toString().trim());
                    requirementRequest.setCashless(cashlessValue);
                    requirementRequest.setSpot_survey(spotSurvey.trim());
                    requirementRequest.setFir(fir.trim());
                    mPresenter.addClaim(requirementRequest);
                }
            }
        });
    }

    private void setUpYear() {
        calendar=Calendar.getInstance();
        int currentYear=calendar.get(Calendar.YEAR);
        ArrayList<String> years=new ArrayList<>();
        years.add("Manufacturing Year");
        for (int i=currentYear;i>=2000;i--)years.add(Integer.toString(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, years);
        manufacturingYear.setAdapter(adapter);
    }

    private boolean validate() {

        if(driverName.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Enter Who Was Driving");
            return false;
        }else if(accidentPlace.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Enter Accident Place");
            return false;
        }else if(date.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Enter Date Of Accident");
            return false;
        }else if(time.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Enter Time Of Accident");
            return false;
        }else if(manufacturingYear.getSelectedItemPosition()==0){
            Utility.showResponseMessage(mainLayout,"Select Manufacturing Year");
            return false;
        }else if(spotSurvey.trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Select Spot Survey");
            return false;
        }else if(fir.trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Select FIR");
            return false;
        }else if(cashlessValue.trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please select policy type");
            return false;
        }else if(accidentCause.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Please Fill Accident Cause");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.spot_yes:
                if(spotYes.isChecked())spotSurvey="yes";
                break;
            case R.id.spot_no:
                if(spotNo.isChecked())spotSurvey="no";
                break;
            case R.id.fir_yes:
                if(firYes.isChecked())fir="yes";
                break;
            case R.id.fir_no:
                if(firNo.isChecked())fir="no";
                break;
            case R.id.yes_cashless:
                if(yesCashless.isChecked())cashlessValue="yes";
                break;
            case R.id.no_cashless:
                if(noCashless.isChecked())cashlessValue="no";
                break;
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

        date.setText(targetdatevalue);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        time.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    public void onSuccess(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
        if(getDialog()!=null) getDialog().dismiss();
    }
}
