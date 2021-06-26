package autroid.business.view.fragment.carsales;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.GarageCarBE;
import autroid.business.model.bean.InsuranceDataBE;
import autroid.business.model.bean.SpecificationsBE;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.GarageCarResponse;
import autroid.business.presenter.AddCarPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.MonthYearPickerDialog;
import io.realm.Realm;

public class EditCarFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    int currentYear, currentDay, currentMonth;

    private static int COLOR = 1, TRANSMISSION = 2, BODYSTYLE = 3, OWNER = 4;

    double latitude, longitude;

    ConstraintLayout mMainLayout;

    LinearLayout membershipLayout;

    CheckBox member;

    TextView mAutomakerTitle, mYear, mAddress, mInsuranceCompany, mPolicyNumber, mPolicyHolder, mExpire, mPremium, mFuelType;

    Button mNext, mGallery, mDocuments;

    String variantId;

    AppCompatSpinner mInsurance, mColor, mOwner, mTransmission, mAccidental, mBodyStyle;

    EditText mKm, mPrice, mDescription, mRegistration, mPurchasedPrice, mRefurbishmentPrice;

    ImageView mBackNavigation;

    Calendar calendar;

    LinearLayoutManager linearLayoutManager;

    DatePickerDialog datePickerDialog;

    String strRcUrl, strIcUrl, carId, carType;

    AddCarPresenter mPresenter;

    private String spinnerSelectText[] = {"", "", "", "", ""};

    private boolean mutex = false, publish = false, refresh;

    Realm mRealm;

    RealmController mRealmController;

    public EditCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController = RealmController.getInstance();

        mPresenter = new AddCarPresenter(this, mMainLayout);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        viewWork(view);

        getBundleData();

        mPresenter.getCarDetails(carId);
    }

    private void viewWork(View view) {

        mMainLayout = view.findViewById(R.id.main_layout);
        membershipLayout = view.findViewById(R.id.membership_layout);
        member = view.findViewById(R.id.member);
        mAutomakerTitle = view.findViewById(R.id.tv_automaker);
        mAddress = view.findViewById(R.id.tv_address);
        mInsurance = view.findViewById(R.id.spn_insurance);
        mColor = view.findViewById(R.id.spn_color);
        mOwner = view.findViewById(R.id.spn_owner);
        mFuelType = view.findViewById(R.id.car_fuel);
        mTransmission = view.findViewById(R.id.spn_transmission);
        mAccidental = view.findViewById(R.id.spn_accidental);
        mGallery = view.findViewById(R.id.gallery);
        mDocuments = view.findViewById(R.id.documents);
        mBodyStyle = view.findViewById(R.id.spn_body_style);
        mKm = view.findViewById(R.id.car_kms);
        mYear = view.findViewById(R.id.year);
        mPrice = view.findViewById(R.id.price);
        mRegistration = view.findViewById(R.id.car_registration_no);
        mInsuranceCompany = view.findViewById(R.id.insurance_company);
        mPolicyNumber = view.findViewById(R.id.policy_number);
        mPolicyHolder = view.findViewById(R.id.policy_holder);
        mPremium = view.findViewById(R.id.premium);
        mExpire = view.findViewById(R.id.expire);
        mDescription = view.findViewById(R.id.car_description);
        mNext = view.findViewById(R.id.car_next);
        mBackNavigation = view.findViewById(R.id.back_navigation);
        mPurchasedPrice = view.findViewById(R.id.purchased_price);
        mRefurbishmentPrice = view.findViewById(R.id.refurbishment_price);

        //mGallery.setLayoutManager(linearLayoutManager);

        mAddress.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mYear.setOnClickListener(this);
        mExpire.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mDocuments.setOnClickListener(this);
        mBackNavigation.setOnClickListener(this);
    }

    private void getBundleData() {

        Bundle bundle = getArguments();

        carId = bundle.getString(Constant.KEY_CAR_ID);
        carType = bundle.getString(Constant.KEY_TYPE);

        if (carType.equalsIgnoreCase(Constant.EDIT_CAR)) {
            refresh = bundle.getBoolean(Constant.IS_GARAGE_CAR);
            mNext.setText("Save Changes");
            publish = false;
        } else {
            mNext.setText("Sell Car");
            publish = true;
        }

        //setUpData();
    }

    public void onSuccessCarDetail(GarageCarResponse garageCarResponse) {

        variantId = garageCarResponse.getGaragecar().getVariant();
        longitude = garageCarResponse.getGaragecar().getGeometry().get(0);
        latitude = garageCarResponse.getGaragecar().getGeometry().get(1);

        GarageCarBE data = garageCarResponse.getGaragecar();

        mAutomakerTitle.setText(data.getTitle());
        mRegistration.setText(data.getRegistration_no());
        mKm.setText(data.getOdometer());
        mFuelType.setText(data.getFuel_type());

        if (data.getNumericPrice() < 1) mPrice.setText("");
        else mPrice.setText("" + data.getNumericPrice());

        if (data.getPurchase_price() < 1) mPurchasedPrice.setText("");
        else mPurchasedPrice.setText("" + data.getPurchase_price());

        if (data.getRefurbishment_cost() < 1) mRefurbishmentPrice.setText("");
        else mRefurbishmentPrice.setText("" + data.getRefurbishment_cost());

        mYear.setText(data.getManufacture_year());
        mDescription.setText(data.getDescription());

        if (data.getLocation() != null && data.getLocation().length() > 0)
            mAddress.setText(data.getLocation());
        else mAddress.setText(data.getUser().getAddress().getLocation());

        setUpSpinners(COLOR, data.getVehicleColor());
        setUpSpinners(OWNER, data.getOwner());
        setUpSpinners(TRANSMISSION, data.getTransmission());
        setUpSpinners(BODYSTYLE, data.getBodyStyle());

        ArrayAdapter<String> accidental = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, getResources().getStringArray(R.array.car_accidential));
        mAccidental.setAdapter(accidental);
        if (data.getAccidental().equalsIgnoreCase(Constant.YES)) mAccidental.setSelection(1);
        else if (data.getAccidental().equalsIgnoreCase(Constant.NO)) mAccidental.setSelection(2);
        else mAccidental.setSelection(0);

        ArrayAdapter<String> insurance = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, getResources().getStringArray(R.array.insurance));
        mInsurance.setAdapter(insurance);
        if (data.getInsurance().equalsIgnoreCase(Constant.YES)) mInsurance.setSelection(1);
        else if (data.getInsurance().equalsIgnoreCase(Constant.NO)) mInsurance.setSelection(2);
        else mInsurance.setSelection(0);

