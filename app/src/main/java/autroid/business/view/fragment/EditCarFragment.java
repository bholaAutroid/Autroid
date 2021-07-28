package autroid.business.view.fragment;


import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.presenter.AddCarPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.addCar.SelectAutomakerActivity;
import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */

public class EditCarFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener {

    LinearLayout mMainLayout;
    private AddCarPresenter mPresenter;
    TextView mAutomakerTitle,mYear,mAddress;
    CarItemsResponse carItemsResponse;


    String modelID,modelName;
    String makerID,makerName;
    String variantID,variantName;

    AppCompatSpinner mColor,mOwner,mStatus,mFuelType,mTransmission,mInsurance,mAccidential,mServiceHistory,mBodyStyle;
    EditText mKm,mPrice,mMileage,mRegistration,mDescription;

    String strKm="",strYear="",strPrice="",strMileage="",strRegistration="",strDescription="",strColor="",strOwner="",strStatus="",strFuelType="",strTransmission="",strInsurance="",strAccidential="",strServiceHistory="",strBosyStyle="";

    private boolean isUsedCar;
    Button mNext;
    String strLocation;
    LatLng loc;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Realm mRealm;
    RealmController mRealmController;

    private String spinnerSelectText[]={ "Select Color", "Select Owner","Select Fuel Type","Select Transmission","Body Style"};


    public EditCarFragment() {
        // Required empty public constructor
    }

    public static EditCarFragment newInstance() {
        EditCarFragment fragment = new EditCarFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        //mPresenter = new AddCarPresenter(this, mMainLayout);
        mAutomakerTitle= (TextView) view.findViewById(R.id.tv_automaker);
        mAutomakerTitle.setOnClickListener(this);
        mAddress= (TextView) view.findViewById(R.id.tv_address);
        mAddress.setOnClickListener(this);

        mColor= (AppCompatSpinner) view.findViewById(R.id.spn_color);
        mOwner= (AppCompatSpinner) view.findViewById(R.id.spn_owner);
        mStatus= (AppCompatSpinner) view.findViewById(R.id.spn_status);
        mFuelType= (AppCompatSpinner) view.findViewById(R.id.spn_fueltype);
        mTransmission= (AppCompatSpinner) view.findViewById(R.id.spn_transmission);
        mInsurance= (AppCompatSpinner) view.findViewById(R.id.spn_insurance);
        mAccidential= (AppCompatSpinner) view.findViewById(R.id.spn_accidental);

        mBodyStyle= (AppCompatSpinner) view.findViewById(R.id.spn_body_style);
        mServiceHistory= (AppCompatSpinner) view.findViewById(R.id.spn_service_history);

        mRealm=RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();

        ShowroomRealm mShowroomRealm=mRealmController.getShowroomData();
        //mAddress.setText(mShowroomRealm.getLocation());

      /*  if(mShowroomRealm.getLatitude()!=null && mShowroomRealm.getLongitude()!=null )
        loc=new LatLng(mShowroomRealm.getLatitude(),mShowroomRealm.getLongitude());*/

        mKm= (EditText) view.findViewById(R.id.car_kms);
        mYear= (TextView) view.findViewById(R.id.year);
        mPrice= (EditText) view.findViewById(R.id.price);
        mMileage= (EditText) view.findViewById(R.id.car_mileage);
        mRegistration= (EditText) view.findViewById(R.id.car_registration_no);
        mDescription= (EditText) view.findViewById(R.id.car_description);

        mNext= (Button) view.findViewById(R.id.car_next);
        mNext.setOnClickListener(this);

        mYear.setOnClickListener(this);


        mStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code herei
                if(position>0) {
                    String status=mStatus.getSelectedItem().toString();
                    if (status.equalsIgnoreCase("New")){
                        mKm.setEnabled(false);
                        mInsurance.setEnabled(false);
                        mYear.setEnabled(false);
                        mRegistration.setEnabled(false);
                        mAccidential.setEnabled(false);
                        mOwner.setEnabled(false);
                        mServiceHistory.setEnabled(false);
                        isUsedCar=false;
                    }
                    else if (status.equalsIgnoreCase("Used")){
                        mKm.setEnabled(true);
                        mInsurance.setEnabled(true);
                        mYear.setEnabled(true);
                        mRegistration.setEnabled(true);
                        mAccidential.setEnabled(true);
                        mOwner.setEnabled(true);
                        mServiceHistory.setEnabled(true);
                        isUsedCar=true;
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        //getAllItems();
    }

//    private void getAllItems(){
//        mPresenter.getCarItems();
//    }

    public void onItemSuccess(CarItemsResponse carItemsResponse){

        this.carItemsResponse=carItemsResponse;

        AddCarSpinnerAdapter adapter = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getColor(),spinnerSelectText[0]);

        mColor.setAdapter(adapter);

        AddCarSpinnerAdapter adapterOwner = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getOwner(),spinnerSelectText[1]);

        mOwner.setAdapter(adapterOwner);

        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getFuel_type(),spinnerSelectText[2]);

