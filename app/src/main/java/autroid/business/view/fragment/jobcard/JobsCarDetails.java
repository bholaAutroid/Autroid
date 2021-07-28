package autroid.business.view.fragment.jobcard;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.camera.DisplayGridFragment;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.bean.InspectionDataBE;
import autroid.business.model.bean.InspectionImageBE;
import autroid.business.model.bean.InsuranceDataBE;
import autroid.business.model.bean.ParticularsDataBE;
import autroid.business.model.request.UpdateAssetsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.model.response.CarInspectionResponse;
import autroid.business.presenter.jobcard.JobCardImagesPresenter;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;

public class JobsCarDetails extends DialogFragment implements View.OnClickListener {

    RelativeLayout mainLayout;

    CardView insuranceCardView, imagesCardView;

    TextView carName, carReg, carVin, carEng, policyHolder, companyName, policyNo, premium, expire, assetValue, odometer, fuel, insuranceEdit, assetEdit, carEdit;

    ArrayList<String> assets;

    CarDetailBE carDetail;

    InsuranceDataBE insuranceData;

    JobCardImagesPresenter presenter;

    long fuelValue, odometerValue;

    String bookingId = "", otherAssets;

    BookingDetailsResponse bookingDetailsResponse;

    AlertDialog alertDialog;

    private Boolean isView = false;

    public JobsCarDetails() {
        // Required empty public constructor
    }

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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_jobs_car_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);

        getBundleData();
        setUpData();

        imagesCardView.setOnClickListener(v -> {
            presenter.getCarImages(bookingId);
        });

    }

    private void findViews(View view) {

        mainLayout = view.findViewById(R.id.main_layout);
        carName = view.findViewById(R.id.car_name);
        carReg = view.findViewById(R.id.reg_no);
        carVin = view.findViewById(R.id.vin_no);
        carEng = view.findViewById(R.id.eng_no);
        policyHolder = view.findViewById(R.id.policy_holder);
        companyName = view.findViewById(R.id.company_name);
        policyNo = view.findViewById(R.id.policy_no);
        premium = view.findViewById(R.id.premium);
        expire = view.findViewById(R.id.expire);
        assetValue = view.findViewById(R.id.asset_name);
        odometer = view.findViewById(R.id.odometer);
        fuel = view.findViewById(R.id.fuel);
        insuranceCardView = view.findViewById(R.id.layout_remarks2);
        imagesCardView = view.findViewById(R.id.layout_remarks4);
        insuranceEdit = view.findViewById(R.id.insurance_edit);
        carEdit = view.findViewById(R.id.car_edit);
        assetEdit = view.findViewById(R.id.assets_edit);
    }



    private void getBundleData() {
        isView = getArguments().getBoolean(Constant.KEY_IS_VIEW);
        bookingDetailsResponse = (BookingDetailsResponse) getArguments().getSerializable(Constant.RESPONSE);
        bookingId = getArguments().getString(Constant.BOOKING_ID);
        carDetail = bookingDetailsResponse.getBookingData().get(0).getCarDetail();
        if (bookingDetailsResponse.getBookingData().get(0).getInsuranceData() != null && !bookingDetailsResponse.getBookingData().get(0).getInsuranceData().isClaim())
            insuranceData = bookingDetailsResponse.getBookingData().get(0).getInsuranceData();
        assets = validAssets(bookingDetailsResponse.getBookingData().get(0).getAssets());
        fuelValue = bookingDetailsResponse.getBookingData().get(0).getFuel();
        odometerValue = bookingDetailsResponse.getBookingData().get(0).getOdometer();
        otherAssets = bookingDetailsResponse.getBookingData().get(0).getOther_assets();

        if (isView) {
            insuranceEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            assetEdit.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            imagesCardView.setVisibility(View.GONE);
        } else {
            insuranceEdit.setOnClickListener(this);
            //assetEdit.setOnClickListener(this);
            carEdit.setOnClickListener(this);
        }

    }

    private void setUpData() {

        presenter = new JobCardImagesPresenter(this, mainLayout);

        if (carDetail != null) {
            carName.setText(carDetail.getTitle());
            carReg.setText(carDetail.getRegistration_no());
            carVin.setText(carDetail.getVin());
            carEng.setText(carDetail.getEngine_no());
        }

        if (insuranceData != null) {
            policyHolder.setText(insuranceData.getPolicy_holder());
            companyName.setText(insuranceData.getInsurance_company());
            policyNo.setText(insuranceData.getPolicy_no());
            premium.setText("₹ " + insuranceData.getPremium());
            if (insuranceData.getExpire() != null)
                expire.setText(insuranceData.getExpire());
            else expire.setText("");
        } else insuranceCardView.setVisibility(View.GONE);

        if (assets.size() > 0)
            for (String value : assets) assetValue.append("\u2022 " + value + "\n");

        if (!otherAssets.equals("")) assetValue.append("\u2022 " + otherAssets);
        else if (assets.size() == 0 && otherAssets.equals("")) assetValue.setText("No Assets");

        fuel.setText(fuelValue + " %");
        odometer.setText(odometerValue + " Km");
    }

    private ArrayList<String> validAssets(ArrayList<ParticularsDataBE> assets) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ParticularsDataBE data : assets) if (data.getChecked()) arrayList.add(data.getValue());
        return arrayList;
    }

    public void onSuccess(CarInspectionResponse response) {

        getDialog().dismiss();

        ArrayList<InspectionImageBE> arrayList = new ArrayList<>();

        for (InspectionDataBE data : response.getInspectionData()) {
            InspectionImageBE image = new InspectionImageBE();
            image.setImgUrl(data.getImageAddress());
            image.setImgId(data.getImageId());
            image.setIndex(data.getIndex());
            arrayList.add(image);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("inspection_list", arrayList);
        ((AwsHomeActivity) getActivity()).addFragment(new DisplayGridFragment(), "DisplayGrid", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);

    }

    public void onSuccessAssetsUpdate(BaseResponse response) {
        alertDialog.dismiss();
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insurance_edit:
                if (insuranceData != null) {
                }
                break;

            case R.id.assets_edit:
                updateAssets(bookingId);
                break;

            case R.id.car_edit:
                if (carDetail != null) {

                }
                break;
        }
    }

    private void updateAssets(String bookingId) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reject_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Assets");
        builder.setView(view);
        builder.setNegativeButton("Cancel", (dialogInterface, which) -> dialogInterface.dismiss());
        builder.setPositiveButton("Update", ((dialogInterface, which) -> {
        }));
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            EditText text = view.findViewById(R.id.remarks);
            if (text.getText().toString().trim().length() != 0) {
                UpdateAssetsRequest updateAssests = new UpdateAssetsRequest();
                updateAssests.setBookingId(bookingId);
                updateAssests.setAssets(text.getText().toString().trim());
                presenter.updateAssets(updateAssests);
            }
        });
    }
}
