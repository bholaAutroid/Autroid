package autroid.business.view.fragment.jobcard;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.model.bean.InsuranceDataBE;
import autroid.business.utils.Constant;

public class JobsClaimDetails extends DialogFragment{

    String bookingId="";

    TextView insuranceCompany, branchName, policyHolder, policyType, expiryDate, premium, claim, cashless, policyNumber, claimNumber;

    TextView accidentDriver,accidentPlace,accidentDate,accidentTime,spotSurvey,fir,accidentCause;

    InsuranceDataBE insuranceData;

    public JobsClaimDetails() {}

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
        return inflater.inflate(R.layout.fragment_jobs_claim_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        getBundleData();
        setUpInsuranceData();
        setUpAccidentData();
    }

    private void findViews(View view) {
        policyHolder=view.findViewById(R.id.policy_holder_name);
        policyType=view.findViewById(R.id.policy_type);
        insuranceCompany=view.findViewById(R.id.ins_company_name);
        branchName=view.findViewById(R.id.branch);
        expiryDate=view.findViewById(R.id.expiry_date);
        premium=view.findViewById(R.id.premium);
        claim=view.findViewById(R.id.claim);
        cashless=view.findViewById(R.id.cashless);
        policyNumber=view.findViewById(R.id.policy_no);
        claimNumber=view.findViewById(R.id.claim_no);

        accidentDriver=view.findViewById(R.id.accident_driver);
        accidentPlace=view.findViewById(R.id.accident_place);
        accidentDate=view.findViewById(R.id.accident_date);
        accidentTime=view.findViewById(R.id.accident_time);
        spotSurvey=view.findViewById(R.id.spot_survey);
        fir=view.findViewById(R.id.fir);
        accidentCause=view.findViewById(R.id.accident_cause);
    }

    private void getBundleData() {
       insuranceData= (InsuranceDataBE) getArguments().getSerializable(Constant.INSURANCE_DETAILS);
       bookingId=getArguments().getString(Constant.BOOKING_ID);
    }

    private void setUpInsuranceData() {
        policyHolder.setText(insuranceData.getPolicy_holder());

        if(insuranceData.getPolicy_type()!=null) {
            if (!insuranceData.getPolicy_type().equals(""))
                policyType.setText(insuranceData.getPolicy_type());
            else policyType.setText("Not Confirmed Yet");
        }

        insuranceCompany.setText(insuranceData.getInsurance_company());

        if(!insuranceData.getBranch().equals(""))branchName.setText(insuranceData.getBranch());
        else branchName.setText("Not Confirmed Yet");

        if (insuranceData.getExpire()!=null) expiryDate.setText(insuranceData.getExpire().substring(0,10));
        else expiryDate.setText("");

        premium.setText("₹ "+insuranceData.getPremium());
        claim.setText("Yes");

        if(insuranceData.isCashless())cashless.setText("Cashless");
        else cashless.setText("Non Cashless");

        policyNumber.setText(insuranceData.getPolicy_no());

        if(insuranceData.getClaim_no()!=null && !insuranceData.getClaim_no().equals(""))claimNumber.setText(insuranceData.getClaim_no());
        else claimNumber.setText("Not Confirmed Yet");
    }

    private void setUpAccidentData() {
        accidentDriver.setText(insuranceData.getDriver_accident());
        accidentPlace.setText(insuranceData.getAccident_place());
        accidentDate.setText(insuranceData.getAccident_date().substring(0,10));
        accidentTime.setText(insuranceData.getAccident_time());
        spotSurvey.setText(insuranceData.getSpot_survey());
        fir.setText(insuranceData.getFir());
        accidentCause.setText(insuranceData.getAccident_cause());
    }
}
