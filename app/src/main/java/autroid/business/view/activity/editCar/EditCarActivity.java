package autroid.business.view.activity.editCar;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.adapter.EditGalleryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.OnImageDeleteCallback;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.presenter.EditCarPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.addCar.SelectAutomakerActivity;
import autroid.business.view.fragment.MonthYearPickerDialog;
import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class EditCarActivity extends Fragment implements View.OnClickListener,OnImageDeleteCallback,DatePickerDialog.OnDateSetListener {

    AppCompatSpinner mColor,mOwner,mStatus,mFuelType,mTransmission,mInsurance,mAccidential,mServiceHistory,mBodyStyle;
    EditText mKm,mPrice,mMileage,mDescription,mYear,mRegistrationOne,mRegistrationTwo,mRegistrationThree,mRegistrationFour;
    RecyclerView mGallery;
    Button mSave,mAddPhotos;
    EditCarPresenter mPresenter;
    RelativeLayout mMainLayout;
    TextView mAddress;

    String stockID;

    CarItemsResponse carItemsResponse;


    String modelID,modelName;
    String makerID,makerName;
    String variantID,variantName;

    LatLng loc;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;


    private Realm realm;
    RealmController realmController;
    TextView mAutomakerTitle;

    EditGalleryAdapter editGalleryAdapter;

    String strKm,strYear,strPrice,strMileage,strRegistration,strDescription,strColor,strOwner,strStatus,strLocation,strFuelType,strTransmission,strInsurance,strAccidential,strServiceHistory,strBodyStyle;

    private String spinnerSelectText[]={ "Select Color", "Select Owner", "Select Maker","Select Model", "Select Variant","Select Fuel Type","Select Transmission"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_edit_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Edit Car Details");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getActivity(),"BAck Clicked",Toast.LENGTH_SHORT).show();
         //   onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void init(View view){

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mAutomakerTitle= (TextView) view.findViewById(R.id.tv_automaker);
        mAutomakerTitle.setOnClickListener(this);

        mColor= (AppCompatSpinner) view.findViewById(R.id.spn_color);
        mOwner= (AppCompatSpinner) view.findViewById(R.id.spn_owner);
        mStatus= (AppCompatSpinner) view.findViewById(R.id.spn_status);
        mFuelType=(AppCompatSpinner) view.findViewById(R.id.spn_fueltype);
        mTransmission=(AppCompatSpinner) view.findViewById(R.id.spn_transmission);
        mInsurance=(AppCompatSpinner) view.findViewById(R.id.spn_insurance);
        mAccidential=(AppCompatSpinner) view.findViewById(R.id.spn_accidental);
        mServiceHistory= (AppCompatSpinner) view.findViewById(R.id.spn_service_history);
        mBodyStyle= (AppCompatSpinner) view.findViewById(R.id.spn_body_style);

        mKm= (EditText) view.findViewById(R.id.car_kms);
        mYear= (EditText) view.findViewById(R.id.year);
        mYear.setOnClickListener(this);
        mPrice= (EditText) view.findViewById(R.id.price);
        mMileage= (EditText) view.findViewById(R.id.car_mileage);
        mRegistrationOne= (EditText) view.findViewById(R.id.car_registration_no_1);
        mRegistrationTwo= (EditText) view.findViewById(R.id.car_registration_no_2);
        mRegistrationThree= (EditText) view.findViewById(R.id.car_registration_no_3);
        mRegistrationFour= (EditText) view.findViewById(R.id.car_registration_no_4);
        mDescription= (EditText) view.findViewById(R.id.car_description);

        mAddress= (TextView) view.findViewById(R.id.tv_address);
        mAddress.setOnClickListener(this);

        mSave= (Button) view.findViewById(R.id.car_save);
        mSave.setOnClickListener(this);


        mAddPhotos= (Button) view.findViewById(R.id.add_car_photos);
        mAddPhotos.setOnClickListener(this);

        mGallery=view.findViewById(R.id.gallery);
        LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getActivity());
        llmGallery.setOrientation(LinearLayoutManager.HORIZONTAL);

        mGallery.setLayoutManager(llmGallery);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new EditCarPresenter(this,mMainLayout);

        Bundle intent=getArguments();
        if(intent!=null){
            stockID=intent.getString(Constant.KEY_ID);
        }

        onDetailSuccess();


    }



    public void onDetailSuccess(){

        Gson gson = new Gson();
        final PreferenceManager preferenceManager=PreferenceManager.getInstance();
        String carItems=preferenceManager.getStringPreference(getActivity(),Constant.SP_CAR_ITEMS);
        carItemsResponse= gson.fromJson(carItems,
                CarItemsResponse.class);

        CarRealm carRealm=realmController.getCar(stockID);

        mPrice.setText(carRealm.getPriceNumeric());
        mYear.setText(carRealm.getYear());
        mKm.setText(carRealm.getKm());
        mMileage.setText(carRealm.getMileage());

        String registration[]=carRealm.getRegistrationNo().split(" ");
        if(registration!=null){
            if(registration.length==4){
                mRegistrationOne.setText(registration[0]);
                mRegistrationTwo.setText(registration[1]);
                mRegistrationThree.setText(registration[2]);
                mRegistrationFour.setText(registration[3]);
            }
        }
        mDescription.setText(carRealm.getDescription());
        variantID=carRealm.getVariant();

        mAddress.setText(carRealm.getPublisherAddress());
        loc=new LatLng(Double.valueOf(carRealm.getLatitude()),Double.valueOf(carRealm.getLongitude()));

        editGalleryAdapter=new EditGalleryAdapter(carRealm.getMedia(),true,this);
        mGallery.setAdapter(editGalleryAdapter);

        AddCarSpinnerAdapter adapter = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getColor(),spinnerSelectText[0]);
        mColor.setAdapter(adapter);

        AddCarSpinnerAdapter adapterOwner = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getOwner(),spinnerSelectText[1]);
        mOwner.setAdapter(adapterOwner);

        mAutomakerTitle.setText(carRealm.getTitle());


        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_status));
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mStatus.setAdapter(adapterBrand);

        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getFuel_type(),spinnerSelectText[5]);

        mFuelType.setAdapter(adapterFuelType);

        AddCarSpinnerAdapter adapterTransmission = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getTransmissions(),spinnerSelectText[6]);
        mTransmission.setAdapter(adapterTransmission);

        ArrayAdapter<String> adapterInsurance = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_insurance));
        adapterInsurance.setDropDownViewResource(R.layout.layout_spinner_text);
        mInsurance.setAdapter(adapterInsurance);

        ArrayAdapter<String> adapterAccidential = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_accidential));
        adapterAccidential.setDropDownViewResource(R.layout.layout_spinner_text);
        mAccidential.setAdapter(adapterAccidential);

        String fuelType=carRealm.getFuelType();
        for(int i=0;i<carItemsResponse.getGetCarItems().getFuel_type().size();i++){
            if(fuelType.length() > 0)
                if(fuelType.trim().equalsIgnoreCase(carItemsResponse.getGetCarItems().getFuel_type().get(i).getValue())){
                    mFuelType.setSelection(i);
                }
        }

        String transmission=carRealm.getTransmission();
        for(int i=0;i<carItemsResponse.getGetCarItems().getTransmissions().size();i++){
            if(transmission.length() > 0)
                if(transmission.trim().equalsIgnoreCase(carItemsResponse.getGetCarItems().getTransmissions().get(i).getValue())){
                    mTransmission.setSelection(i);
                }
        }

        ArrayAdapter<String> adapterHistory = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.service_history));
        adapterHistory.setDropDownViewResource(R.layout.layout_spinner_text);
        mServiceHistory.setAdapter(adapterHistory);

        if(true){
            String type=carRealm.getServiceRecord();
            String arr[]=getResources().getStringArray(R.array.service_history);
            for(int i=0;i<arr.length;i++){
                if(type.length() > 0)
                    if(type.equalsIgnoreCase(arr[i])){
                        mServiceHistory.setSelection(i);
                    }
            }
        }

        if(true){
            String type=carRealm.getInsurance();
            String arr[]=getResources().getStringArray(R.array.car_insurance);
            for(int i=0;i<arr.length;i++){
                if(type.length() > 0)
                    if(type.equalsIgnoreCase(arr[i])){
                        mInsurance.setSelection(i);
                    }
            }
        }

        if(true){
            String type=carRealm.getAccidential();
            String arr[]=getResources().getStringArray(R.array.car_accidential);
            for(int i=0;i<arr.length;i++){
                if(type.length() > 0)
                    if(type.equalsIgnoreCase(arr[i])){
                        mAccidential.setSelection(i);
                    }
            }
        }

        AddCarSpinnerAdapter adapterBodyStyle = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getBody_style(),spinnerSelectText[4]);
        mBodyStyle.setAdapter(adapterBodyStyle);

        String bodyStyle=carRealm.getBodyStyle();
        for(int i=0;i<carItemsResponse.getGetCarItems().getBody_style().size();i++){
            if(bodyStyle.length() > 0)
                if(bodyStyle.equalsIgnoreCase(carItemsResponse.getGetCarItems().getBody_style().get(i).getValue())){
                    mBodyStyle.setSelection(i);
                }
        }

        String color=carRealm.getColor();
        for(int i=0;i<carItemsResponse.getGetCarItems().getColor().size();i++){
            if(color.length() > 0)
                if(color.equalsIgnoreCase(carItemsResponse.getGetCarItems().getColor().get(i).getValue())){
                    mColor.setSelection(i);
                }
        }

        String owner=carRealm.getOwnership();
        for(int i=0;i<carItemsResponse.getGetCarItems().getOwner().size();i++){
            if(owner.length() > 0)
                if(owner.equalsIgnoreCase(carItemsResponse.getGetCarItems().getOwner().get(i).getValue())){
                    mOwner.setSelection(i);
                }
        }

        String type=carRealm.getStatus();
        String arr[]=getResources().getStringArray(R.array.car_status);
        for(int i=0;i<arr.length;i++){
            if(type.length() > 0)
                if(type.equalsIgnoreCase(arr[i])){
                    mStatus.setSelection(i);
                }
        }

    }



    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.car_save:
                    if(validate()){
                        addCar();
                    }
                    break;
                case R.id.add_car_photos: {
                    Intent intent = new Intent(getActivity(), AddMultipleImagesActivity.class);
                    intent.putExtra(Constant.KEY_ID, stockID);
                    intent.putExtra(Constant.KEY_TYPE, Constant.VALUE_CAR);
                    startActivity(intent);

                    break;
                }
                case R.id.year:
                    MonthYearPickerDialog pd = new MonthYearPickerDialog();
                    pd.setListener(this);
                    pd.show(getChildFragmentManager(), "MonthYearPickerDialog");
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
                case R.id.tv_automaker:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.KEY_AUTOMAKER, carItemsResponse.getGetCarItems().getAutomaker());
                    ((HomeScreen) getActivity()).makeDrawerVisible();
                    ((HomeScreen) getActivity()).addFragment(new SelectAutomakerActivity(), "SelectAutomakerActivity", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                    break;
            }
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SELECT_CAR) {
            makerID=intent.getStringExtra(Constant.KEY_MAKER_ID);
            makerName=intent.getStringExtra(Constant.KEY_MAKER_NAME);
            modelID=intent.getStringExtra(Constant.KEY_MODEL_ID);
            modelName=intent.getStringExtra(Constant.KEY_MODEL_NAME);
            variantID=intent.getStringExtra(Constant.KEY_VARIANT_ID);
            variantName=intent.getStringExtra(Constant.KEY_VARIANT_NAME);

            mAutomakerTitle.setText(makerName+", "+modelName+", "+variantName);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mAddress.setText(place.getAddress().toString());
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



    public void onDeleteSuccess(BaseResponse baseResponse,String imageId){
        realmController.deleteCarImage(stockID,imageId);
        editGalleryAdapter.notifyDataSetChanged();
    }

    private boolean validate(){
        boolean flag=true;
        strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();
        strYear=mYear.getText().toString();
        strKm=mKm.getText().toString();
        strOwner=carItemsResponse.getGetCarItems().getOwner().get(mOwner.getSelectedItemPosition()).getValue();
        strPrice=mPrice.getText().toString();
        strMileage=mMileage.getText().toString();

        strStatus=mStatus.getSelectedItem().toString();
        strDescription=mDescription.getText().toString();
        strLocation=mAddress.getText().toString();
        strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
        strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
        strInsurance=mInsurance.getSelectedItem().toString();
        strAccidential=mAccidential.getSelectedItem().toString();
        strServiceHistory=mServiceHistory.getSelectedItem().toString();
        strBodyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();


       if(strKm.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter km driven");
        }
        else if(strYear.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select purchase year");
        }
        else if(strColor.trim().equalsIgnoreCase(spinnerSelectText[0])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select color");
        }
        else if(strOwner.trim().equalsIgnoreCase(spinnerSelectText[1])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select Ownership");
        }
        else if(strFuelType.trim().equalsIgnoreCase(spinnerSelectText[5])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select FuelType");
        }
        else if(strTransmission.trim().equalsIgnoreCase(spinnerSelectText[6])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select Transmission");
        }
        else if(strPrice.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter price");
        }
        else if(strMileage.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter mileage");
        }
        else if(strStatus.trim().equalsIgnoreCase("Select Car Status")){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter car status");
        }
        else if(strInsurance.trim().equalsIgnoreCase("Insurance")){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter insurance");
        }
        else if(strAccidential.trim().equalsIgnoreCase("Accidential")){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter accidential");
        }
        else if(strDescription.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter car description");
        }
        else if(strLocation.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter your address");
        }
       else {
           String registration1=mRegistrationOne.getText().toString();
           String registration2=mRegistrationTwo.getText().toString();
           String registration3=mRegistrationThree.getText().toString();
           String registration4=mRegistrationFour.getText().toString();

           if(!(registration1.length()==2 && registration4.length()==4 && registration2.length()!=0 && registration3.length()!=0)){
               flag=false;
               Utility.showResponseMessage(mMainLayout,"Please enter valid registration number");
           }
           else {
               strRegistration=mRegistrationOne.getText().toString()+" "+mRegistrationTwo.getText().toString()+" "+mRegistrationThree.getText().toString()+" "+mRegistrationFour.getText().toString();
           }
       }

        return flag;
    }

    private void  addCar(){
        AddCarRequest addCarRequest=new AddCarRequest();

        addCarRequest.setDescription(strDescription);
        //addCarRequest.setPrice(strPrice);
        addCarRequest.setOdometer(strKm);
        addCarRequest.setLatitude(loc.latitude);
        addCarRequest.setLongitude(loc.longitude);
        addCarRequest.setLocation(strLocation);
        addCarRequest.setManufacture_year(strYear);
        addCarRequest.setOwner(strOwner);
        addCarRequest.setRegistration_no(strRegistration);
        addCarRequest.setVariant(variantID);
        addCarRequest.setVehicle_color(strColor);
        addCarRequest.setVehicle_status(strStatus);
        addCarRequest.setCar(stockID);
        addCarRequest.setFuel_type(strFuelType);
        addCarRequest.setTransmission(strTransmission);
        //addCarRequest.setInsurance(strInsurance);
        addCarRequest.setAccidental(strAccidential);
        addCarRequest.setBody_style(strBodyStyle);

        mPresenter.editCar(addCarRequest);
    }

    public void onEditSuccess(AddCarResponse addCarResponse){
//        Utility.showResponseMessage(mMainLayout,addCarResponse.getResponseMessage());
//        realmController.updateCar(stockID,addCarResponse,Constant.KEY_CAR_TYPE);
        }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear.setText(""+year);
    }

    @Override
    public void onDeleteClick(String id) {
        Log.i("Image position",id+"");
        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        mPresenter.removeCarPic(publishUnpublishRequest,id);
    }

    @Override
    public void onDeleteImageClickId(String id) {

    }

    @Override
    public void onAddImageClick(int position) {
        Intent intent = new Intent(getActivity(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID, stockID);
        intent.putExtra(Constant.KEY_TYPE, Constant.VALUE_CAR);
        startActivity(intent);
    }
}
