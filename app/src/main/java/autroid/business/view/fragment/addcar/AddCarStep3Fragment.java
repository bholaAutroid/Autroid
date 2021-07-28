package autroid.business.view.fragment.addcar;


import android.app.Application;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.presenter.AddCarPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarStep3Fragment extends Fragment implements View.OnClickListener, AwsHomeActivity.AddressCallback {
    LinearLayout mMainLayout;
    TextView mAddress,mCurrentLocation;
    EditText mDescription;
    String strDescription="";
    Button mNext;
    String strLocation;
    LatLng loc;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private AddCarPresenter mPresenter;

    AddCarRequest addCarRequest;

    private Realm realm;
    RealmController realmController;
    private TextView mTitle;

    private FirebaseAnalytics mFirebaseAnalytics;


    public AddCarStep3Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car_step3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Car",null);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);
        
        //mPresenter = new AddCarPresenter(this, mMainLayout);
        addCarRequest= (AddCarRequest) getArguments().getSerializable("AddCarRequest");
        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);

        mAddress= (TextView) view.findViewById(R.id.tv_address);
        mAddress.setOnClickListener(this);

        mCurrentLocation=view.findViewById(R.id.tv_current_address);
        mCurrentLocation.setOnClickListener(this);

        mDescription= (EditText) view.findViewById(R.id.car_description);

        mNext= (Button) view.findViewById(R.id.car_next);
        mNext.setOnClickListener(this);

        String address=realmController.getShowroomData().getLocation();
        mAddress.setText(address);
        loc=new LatLng(realmController.getShowroomData().getLatitude(),realmController.getShowroomData().getLongitude());

        mTitle=view.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.add_car));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_address:
                // mAddressFrame.setVisibility(View.VISIBLE);
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.car_next:
                if(validate()){
                    addCar();
                }
                break;
            case R.id.tv_current_address:
                ((AwsHomeActivity)getActivity()).getCurrentLocation(this);
                break;
        }
    }

    private void addCar() {
        addCarRequest.setDescription(strDescription);
        if(loc!=null) {
            addCarRequest.setLatitude(loc.latitude );
            addCarRequest.setLongitude(loc.longitude );
        }
        else {
            addCarRequest.setLatitude(0.0);
            addCarRequest.setLongitude(0.0);
        }
        addCarRequest.setLocation(strLocation);

        mPresenter.addCar(addCarRequest);


    }

    private boolean validate(){
        Boolean flag=true;
        strDescription=mDescription.getText().toString();
        strLocation=mAddress.getText().toString();

        if (strDescription.trim().length() == 0) {
            flag = false;
            Utility.showResponseMessage(mMainLayout, "Please enter car description");
        } else if (strLocation.trim().length() == 0) {
            flag = false;
            Utility.showResponseMessage(mMainLayout, "Please enter your address");
        }

        return flag;
    }

    public void onAddCarSuccess(AddCarResponse carStockResponse){

      
            CarRealm carRealm=new CarRealm();
            carRealm.setCreatedDate(carStockResponse.getGetCarResponse().getItem().getCreated_at());
            carRealm.setFuelType(carStockResponse.getGetCarResponse().getItem().getFuel_type());
            carRealm.setId(carStockResponse.getGetCarResponse().getItem().getId());
            carRealm.setKm(carStockResponse.getGetCarResponse().getItem().getDriven());
            carRealm.setPrice(carStockResponse.getGetCarResponse().getItem().getPrice());
            carRealm.setAccidential(carStockResponse.getGetCarResponse().getItem().getAccidental());
            carRealm.setColor(carStockResponse.getGetCarResponse().getItem().getVehicle_color());
            carRealm.setDescription(carStockResponse.getGetCarResponse().getItem().getDescription());
            carRealm.setInsurance(carStockResponse.getGetCarResponse().getItem().getInsurance());
            carRealm.setMileage(carStockResponse.getGetCarResponse().getItem().getMileage());
            carRealm.setOwnership(carStockResponse.getGetCarResponse().getItem().getOwner());
            carRealm.setBodyStyle(carStockResponse.getGetCarResponse().getItem().getBody_style());
            carRealm.setRegistrationNo(carStockResponse.getGetCarResponse().getItem().getRegistration_no());
            carRealm.setServiceRecord(carStockResponse.getGetCarResponse().getItem().getService_history());
            carRealm.setTransmission(carStockResponse.getGetCarResponse().getItem().getTransmission());
            carRealm.setMaker(carStockResponse.getGetCarResponse().getItem().getMaker());
            carRealm.setModel(carStockResponse.getGetCarResponse().getItem().getModel());
            carRealm.setVariant(carStockResponse.getGetCarResponse().getItem().getVariant());
            carRealm.setVariantId(carStockResponse.getGetCarResponse().getItem().getVariant_id());
            carRealm.setStatus(carStockResponse.getGetCarResponse().getItem().getVehicle_status());
            carRealm.setLatitude(carStockResponse.getGetCarResponse().getItem().getGeometry().get(0));
            carRealm.setLongitude(carStockResponse.getGetCarResponse().getItem().getGeometry().get(1));

            carRealm.setPublish(carStockResponse.getGetCarResponse().getItem().getPublish());

            carRealm.setPublisherAddress(carStockResponse.getGetCarResponse().getItem().getUser().getAddress().getLocation());
            carRealm.setPublisherName(carStockResponse.getGetCarResponse().getItem().getUser().getName());
            carRealm.setTitle(carStockResponse.getGetCarResponse().getItem().getTitle());
            carRealm.setYear(carStockResponse.getGetCarResponse().getItem().getManufacture_year());

            realm.beginTransaction();
            realm.copyToRealm(carRealm);
            realm.commitTransaction();

        Bundle params = new Bundle();
        params.putString("model_name", carStockResponse.getGetCarResponse().getItem().getModelName());
        mFirebaseAnalytics.logEvent("add_car", params);

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_POST_CAR);


        
        Intent intent=new Intent(getActivity(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID,carStockResponse.getGetCarResponse().getItem().getId());
        intent.putExtra(Constant.KEY_TYPE,Constant.VALUE_CAR);
        startActivity(intent);

            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            GlobalBus.getBus().post(sendEvent);

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        //getActivity().onBackPressed();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mAddress.setText(place.getAddress().toString());
                strLocation=place.getAddress().toString();
                loc=place.getLatLng();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        
    }

    @Override
    public void getAddress(String address, Location location) {
        mAddress.setText(address);
        loc=new LatLng(location.getLatitude(),location.getLongitude());
    }
}