//        if (data.getInsuranceData() != null) {
//            mInsuranceCompany.setText(data.getInsuranceData().getInsurance_company());
//            mPolicyNumber.setText(data.getInsuranceData().getPolicy_no());
//            mPolicyHolder.setText(data.getInsuranceData().getPolicy_holder());
//            mPremium.setText("" + data.getInsuranceData().getPremium());
//            if (data.getInsuranceData().getExpire() != null) mExpire.setText(data.getInsuranceData().getExpire().substring(0, 10));
//        }

        if (garageCarResponse.getGaragecar().getUser().getPartner() == null) {

            membershipLayout.setVisibility(View.VISIBLE);

            if (garageCarResponse.getGaragecar().getMyPackage() != null) member.setChecked(true);
            else member.setChecked(false);
        }

        if (data.getRc().trim().length() != 0) strRcUrl = data.getRc_address();

        if (data.getIc().trim().length() != 0) strIcUrl = data.getIc_address();

    }

    private void setUpSpinners(int type, ArrayList<SpecificationsBE> arrayList) {

        ArrayList<String> spinnerList = new ArrayList<>();

        int selection = 0;

        for (int i = 0; i < arrayList.size(); i++) {
            spinnerList.add(arrayList.get(i).getValue());
            if (arrayList.get(i).isSelected()) selection = i + 1;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(), R.layout.layout_spinner_remark, spinnerList);

        if (type == COLOR) {
            spinnerList.add(0, spinnerSelectText[0]);
            mColor.setAdapter(adapter);
            mColor.setSelection(selection);
        } else if (type == TRANSMISSION) {
            spinnerList.add(0, spinnerSelectText[3]);
            mTransmission.setAdapter(adapter);
            mTransmission.setSelection(selection);
        } else if (type == OWNER) {
            spinnerList.add(0, spinnerSelectText[1]);
            mOwner.setAdapter(adapter);
            mOwner.setSelection(selection);
        } else if (type == BODYSTYLE) {
            spinnerList.add(0, spinnerSelectText[4]);
            mBodyStyle.setAdapter(adapter);
            mBodyStyle.setSelection(selection);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.car_next:
                if (carType.equalsIgnoreCase(Constant.PUBLISH_CAR) && validatePublishCar())
                    mPresenter.editCar(addPublishCarRequest());
                else if (carType.equalsIgnoreCase(Constant.EDIT_CAR) && validateEditCar())
                    mPresenter.editCar(addEditCarRequest());
                break;

            case R.id.year:
                MonthYearPickerDialog dialog = new MonthYearPickerDialog();
                dialog.setListener(this);
                dialog.show(getFragmentManager(), "MonthYearPickerDialog");
                mutex = true;
                break;

            case R.id.expire:
                mutex = false;
                calendar = Calendar.getInstance();
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                currentMonth = calendar.get(Calendar.MONTH);
                currentYear = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
                break;

            case R.id.tv_address:
                BusinessAddressFragment businessAddressFragment = new BusinessAddressFragment();
                businessAddressFragment.show(getFragmentManager(), "BusinessAddressFragment");
                break;

            case R.id.gallery:
                Bundle galleryBundle = new Bundle();
                galleryBundle.putString(Constant.KEY_CAR_ID, carId);
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new CarImagesFragment(), "CarImagesFragment", true, false, galleryBundle, ((HomeScreen) getActivity()).currentFrameId);
                break;

            case R.id.documents:
                Bundle bundle = new Bundle();
                bundle.putString(Constant.RC_URL, strRcUrl);
                bundle.putString(Constant.IC_URL, strIcUrl);
                bundle.putString(Constant.KEY_CAR_ID, carId);
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new CarDocumentFragment(), "CarDocumentFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                break;

            case R.id.back_navigation:
                getActivity().onBackPressed();
                break;

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    private boolean validatePublishCar() {

        if (mInsurance.getSelectedItem() == null || mColor.getSelectedItem() == null || mTransmission.getSelectedItem() == null || mBodyStyle.getSelectedItem() == null || mOwner.getSelectedItem() == null || mAccidental.getSelectedItem() == null) {
            Utility.showResponseMessage(mMainLayout, "Please refresh screen");
            return false;
        } else if (mAutomakerTitle.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select your car");
            return false;
        } else if (mRegistration.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Invalid Registration Number");
            return false;
        } else if (mInsurance.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select insurance");
            return false;
        } else if (mKm.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter km driven");
            return false;
        } else if (mColor.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select color");
            return false;
        } else if (mFuelType.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select FuelType");
            return false;
        } else if (mTransmission.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select Transmission");
            return false;
        } else if (mBodyStyle.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select Body Style");
            return false;
        } else if (mAccidental.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter accidential");
            return false;
        } else if (mPrice.getText().toString().trim().length() == 0 || Float.parseFloat(mPrice.getText().toString().trim()) == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter valid sale price");
            return false;
        } else if (mYear.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select Manufacture year");
            return false;
        } else if (mOwner.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select Ownership");
            return false;

        }/* else if (mInsuranceCompany.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Insurance Company");
            return false;
        } else if (mPolicyNumber.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Policy Number");
            return false;
        } else if (mPolicyHolder.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Policy Holder");
            return false;
        } else if (mPremium.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Insurance Premium");
            return false;
        } else if (mExpire.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Insurance Expiry");
            return false;
        } */ else if (mAddress.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter your address");
            return false;
        }

        return true;
    }

    private boolean validateEditCar() {

        if (mInsurance.getSelectedItem() == null || mColor.getSelectedItem() == null || mTransmission.getSelectedItem() == null || mBodyStyle.getSelectedItem() == null || mOwner.getSelectedItem() == null || mAccidental.getSelectedItem() == null) {
            Utility.showResponseMessage(mMainLayout, "Please refresh screen");
            return false;
        } else if (mAutomakerTitle.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select your car");
            return false;
        } else if (mRegistration.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Invalid Registration Number");
            return false;
        }

        return true;
    }

    private AddCarRequest addPublishCarRequest() {

        AddCarRequest addCarRequest = new AddCarRequest();
        addCarRequest.setCar(carId);
        addCarRequest.setVariant(variantId);
        addCarRequest.setRegistration_no(mRegistration.getText().toString().trim().toUpperCase());
        addCarRequest.setOdometer(mKm.getText().toString().trim());
        addCarRequest.setVehicle_color(mColor.getSelectedItem().toString().trim());
        addCarRequest.setFuel_type(mFuelType.getText().toString().trim());
        addCarRequest.setTransmission(mTransmission.getSelectedItem().toString().trim());
        addCarRequest.setInsurance(mInsurance.getSelectedItem().toString().trim());
        addCarRequest.setBody_style(mBodyStyle.getSelectedItem().toString().trim());
        addCarRequest.setAccidental(mAccidental.getSelectedItem().toString().trim());
        addCarRequest.setManufacture_year(mYear.getText().toString().trim());
        addCarRequest.setOwner(mOwner.getSelectedItem().toString().trim());
        addCarRequest.setInsuranceData(insuranceData());
        addCarRequest.setDescription(mDescription.getText().toString().trim());
        addCarRequest.setLocation(mAddress.getText().toString().trim());
        addCarRequest.setLatitude(latitude);
        addCarRequest.setLongitude(longitude);
        addCarRequest.setPublish(publish);
        addCarRequest.setPrice(Float.parseFloat(mPrice.getText().toString().trim()));
        if (mRefurbishmentPrice.getText().toString().trim().length() != 0)
            addCarRequest.setRefurbishment_cost(Float.parseFloat(mRefurbishmentPrice.getText().toString().trim()));
        if (mPurchasedPrice.getText().toString().trim().length() != 0)
            addCarRequest.setPurchase_price(Float.parseFloat(mPurchasedPrice.getText().toString().trim()));
        addCarRequest.setVehicle_status("Used");
        addCarRequest.setIs_package(member.isChecked());

        return addCarRequest;
    }

    private AddCarRequest addEditCarRequest() {

        AddCarRequest addCarRequest = new AddCarRequest();
        addCarRequest.setCar(carId);
        addCarRequest.setVariant(variantId);
        addCarRequest.setRegistration_no(mRegistration.getText().toString().trim().toUpperCase());
        addCarRequest.setOdometer(mKm.getText().toString().trim());
        addCarRequest.setManufacture_year(mYear.getText().toString().trim());
        addCarRequest.setFuel_type(mFuelType.getText().toString().trim());
        addCarRequest.setDescription(mDescription.getText().toString().trim());
        addCarRequest.setLocation(mAddress.getText().toString().trim());
        addCarRequest.setLatitude(latitude);
        addCarRequest.setLongitude(longitude);
        addCarRequest.setPublish(publish);
        addCarRequest.setVehicle_status("Used");
        addCarRequest.setIs_package(member.isChecked());


        if (mInsurance.getSelectedItemPosition() == 0) addCarRequest.setInsurance("");
        else if (mInsurance.getSelectedItemPosition() != 0)
            addCarRequest.setInsurance(mInsurance.getSelectedItem().toString().trim());

        if (mColor.getSelectedItemPosition() == 0) addCarRequest.setVehicle_color("");
        else if (mColor.getSelectedItemPosition() != 0)
            addCarRequest.setVehicle_color(mColor.getSelectedItem().toString().trim());

        if (mTransmission.getSelectedItemPosition() == 0) addCarRequest.setTransmission("");
        else if (mTransmission.getSelectedItemPosition() != 0)
            addCarRequest.setTransmission(mTransmission.getSelectedItem().toString().trim());

        if (mBodyStyle.getSelectedItemPosition() == 0) addCarRequest.setBody_style("");
        else if (mBodyStyle.getSelectedItemPosition() != 0)
            addCarRequest.setBody_style(mBodyStyle.getSelectedItem().toString().trim());

        if (mOwner.getSelectedItemPosition() == 0) addCarRequest.setOwner("");
        else if (mOwner.getSelectedItemPosition() != 0)
            addCarRequest.setOwner(mOwner.getSelectedItem().toString().trim());

        if (mAccidental.getSelectedItemPosition() == 0) addCarRequest.setAccidental("");
        else if (mAccidental.getSelectedItemPosition() != 0)
            addCarRequest.setAccidental(mAccidental.getSelectedItem().toString().trim());

        if (mPrice.getText().toString().trim().length() != 0)
            addCarRequest.setPrice(Float.parseFloat(mPrice.getText().toString().trim()));

        if (mRefurbishmentPrice.getText().toString().trim().length() != 0)
            addCarRequest.setRefurbishment_cost(Float.parseFloat(mRefurbishmentPrice.getText().toString().trim()));

        if (mPurchasedPrice.getText().toString().trim().length() != 0)
            addCarRequest.setPurchase_price(Float.parseFloat(mPurchasedPrice.getText().toString().trim()));

        addCarRequest.setInsuranceData(insuranceData());

        return addCarRequest;
    }

    private InsuranceDataBE insuranceData() {
        InsuranceDataBE insuranceData = new InsuranceDataBE();
        insuranceData.setInsurance_company(mInsuranceCompany.getText().toString().trim());
        insuranceData.setPolicy_no(mPolicyNumber.getText().toString().trim());
        insuranceData.setPolicy_holder(mPolicyHolder.getText().toString().trim());
        if (mPremium.getText().toString().trim().length() != 0)
            insuranceData.setPremium(Integer.valueOf(mPremium.getText().toString().trim()));
        else insuranceData.setPremium(0);
        insuranceData.setExpire(mExpire.getText().toString().trim());

        return insuranceData;
    }

    public void onAddCarSuccess(AddCarResponse addCarResponse) {
        Intent intent = new Intent(getActivity(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID, addCarResponse.getGetCarResponse().getItem().getId());
        intent.putExtra(Constant.KEY_TYPE, Constant.VALUE_CAR);
        startActivity(intent);
        getActivity().onBackPressed();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        if (mutex) mYear.setText("" + year);
        else {
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
            mExpire.setText(targetdatevalue);
        }
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    public void onEditSuccess(AddCarResponse addCarResponse) {

        if (!refresh) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_REFRESH_CAR_DETAIL);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            GlobalBus.getBus().post(sendEvent);
        }

        if (carType.equalsIgnoreCase(Constant.EDIT_CAR)) {
            Utility.showResponseMessage(mMainLayout, addCarResponse.getResponseMessage());
            Utility.hideSoftKeyboard(getActivity());
            getActivity().onBackPressed();
        } else if (carType.equalsIgnoreCase(Constant.PUBLISH_CAR)) {
            Utility.hideSoftKeyboard(getActivity());

            new AlertDialog.Builder(getActivity())
                    .setTitle("Success Message")
                    .setMessage(addCarResponse.getResponseMessage())
                    .setPositiveButton("Done", (dialogInterface, which) -> {
                        getActivity().onBackPressed();
                    }).show().setCancelable(false);
        }


        mRealmController.updatePublishedStatus(carId, addCarResponse.getGetCarResponse().getItem().getPublish());
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {

        Intent intent = sendEvent.getEvent();

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_LOCATION) {
            //mAddress.setText(intent.getStringExtra(Constant.KEY_LOCATION));
            //latitude = intent.getDoubleExtra(Constant.Key_lat, 0.0);
            //longitude = intent.getDoubleExtra(Constant.Key_lng, 0.0);
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_RC_UPDATE) {
            strRcUrl = intent.getStringExtra(Constant.LINK);
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_IC_UPDATE) {
            strIcUrl = intent.getStringExtra(Constant.LINK);
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_ADD_ADDRESS) {

            latitude = Double.parseDouble(intent.getStringExtra(Constant.LATITUDE));
            longitude = Double.parseDouble(intent.getStringExtra(Constant.LONGITUDE));

            String address = intent.getStringExtra(Constant.ADDRESS) + ",";

            if (intent.getStringExtra(Constant.LANDMARK).trim().length() > 0)
                address = address + intent.getStringExtra(Constant.LANDMARK) + ",";

            address = address + intent.getStringExtra(Constant.TOWN) + "," + intent.getStringExtra(Constant.STATE) + "," + intent.getStringExtra(Constant.ZIP);

            mAddress.setText(address);
        }
    }
}
