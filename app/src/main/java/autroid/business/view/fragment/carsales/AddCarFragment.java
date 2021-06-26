package autroid.business.view.fragment.carsales;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.realm.CarStockRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.response.AddCarResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.presenter.carsales.AddCarBookingPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.leads.LeadInsuranceCarDetailsFragment;
import autroid.business.view.fragment.search.SearchCarFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCarFragment extends Fragment implements View.OnClickListener {

    String variantId, variantName, usedId;

    TextView mAutomakerTitle;

    EditText registration;

    ImageView backNavigation;

    Button mNext;

    ConstraintLayout mMainLayout;

    Realm realm;

    RealmController realmController;

    AddCarBookingPresenter mPresenter;

    CarItemsResponse carItemsResponse;

    String carType;

    boolean isLeadInsurance;

    private FirebaseAnalytics mFirebaseAnalytics;

    public AddCarFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_car_booking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Car", null);

        ButterKnife.bind(this, view);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());

        mAutomakerTitle = view.findViewById(R.id.tv_automaker);
        mAutomakerTitle.setOnClickListener(this);

        if (getArguments() != null && getArguments().getBoolean(Constant.VALUE)) {
            isLeadInsurance = true;
            mAutomakerTitle.setText(getArguments().getString(Constant.KEY_VARIANT_NAME));
            variantId = getArguments().getString(Constant.KEY_VARIANT_ID);
            usedId = getArguments().getString(Constant.USER_ID);
            mAutomakerTitle.setEnabled(false);
        }


        registration = view.findViewById(R.id.car_registration);

        backNavigation = view.findViewById(R.id.back_navigation);
        backNavigation.setOnClickListener(this);

        mNext = view.findViewById(R.id.car_next);
        mNext.setOnClickListener(this);

        mMainLayout = view.findViewById(R.id.main_layout);
        mMainLayout.setOnClickListener(this);

        mPresenter = new AddCarBookingPresenter(this, mMainLayout);

        Bundle bundle = getArguments();
        if (bundle != null) carType = bundle.getString(Constant.KEY_CAR_TYPE);

        onItemSuccess();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {

        Intent intent = sendEvent.getEvent();

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_ADD_CAR) {
            variantId = intent.getStringExtra(Constant.KEY_VARIANT_ID);
            variantName = intent.getStringExtra(Constant.KEY_VARIANT_NAME);
            mAutomakerTitle.setText(variantName);
            registration.requestFocus();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_automaker:

                Bundle bundle = new Bundle();
                bundle.putBoolean(Constant.IS_GARAGE_CAR, true);
                // bundle.putSerializable(Constant.KEY_AUTOMAKER, carItemsResponse.getGetCarItems().getAutomaker());
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new SearchCarFragment(), "SearchCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
               /* Intent bundle = new Intent(getActivity(), CarAutomakerFragment.class);
                bundle.putExtra(Constant.KEY_AUTOMAKER, carItemsResponse.getGetCarItems().getAutomaker());
                getActivity().startActivity(bundle);*/

                break;

            case R.id.car_next:
                if (validate() && !isLeadInsurance) addCar();
                else if (validate() && isLeadInsurance) addUserCar();
                break;

            case R.id.main_layout:
                Utility.hideSoftKeyboard(getActivity());
                break;

            case R.id.back_navigation:
                getActivity().onBackPressed();
                break;
        }
    }

    private boolean validate() {

        if (mAutomakerTitle.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please select your car");
            return false;
        } else if (registration.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter valid registration number");
            return false;
        }

        return true;
    }

    private void addCar() {
        AddCarRequest addCarRequest = new AddCarRequest();
        addCarRequest.setRegistration_no(registration.getText().toString().trim().toUpperCase());
        addCarRequest.setVariant(variantId);
        mPresenter.addCar(addCarRequest);
    }

    private void addUserCar() {
        AddCarRequest addCarRequest = new AddCarRequest();
        addCarRequest.setRegistration_no(registration.getText().toString().trim().toUpperCase());
        addCarRequest.setVariant(variantId);
        addCarRequest.setUser(usedId);
        mPresenter.addUserCar(addCarRequest);
    }

    public void onItemSuccess() {
        Gson gson = new Gson();
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        String carItems = preferenceManager.getStringPreference(getContext(), Constant.SP_CAR_ITEMS);
        carItemsResponse = gson.fromJson(carItems, CarItemsResponse.class);

        /*if(carItemsResponse!=null) {
            AddCarSpinnerAdapter adapterTransmission = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text_black, carItemsResponse.getGetCarItems().getTransmissions(), spinnerSelectText[3]);
            mTransmission.setAdapter(adapterTransmission);
        }*/
    }

    public void onAddCarSuccess(AddCarResponse addCarResponse) {

        Utility.hideSoftKeyboard(getActivity());

        if (addCarResponse != null) {

            CarStockRealm carRealm = new CarStockRealm();

            carRealm.setFuelType(addCarResponse.getGetCarResponse().getItem().getFuel_type());
            carRealm.setId(addCarResponse.getGetCarResponse().getItem().getId());
            //carRealm.setKm(addCarResponse.getGetCarResponse().getItem().getDriven());
            // carRealm.setPrice(addCarResponse.getGetCarResponse().getItem().getPrice());
            carRealm.setAccidential(addCarResponse.getGetCarResponse().getItem().getAccidental());
            carRealm.setColor(addCarResponse.getGetCarResponse().getItem().getVehicle_color());
            carRealm.setDescription(addCarResponse.getGetCarResponse().getItem().getDescription());
            carRealm.setInsurance(addCarResponse.getGetCarResponse().getItem().getInsurance());
            carRealm.setMileage(addCarResponse.getGetCarResponse().getItem().getMileage());
            carRealm.setOwnership(addCarResponse.getGetCarResponse().getItem().getOwner());
            carRealm.setRegistrationNo(addCarResponse.getGetCarResponse().getItem().getRegistration_no());
            carRealm.setServiceRecord(addCarResponse.getGetCarResponse().getItem().getService_history());
            carRealm.setTransmission(addCarResponse.getGetCarResponse().getItem().getTransmission());
            carRealm.setMaker(addCarResponse.getGetCarResponse().getItem().getMaker());
            carRealm.setModel(addCarResponse.getGetCarResponse().getItem().getModel());
            carRealm.setVariant(addCarResponse.getGetCarResponse().getItem().getVariant());
            carRealm.setStatus(addCarResponse.getGetCarResponse().getItem().getVehicle_status());
            carRealm.setLatitude(addCarResponse.getGetCarResponse().getItem().getGeometry().get(0));
            carRealm.setLongitude(addCarResponse.getGetCarResponse().getItem().getGeometry().get(1));
            carRealm.setBodyStyle(addCarResponse.getGetCarResponse().getItem().getBody_style());
            carRealm.setCreatedDate(addCarResponse.getGetCarResponse().getItem().getCreated_at());
            carRealm.setPublish(addCarResponse.getGetCarResponse().getItem().getPublish());
            carRealm.setPublisherAddress(addCarResponse.getGetCarResponse().getItem().getUser().getAddress().getLocation());
            carRealm.setPublisherName(addCarResponse.getGetCarResponse().getItem().getUser().getName());
            carRealm.setTitle(addCarResponse.getGetCarResponse().getItem().getTitle());
            carRealm.setYear(addCarResponse.getGetCarResponse().getItem().getManufacture_year());
            carRealm.setCarId(addCarResponse.getGetCarResponse().getItem().getCarId());
            carRealm.setModelName(addCarResponse.getGetCarResponse().getItem().getModelName());
            carRealm.setInternalType(Constant.STOCK_TYPE_GARAGE);

       /* if (addCarResponse.getGetCarResponse().getItem().getThumbnails().size() > 0) {
            RealmList<MediaRealm> realmListMedia = new RealmList<>();
            for (int j = 0; j < addCarResponse.getGetCarResponse().getItem().getThumbnails().size(); j++) {

                MediaRealm mediaRealm = new MediaRealm();
                mediaRealm.setId(addCarResponse.getGetCarResponse().getItem().getThumbnails().get(j).getId());
                mediaRealm.setFile_address(addCarResponse.getGetCarResponse().getItem().getThumbnails().get(j).getFile_address());
                realmListMedia.add(mediaRealm);

            }
            carRealm.setMedia(realmListMedia);
        }*/


            realm.beginTransaction();
            realm.copyToRealm(carRealm);
            realm.commitTransaction();

            Bundle params = new Bundle();
            params.putString("model_name", addCarResponse.getGetCarResponse().getItem().getModelName());
            mFirebaseAnalytics.logEvent("add_car", params);

            if (carType.equals(Constant.CAR_TYPES[1])) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_CAR_ID, addCarResponse.getGetCarResponse().getItem().getId());
                bundle.putString(Constant.KEY_MODEL_NAME, addCarResponse.getGetCarResponse().getItem().getModelName());
                bundle.putBoolean(Constant.KEY_TYPE, true);
                bundle.putDouble(Constant.Key_lat, 0.0);
                bundle.putDouble(Constant.Key_lng, 0.0);
                ((HomeScreen) getActivity()).clearStackLocal();
                //((HomeScreen) getActivity()).addFragment(new BookingCategoryFragment(), "BookingCategoryFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
            } else {
               /* Intent intent = new Intent(getActivity(), AddMultipleImagesActivity.class);
                intent.putExtra(Constant.KEY_ID, addCarResponse.getGetCarResponse().getItem().getId());
                intent.putExtra(Constant.KEY_TYPE, Constant.VALUE_CAR);
                intent.putExtra(Constant.KEY_CAR_TYPE, carType);
                startActivity(intent);*/
                getActivity().onBackPressed();
            }
        }

    }

    public void onAddUserCarSuccess(AddCarResponse addCarResponse) {

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_CAR_ID,addCarResponse.getGetCarResponse().getItem().getId());
        ((HomeScreen) getActivity()).makeDrawerVisible();
        ((HomeScreen) getActivity()).addFragment(new LeadInsuranceCarDetailsFragment(), "LeadInsuranceCarDetailsFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }
}