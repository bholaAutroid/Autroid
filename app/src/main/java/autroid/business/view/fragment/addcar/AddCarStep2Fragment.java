package autroid.business.view.fragment.addcar;


import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
import autroid.business.view.fragment.MonthYearPickerDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarStep2Fragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener{

    LinearLayout mMainLayout;
    TextView mYear;
    CarItemsResponse carItemsResponse;

    AppCompatSpinner mOwner,mInsurance,mAccidential,mServiceHistory;
    EditText mKm,mRegistrationOne,mRegistrationTwo,mRegistrationThree,mRegistrationFour;
    Button mNext;
    String strKm="",strYear="",strRegistration="",strOwner="",strInsurance="",strAccidential="",strServiceHistory="";
    AddCarRequest addCarRequest;

    private String spinnerSelectText[]={ "Select Color", "Select Owner","Select Fuel Type","Select Transmission","Body Style"};
    private TextView mTitle;

    public AddCarStep2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car_step2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);
        addCarRequest= (AddCarRequest) getArguments().getSerializable("AddCarRequest");

        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);

        mOwner= (AppCompatSpinner) view.findViewById(R.id.spn_owner);
        mInsurance= (AppCompatSpinner) view.findViewById(R.id.spn_insurance);
        mAccidential= (AppCompatSpinner) view.findViewById(R.id.spn_accidental);
        mServiceHistory= (AppCompatSpinner) view.findViewById(R.id.spn_service_history);
        mKm= (EditText) view.findViewById(R.id.car_kms);
        mYear= (TextView) view.findViewById(R.id.year);
        mRegistrationOne= (EditText) view.findViewById(R.id.car_registration_no_1);
        mRegistrationTwo= (EditText) view.findViewById(R.id.car_registration_no_2);
        mRegistrationThree= (EditText) view.findViewById(R.id.car_registration_no_3);
        mRegistrationFour= (EditText) view.findViewById(R.id.car_registration_no_4);


        mNext= (Button) view.findViewById(R.id.car_next);
        mNext.setOnClickListener(this);

        mYear.setOnClickListener(this);

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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear.setText(""+year);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        }
    }

    private void setDetails(){

        Gson gson = new Gson();
        final PreferenceManager preferenceManager=PreferenceManager.getInstance();
        String carItems=preferenceManager.getStringPreference(getContext(), Constant.SP_CAR_ITEMS);
        carItemsResponse= gson.fromJson(carItems,
                CarItemsResponse.class);



        AddCarSpinnerAdapter adapterOwner = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text, carItemsResponse.getGetCarItems().getOwner(),spinnerSelectText[1]);
        mOwner.setAdapter(adapterOwner);

        ArrayAdapter<String> adapterInsurance = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_insurance));
        adapterInsurance.setDropDownViewResource(R.layout.layout_spinner_text);
        mInsurance.setAdapter(adapterInsurance);

        ArrayAdapter<String> adapterAccidential = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.car_accidential));
        adapterAccidential.setDropDownViewResource(R.layout.layout_spinner_text);
        mAccidential.setAdapter(adapterAccidential);

        ArrayAdapter<String> adapterHistory = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.service_history));
        adapterHistory.setDropDownViewResource(R.layout.layout_spinner_text);
        mServiceHistory.setAdapter(adapterHistory);

    }


    private boolean validate(){
        boolean flag=true;
            strYear=mYear.getText().toString();
            strKm=mKm.getText().toString();
            strOwner=carItemsResponse.getGetCarItems().getOwner().get(mOwner.getSelectedItemPosition()).getValue();
            strInsurance=mInsurance.getSelectedItem().toString();
            strAccidential=mAccidential.getSelectedItem().toString();
            strServiceHistory=mServiceHistory.getSelectedItem().toString();

           if (strYear.trim().length() == 0) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, "Please select purchase year");
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

        addCarRequest.setOdometer(strKm);
        addCarRequest.setManufacture_year(strYear);
        addCarRequest.setOwner(strOwner);
        addCarRequest.setRegistration_no(strRegistration);
        addCarRequest.setAccidental(strAccidential);
        Bundle bundle=new Bundle();
        bundle.putSerializable("AddCarRequest",addCarRequest);
        ((HomeScreen)getActivity()).addFragment(new AddCarStep3Fragment(), FragmentTags.FRAGMENT_ADD_CAR,true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
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
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_POST_CAR) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}