        mFuelType.setAdapter(adapterFuelType);

        AddCarSpinnerAdapter adapterBodyStyle = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getBody_style(),spinnerSelectText[4]);

        mBodyStyle.setAdapter(adapterBodyStyle);

        AddCarSpinnerAdapter adapterTransmission = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getTransmissions(),spinnerSelectText[3]);
        mTransmission.setAdapter(adapterTransmission);

        ArrayAdapter<String> adapterInsurance = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_insurance));
        adapterInsurance.setDropDownViewResource(R.layout.layout_spinner_text);
        mInsurance.setAdapter(adapterInsurance);

        ArrayAdapter<String> adapterAccidential = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_accidential));
        adapterAccidential.setDropDownViewResource(R.layout.layout_spinner_text);
        mAccidential.setAdapter(adapterAccidential);

        ArrayAdapter<String> adapterHistory = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.service_history));
        adapterHistory.setDropDownViewResource(R.layout.layout_spinner_text);
        mServiceHistory.setAdapter(adapterHistory);

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_status));
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mStatus.setAdapter(adapterBrand);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_automaker:
                Intent bundle=new Intent(getActivity(),SelectAutomakerActivity.class);
                bundle.putExtra(Constant.KEY_AUTOMAKER,carItemsResponse.getGetCarItems().getAutomaker());
                ((AwsHomeActivity) getActivity()).startActivity(bundle);

                break;
            case R.id.car_next:
                if(validate()){
                    addCar();
                }
                break;

            case R.id.year:
                MonthYearPickerDialog pd = new MonthYearPickerDialog();
                pd.setListener(this);
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
               // createDialogWithoutDateField().show();
                break;
            case R.id.tv_address:
               // mAddressFrame.setVisibility(View.VISIBLE);
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;

        }
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Broadcast from service","Broadcast From Service: \n");

            if (intent.getAction().equals(Constant.BROADCAST_VARIANT)) {
                makerID=intent.getStringExtra(Constant.KEY_MAKER_ID);
                makerName=intent.getStringExtra(Constant.KEY_MAKER_NAME);
                modelID=intent.getStringExtra(Constant.KEY_MODEL_ID);
                modelName=intent.getStringExtra(Constant.KEY_MODEL_NAME);
                variantID=intent.getStringExtra(Constant.KEY_VARIANT_ID);
                variantName=intent.getStringExtra(Constant.KEY_VARIANT_NAME);

                mAutomakerTitle.setText(makerName+", "+modelName+", "+variantName);

            }

        }

    };

    @Override
    public void onResume() {
        super.onResume();
       // getActivity().registerReceiver(mReceiver, mIntentFilter);

    }

    private boolean validate(){
        boolean flag=true;




        if(isUsedCar) {

            strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();
            strYear=mYear.getText().toString();
            strKm=mKm.getText().toString();
            strOwner=carItemsResponse.getGetCarItems().getOwner().get(mOwner.getSelectedItemPosition()).getValue();
            strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
            strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
            strBosyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();
            strInsurance=mInsurance.getSelectedItem().toString();
            strAccidential=mAccidential.getSelectedItem().toString();
            strPrice=mPrice.getText().toString();
            strMileage=mMileage.getText().toString();
            strRegistration=mRegistration.getText().toString();
            strStatus=mStatus.getSelectedItem().toString();
            strDescription=mDescription.getText().toString();
            strLocation=mAddress.getText().toString();
            strServiceHistory=mServiceHistory.getSelectedItem().toString();

            if (mAutomakerTitle.getText().toString().trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select your car");
            } else if (strStatus.trim().equalsIgnoreCase("Select Car Status")) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter car status");
            } else if (strColor.trim().equalsIgnoreCase(spinnerSelectText[0])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select color");
            } else if (strFuelType.trim().equalsIgnoreCase(spinnerSelectText[2])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select FuelType");
            } else if (strTransmission.trim().equalsIgnoreCase(spinnerSelectText[3])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Transmission");
            } else if (strBosyStyle.trim().equalsIgnoreCase(spinnerSelectText[4])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Body Style");
            } else if (strMileage.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter mileage");
            } else if (strPrice.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter price");
            } else if (strYear.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select purchase year");
            } else if (strRegistration.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter registration number");
            } else if (strKm.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter km driven");
            } else if (strOwner.trim().equalsIgnoreCase(spinnerSelectText[1])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Ownership");
            } else if (strInsurance.trim().equalsIgnoreCase("Insurance")) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter insurance");
            } else if (strAccidential.trim().equalsIgnoreCase("Accidential")) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter accidential");
            } else if (strServiceHistory.trim().equalsIgnoreCase("Service History")) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select service history");
            } else if (strDescription.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter car description");
            } else if (strLocation.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter your address");
            }
        }
        else {


            strStatus=mStatus.getSelectedItem().toString();
            strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();
            strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
            strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
            strBosyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();
            strMileage=mMileage.getText().toString();
            strPrice=mPrice.getText().toString();
            strDescription=mDescription.getText().toString();
            strLocation=mAddress.getText().toString();

            if (mAutomakerTitle.getText().toString().trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select your car");
            } else if (strStatus.trim().equalsIgnoreCase("Select Car Status")) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter car status");
            } else if (strColor.trim().equalsIgnoreCase(spinnerSelectText[0])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select color");
            } else if (strFuelType.trim().equalsIgnoreCase(spinnerSelectText[2])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select FuelType");
            } else if (strTransmission.trim().equalsIgnoreCase(spinnerSelectText[3])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Transmission");
            } else if (strBosyStyle.trim().equalsIgnoreCase(spinnerSelectText[4])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Body Style");
            } else if (strMileage.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter mileage");
            } else if (strPrice.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter price");
            }
            else if (strDescription.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter car description");
            } else if (strLocation.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter your address");
            }
        }



        return flag;
    }

    private void  addCar(){
        AddCarRequest addCarRequest=new AddCarRequest();

        addCarRequest.setDescription(strDescription);
        //addCarRequest.setPrice(strPrice);
        addCarRequest.setOdometer(strKm);
        if(loc!=null) {
            addCarRequest.setLatitude(loc.latitude);
            addCarRequest.setLongitude(loc.longitude);
        }
        else {
            addCarRequest.setLatitude(0.0);
            addCarRequest.setLongitude(0.0);
        }
        addCarRequest.setLocation(strLocation);
        addCarRequest.setManufacture_year(strYear);
        addCarRequest.setOwner(strOwner);
        addCarRequest.setRegistration_no(strRegistration);
        addCarRequest.setVariant(variantID);
        addCarRequest.setVehicle_color(strColor);
        addCarRequest.setVehicle_status(strStatus);
        addCarRequest.setFuel_type(strFuelType);
        addCarRequest.setTransmission(strTransmission);
        addCarRequest.setAccidental(strAccidential);
        addCarRequest.setBody_style(strBosyStyle);

        mPresenter.addCar(addCarRequest);
    }
    public void onAddCarSuccess(AddCarResponse addCarResponse){
            Intent intent=new Intent(getActivity(), AddMultipleImagesActivity.class);
            intent.putExtra(Constant.KEY_ID,addCarResponse.getGetCarResponse().getItem().getId());
            intent.putExtra(Constant.KEY_TYPE,Constant.VALUE_CAR);
            startActivity(intent);
            getActivity().onBackPressed();
    }

   /* @Override
    public void onPause() {
        getActivity().unregisterReceiver(mReceiver);
        super.onPause();
    }*/


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear.setText(""+year);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    /*    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(autocompleteFragment);
        ft.commit();*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mAddress.setText(place.getAddress().toString());
                strLocation=place.getAddress().toString();
                loc=place.getLatLng();
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
