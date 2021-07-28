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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.adapter.EditGalleryAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.OnImageDeleteCallback;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.presenter.EditNewCarPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.addCar.SelectAutomakerActivity;
import autroid.business.view.fragment.MonthYearPickerDialog;
import io.realm.Realm;

public class EditNewCarActivity extends Fragment implements View.OnClickListener,OnImageDeleteCallback,DatePickerDialog.OnDateSetListener{

    AppCompatSpinner mColor,mFuelType,mTransmission,mBodyStyle;
    EditText mPrice,mMileage,mDescription;
    RecyclerView mGallery;
    Button mSave,mAddPhotos;
    EditNewCarPresenter mPresenter;
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

    String strPrice,strMileage,strDescription,strColor,strLocation,strFuelType,strTransmission,strBodyStyle;

    private String spinnerSelectText[]={ "Select Color", "Select Owner", "Select Maker","Select Model", "Select Variant","Select Fuel Type","Select Transmission"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_edit_new_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Edit Car Details");


    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
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
    private void init(View view){

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mAutomakerTitle= (TextView) view.findViewById(R.id.tv_automaker);
        mAutomakerTitle.setOnClickListener(this);

        mColor= (AppCompatSpinner) view.findViewById(R.id.spn_color);
        mFuelType=(AppCompatSpinner)view.findViewById(R.id.spn_fueltype);
        mTransmission=(AppCompatSpinner)view.findViewById(R.id.spn_transmission);
        mBodyStyle= (AppCompatSpinner) view.findViewById(R.id.spn_body_style);

        mPrice= (EditText) view.findViewById(R.id.price);
        mMileage= (EditText) view.findViewById(R.id.car_mileage);
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
        mPresenter=new EditNewCarPresenter(this,mMainLayout);

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
        mMileage.setText(carRealm.getMileage());
        mDescription.setText(carRealm.getDescription());
        variantID=carRealm.getVariant();

        mAddress.setText(carRealm.getPublisherAddress());
        loc=new LatLng(Double.valueOf(carRealm.getLatitude()),Double.valueOf(carRealm.getLongitude()));

        editGalleryAdapter=new EditGalleryAdapter(carRealm.getMedia(),true,this);
        mGallery.setAdapter(editGalleryAdapter);

        AddCarSpinnerAdapter adapter = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getColor(),spinnerSelectText[0]);
        mColor.setAdapter(adapter);


        mAutomakerTitle.setText(carRealm.getTitle());


        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getFuel_type(),spinnerSelectText[5]);

        mFuelType.setAdapter(adapterFuelType);

        AddCarSpinnerAdapter adapterTransmission = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,carItemsResponse.getGetCarItems().getTransmissions(),spinnerSelectText[6]);
        mTransmission.setAdapter(adapterTransmission);

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


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                ((AwsHomeActivity) getActivity()).addFragment(new SelectAutomakerActivity(), "SelectAutomakerActivity", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;
        }
    }

    @Override
    public void onDeleteClick(String id) {
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

    public void onDeleteSuccess(BaseResponse baseResponse, String imageId){
        realmController.deleteCarImage(stockID,imageId);
        editGalleryAdapter.notifyDataSetChanged();
    }

    public void onEditSuccess(AddCarResponse addCarResponse){
//        Utility.showResponseMessage(mMainLayout,addCarResponse.getResponseMessage());
//        realmController.updateCar(stockID,addCarResponse,Constant.VALUE_CAR);
    }

    private void  addCar(){
        AddCarRequest addCarRequest=new AddCarRequest();

        addCarRequest.setDescription(strDescription);
        //addCarRequest.setPrice(strPrice);
        addCarRequest.setOdometer("");
        addCarRequest.setLatitude(loc.latitude);
        addCarRequest.setLongitude(loc.longitude);
        addCarRequest.setLocation(strLocation);
        addCarRequest.setManufacture_year("");
        addCarRequest.setOwner("");
        addCarRequest.setRegistration_no("");
        addCarRequest.setVariant(variantID);
        addCarRequest.setVehicle_color(strColor);
        addCarRequest.setVehicle_status("New");
        addCarRequest.setCar(stockID);
        addCarRequest.setFuel_type(strFuelType);
        addCarRequest.setTransmission(strTransmission);
        addCarRequest.setAccidental("");
        addCarRequest.setBody_style(strBodyStyle);

        mPresenter.editCar(addCarRequest);
    }

    private boolean validate(){
        boolean flag=true;
        strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();

        strPrice=mPrice.getText().toString();
        strMileage=mMileage.getText().toString();
        strDescription=mDescription.getText().toString();
        strLocation=mAddress.getText().toString();
        strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
        strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
        strBodyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();


       if(strColor.trim().equalsIgnoreCase(spinnerSelectText[0])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select color");
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
        else if(strDescription.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter car description");
        }
        else if(strLocation.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter your address");
        }

        return flag;
    }


}
