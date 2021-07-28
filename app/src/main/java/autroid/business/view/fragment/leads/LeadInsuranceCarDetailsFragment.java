package autroid.business.view.fragment.leads;

import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.model.request.CarInsuranceRequest;
import autroid.business.model.response.CarStockResponse;
import autroid.business.presenter.CustomerCarDetailsPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.customviews.CustomSpinner;
import autroid.business.view.fragment.MonthYearPickerDialog;

public class LeadInsuranceCarDetailsFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private RelativeLayout mainLayout;

    private LinearLayout previousPolicyLayout,insuranceClaimLayout,noClaimLayout;

    private EditText registrationNumber;

    private TextView registrationManufactureYear,policyEndDate,carTitle,variableName;

    private ImageView backNavigation;

    private CustomSpinner insuranceFor,insuranceClaim,insuranceClaimBonus;

    private Button next;

    private boolean mutexA, mutexB;

    private CarStockResponse carStockResponse;

    private CustomerCarDetailsPresenter presenter;

    private String carId;

    public LeadInsuranceCarDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_car_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        carId=getArguments().getString(Constant.KEY_CAR_ID);

        mainLayout=view.findViewById(R.id.main_layout);
        carTitle=view.findViewById(R.id.car_title);
        registrationNumber=view.findViewById(R.id.car_registration_no);
        insuranceFor=view.findViewById(R.id.spn_insurance_for);
        insuranceClaim=view.findViewById(R.id.spn_insurance_claim);
        insuranceClaimBonus=view.findViewById(R.id.spn_claim_bonus);
        carTitle=view.findViewById(R.id.car_title);
        registrationManufactureYear=view.findViewById(R.id.year);
        variableName=view.findViewById(R.id.manufacturing_reg_year);
        previousPolicyLayout=view.findViewById(R.id.previous_policy_layout);
        insuranceClaimLayout=view.findViewById(R.id.insurance_claim_layout);
        noClaimLayout=view.findViewById(R.id.no_claim_layout);
        policyEndDate=view.findViewById(R.id.expiry_date);
        next=view.findViewById(R.id.btn_next);
        backNavigation=view.findViewById(R.id.back_navigation);

        ArrayAdapter<String> varX = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_remark, getResources().getStringArray(R.array.insurance_for));
        insuranceFor.setAdapter(varX);
        insuranceFor.setSelection(1);

        ArrayAdapter<String> varY = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_remark, getResources().getStringArray(R.array.insurance_claim));
        insuranceClaim.setAdapter(varY);

        ArrayAdapter<String> varZ = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_remark, getResources().getStringArray(R.array.claim_bonus));
        insuranceClaimBonus.setAdapter(varZ);

        backNavigation.setOnClickListener(v->getActivity().onBackPressed());

        registrationManufactureYear.setOnClickListener(v->{
            MonthYearPickerDialog dialog = new MonthYearPickerDialog();
            dialog.setListener(this);
            dialog.show(getFragmentManager(), "MonthYearPickerDialog");
            mutexA = true;
            mutexB = false;
        });

        policyEndDate.setOnClickListener(v->{
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            mutexB = true;
            mutexA = false;
        });

        insuranceFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if(pos==0){
                    variableName.setText("Manufacture Year");
                    insuranceClaimLayout.setVisibility(View.GONE);
                    noClaimLayout.setVisibility(View.GONE);
                    previousPolicyLayout.setVisibility(View.GONE);
                }else {
                    variableName.setText("Registration Year");
                    noClaimLayout.setVisibility(View.VISIBLE);
                    insuranceClaimLayout.setVisibility(View.VISIBLE);
                    previousPolicyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        insuranceClaim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos==0)noClaimLayout.setVisibility(View.GONE);
                else noClaimLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        next.setOnClickListener(v->{
            if(validate()){
                Bundle bundle=new Bundle();
                bundle.putSerializable(Constant.VALUE,createRequest());
                ((AwsHomeActivity)getActivity()).makeDrawerVisible();
                ((AwsHomeActivity)getActivity()).addFragment(new LeadInsuranceCustomerFragment(),"CustomerDetailsFragment",true,false,bundle,((AwsHomeActivity)getActivity()).currentFrameId);
            }
        });

        presenter=new CustomerCarDetailsPresenter(this,mainLayout);

        presenter.getCarDetail(carId);
    }

    public void onSuccessCarDetail(CarStockResponse response) {
        this.carStockResponse=response;
        carTitle.setText(response.getGetCarDetails().getTitle());
        registrationNumber.setText(response.getGetCarDetails().getRegistration_no());
        if (response.getGetCarDetails().getInsuranceDataBE()!=null && response.getGetCarDetails().getInsuranceDataBE().getExpire()!=null)policyEndDate.setText(response.getGetCarDetails().getInsuranceDataBE().getExpire());
    }

    private boolean validate(){

        if (carTitle.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Maker");
            return false;
        } else if (registrationNumber.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Registration Number");
            return false;
        }else if (registrationManufactureYear.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid year");
            return false;
        } else if (insuranceFor.getSelectedItemPosition()==1 && policyEndDate.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid policy end date");
            return false;
        }

        return true;
    }

    private CarInsuranceRequest createRequest(){

        CarInsuranceRequest request=new CarInsuranceRequest();

        if(insuranceFor.getSelectedItemPosition()==0) request.setInsuranceType("new");
        else  request.setInsuranceType("renew");

        request.setMake(carStockResponse.getGetCarDetails().get_automaker());
        request.setModel(carStockResponse.getGetCarDetails().get_model());
        request.setVariant(carStockResponse.getGetCarDetails().get_variant());
        request.setRegistrationNumber(registrationNumber.getText().toString().trim());
        request.setRegistrationYear(registrationManufactureYear.getText().toString().trim());

        request.setFirstName(carStockResponse.getGetCarDetails().getUser().getName());
        request.setMobileNumber(carStockResponse.getGetCarDetails().getUser().getContact_no());
        request.setEmailId(carStockResponse.getGetCarDetails().getUser().getEmail());

        if(insuranceFor.getSelectedItemPosition()==1) request.setPrevPolicyEndDate(policyEndDate.getText().toString().trim());
        else request.setPrevPolicyEndDate("");

        if (insuranceFor.getSelectedItemPosition()==1 && insuranceClaim.getSelectedItemPosition()==1) request.setNcb(insuranceClaimBonus.getSelectedItem().toString().trim());
        else request.setNcb("");

        return request;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mutexA) registrationManufactureYear.setText("" + year);
        else if(mutexB){
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
            policyEndDate.setText(targetdatevalue);
        }
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }
}
