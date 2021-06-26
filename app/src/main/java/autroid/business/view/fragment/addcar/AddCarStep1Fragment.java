package autroid.business.view.fragment.addcar;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.addCar.SelectAutomakerActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarStep1Fragment extends Fragment implements View.OnClickListener{

    LinearLayout mMainLayout;
    TextView mAutomakerTitle;
    AppCompatSpinner mColor,mStatus,mFuelType,mTransmission,mBodyStyle;
    EditText mPrice,mMileage;
    Button mNext;
    TextView mTitle;

    String modelID,modelName;
    String makerID,makerName;
    String variantID,variantName;

    String strPrice="",strMileage="",strColor="",strStatus="",strFuelType="",strTransmission="",strBodyStyle="";
    private String spinnerSelectText[]={ "Select Color", "Select Owner","Select Fuel Type","Select Transmission","Body Style"};

    CarItemsResponse carItemsResponse;
    public AddCarStep1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car_step1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);
        mAutomakerTitle= (TextView) view.findViewById(R.id.tv_automaker);
        mAutomakerTitle.setOnClickListener(this);
        mColor= (AppCompatSpinner) view.findViewById(R.id.spn_color);
        mStatus= (AppCompatSpinner) view.findViewById(R.id.spn_status);
        mFuelType= (AppCompatSpinner) view.findViewById(R.id.spn_fueltype);
        mTransmission= (AppCompatSpinner) view.findViewById(R.id.spn_transmission);
        mBodyStyle= (AppCompatSpinner) view.findViewById(R.id.spn_body_style);
        mPrice= (EditText) view.findViewById(R.id.price);
        mMileage= (EditText) view.findViewById(R.id.car_mileage);
        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        mNext= (Button) view.findViewById(R.id.car_next);
        mNext.setOnClickListener(this);

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

        setDetails();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_automaker:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.KEY_AUTOMAKER, carItemsResponse.getGetCarItems().getAutomaker());
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new SelectAutomakerActivity(), "SelectAutomakerActivity", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

                break;
            case R.id.car_next:
                if (validate()) {
                    addCar();
                }
                break;
        }
    }


    private boolean validate(){
        boolean flag=true;
            strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();
            strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
            strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
            strBodyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();

            strPrice=mPrice.getText().toString();
            strMileage=mMileage.getText().toString();
            strStatus=mStatus.getSelectedItem().toString();


            strStatus=mStatus.getSelectedItem().toString();
            strColor=carItemsResponse.getGetCarItems().getColor().get(mColor.getSelectedItemPosition()).getValue();
            strFuelType=carItemsResponse.getGetCarItems().getFuel_type().get(mFuelType.getSelectedItemPosition()).getValue();
            strTransmission=carItemsResponse.getGetCarItems().getTransmissions().get(mTransmission.getSelectedItemPosition()).getValue();
            strBodyStyle=carItemsResponse.getGetCarItems().getBody_style().get(mBodyStyle.getSelectedItemPosition()).getValue();
            strMileage=mMileage.getText().toString();
            strPrice=mPrice.getText().toString();


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
            } else if (strBodyStyle.trim().equalsIgnoreCase(spinnerSelectText[4])) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select Body Style");
            } else if (strMileage.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter mileage");
            } else if (strPrice.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please enter price");
            }




        return flag;
    }

    private void setDetails(){

        Gson gson = new Gson();
        final PreferenceManager preferenceManager=PreferenceManager.getInstance();
        String carItems=preferenceManager.getStringPreference(getContext(),Constant.SP_CAR_ITEMS);
        carItemsResponse= gson.fromJson(carItems,
                CarItemsResponse.class);

        AddCarSpinnerAdapter adapter = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getColor(),spinnerSelectText[0]);

        mColor.setAdapter(adapter);
        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getFuel_type(),spinnerSelectText[2]);

        mFuelType.setAdapter(adapterFuelType);

        AddCarSpinnerAdapter adapterBodyStyle = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getBody_style(),spinnerSelectText[4]);

        mBodyStyle.setAdapter(adapterBodyStyle);

        AddCarSpinnerAdapter adapterTransmission = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getTransmissions(),spinnerSelectText[3]);
        mTransmission.setAdapter(adapterTransmission);


        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_status));
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mStatus.setAdapter(adapterBrand);
    }


    private void  addCar(){
        AddCarRequest addCarRequest=new AddCarRequest();

        //addCarRequest.setPrice(strPrice);
        addCarRequest.setVariant(variantID);
        addCarRequest.setVehicle_color(strColor);
        addCarRequest.setVehicle_status(strStatus);
        addCarRequest.setFuel_type(strFuelType);
        addCarRequest.setTransmission(strTransmission);
        addCarRequest.setVariant(variantID);
        addCarRequest.setBody_style(strBodyStyle);

        addCarRequest.setDescription("");
        addCarRequest.setOdometer("");
        addCarRequest.setLatitude(0.0);
        addCarRequest.setLongitude(0.0);
        addCarRequest.setLocation("");
        addCarRequest.setManufacture_year("");
        addCarRequest.setOwner("");
        addCarRequest.setRegistration_no("");
        addCarRequest.setAccidental("");

        Bundle intent=new Bundle();
        intent.putSerializable("AddCarRequest",addCarRequest);
        if(strStatus.equalsIgnoreCase("Used"))
           ((HomeScreen)getActivity()).addFragment(new AddCarStep2Fragment(), FragmentTags.FRAGMENT_ADD_CAR,true,false,intent,((HomeScreen) getActivity()).currentFrameId);
        else
            ((HomeScreen)getActivity()).addFragment(new AddCarStep3Fragment(), FragmentTags.FRAGMENT_ADD_CAR,true,false,intent,((HomeScreen) getActivity()).currentFrameId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
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
        else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_POST_CAR) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();


        }
    }
}
