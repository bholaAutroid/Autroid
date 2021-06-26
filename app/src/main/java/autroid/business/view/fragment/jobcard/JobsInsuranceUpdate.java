package autroid.business.view.fragment.jobcard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CompanyDataBE;
import autroid.business.model.bean.InsuranceDataBE;
import autroid.business.model.request.InsuranceUpdateRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.GetCompanyResponse;
import autroid.business.presenter.jobcard.JobInsuranceUpdatePresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;

public class JobsInsuranceUpdate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText policyHolder,policyNumber,premium;

    AppCompatAutoCompleteTextView companyName;

    TextView expire;

    Button update;

    RelativeLayout mainLayout;

    InsuranceDataBE insuranceData;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    int currentDay, currentMonth, currentYear;

    String bookingId="";

    JobInsuranceUpdatePresenter presenter;

    public JobsInsuranceUpdate(){}

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
        return inflater.inflate(R.layout.fragment_insurance_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);
        insuranceData= (InsuranceDataBE) getArguments().getSerializable(Constant.INSURANCE_DETAILS);
        setUpData();

        presenter=new JobInsuranceUpdatePresenter(this,mainLayout);
        presenter.getCompanyName();

        expire.setOnClickListener(v->{
            ((HomeScreen) getActivity()).disableTextview(expire);
            datePickerDialog.show();
        });

        update.setOnClickListener(v->{
            ((HomeScreen)getActivity()).disableButton(update);
            if(validate()){
                ((HomeScreen) getActivity()).disableButton(update);
                presenter.updateInsuranceData(createUpdateRequest());
            }
        });
    }

    private void setUpData() {
        bookingId=getArguments().getString(Constant.BOOKING_ID);
        policyHolder.setText(insuranceData.getPolicy_holder());
        companyName.setText(insuranceData.getInsurance_company());
        policyNumber.setText(insuranceData.getPolicy_no());
        premium.setText(String.valueOf(insuranceData.getPremium()));
        if(insuranceData.getExpire()!=null)expire.setText(insuranceData.getExpire().substring(0,10));
        else expire.setText("");
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
    }

    private void findViews(View view) {
        policyHolder=view.findViewById(R.id.policy_holder);
        companyName=view.findViewById(R.id.company_name);
        policyNumber=view.findViewById(R.id.policy_number);
        premium=view.findViewById(R.id.premium);
        expire=view.findViewById(R.id.expire);
        update=view.findViewById(R.id.update);
        mainLayout=view.findViewById(R.id.main_layout);
    }

    private boolean validate() {

        if(policyHolder.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Enter policy holder");
            return false;
        }if(companyName.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Enter company name");
            return false;
        }if(policyNumber.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Enter policy number");
            return false;
        }if(premium.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Enter premium");
            return false;
        }if(expire.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Enter expiry date");
            return false;
        }

        return true;
    }

    private InsuranceUpdateRequest createUpdateRequest(){
        InsuranceUpdateRequest insuranceUpdateRequest=new InsuranceUpdateRequest();
        insuranceUpdateRequest.setBookingId(bookingId);
        insuranceUpdateRequest.setPolicy_holder(policyHolder.getText().toString().trim());
        insuranceUpdateRequest.setInsurance_company(companyName.getText().toString().trim());
        insuranceUpdateRequest.setPolicy_no(policyNumber.getText().toString().trim());
        insuranceUpdateRequest.setPremium(Integer.parseInt(premium.getText().toString().trim()));
        insuranceUpdateRequest.setExpire(expire.getText().toString().trim());

        return insuranceUpdateRequest;
    }

    public void onSuccess(BaseResponse response) {
        Toast.makeText(getActivity(),response.getResponseMessage(),Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
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

        expire.setText(targetdatevalue);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    public void onSuccessCompanyName(GetCompanyResponse companyResponse) {
        ArrayList<String> comapanyNames = new ArrayList<>();
        for (CompanyDataBE data : companyResponse.getCompanyResponse()) comapanyNames.add(data.getCompanyName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_remark, comapanyNames);
        companyName.setAdapter(arrayAdapter);
    }
}
