package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.greenrobot.eventbus.Subscribe;
import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.request.UpdateCarDetailsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobCardCarUpdatePresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.search.SearchCarFragment;

public class JobCarUpdateFragment extends DialogFragment {

    private ConstraintLayout mainLayout;

    private JobCardCarUpdatePresenter presenter;

    private CarDetailBE carDetailBE;

    private EditText registrationNumber, vinNumber, engNumber,km,fuel,purchaseYear,manufacturingYear;

    private TextView name;

    String carId="",variantId="",userId="",bookingId="";

    private Button update;

    public JobCarUpdateFragment() {
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
        return inflater.inflate(R.layout.fragment_jobs_car_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);

        carDetailBE= (CarDetailBE) getArguments().getSerializable(Constant.CAR_DETAILS);

        variantId=getArguments().getString(Constant.KEY_VARIANT_ID);
        carId=getArguments().getString(Constant.KEY_CAR_ID);;
        bookingId=getArguments().getString(Constant.BOOKING_ID);;
        userId=getArguments().getString(Constant.USER_ID);;

        mainLayout = view.findViewById(R.id.main_layout);

        presenter = new JobCardCarUpdatePresenter(this, mainLayout);

        name = view.findViewById(R.id.name);
        registrationNumber = view.findViewById(R.id.car_registration_no);
        vinNumber = view.findViewById(R.id.vin_no);
        engNumber = view.findViewById(R.id.eng_no);
        update = view.findViewById(R.id.update);

        km = view.findViewById(R.id.car_km);
        fuel = view.findViewById(R.id.car_fuel);
        purchaseYear = view.findViewById(R.id.purchase_year);
        manufacturingYear = view.findViewById(R.id.manufacturibg_year);

        name.setText(carDetailBE.getTitle());
        registrationNumber.setText(carDetailBE.getRegistration_no());
        vinNumber.setText(carDetailBE.getVin());
        engNumber.setText(carDetailBE.getEngine_no());
        km.setText(carDetailBE.getOdometer());
        fuel.setText(carDetailBE.getFuel_type());
        purchaseYear.setText(carDetailBE.getPurchased_year());
        manufacturingYear.setText(carDetailBE.getManufacture_year());

        name.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IS_CAR_UPDATE, true);
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new SearchCarFragment(), "SearchCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        });


        update.setOnClickListener(v -> {

            if (validate()) {
                UpdateCarDetailsRequest request = new UpdateCarDetailsRequest();
                request.setCar(carId);
                request.setBooking(bookingId);
                request.setUser(userId);
                request.setVariant(variantId);
                request.setTitle(name.getText().toString().trim());
                request.setRegistration_no(registrationNumber.getText().toString().trim());
                request.setVin(vinNumber.getText().toString().trim());
                request.setEngine_no(engNumber.getText().toString().trim());

                request.setOdometer(km.getText().toString().trim());
                request.setPurchased_year(purchaseYear.getText().toString().trim());
                request.setManufacture_year(manufacturingYear.getText().toString().trim());
                request.setFuel_level(fuel.getText().toString().trim());

                presenter.updateCarDetails(request);
            }
        });
    }

    private boolean validate() {

        if (name.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid Name");
            return false;
        } else if (registrationNumber.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid Registration Number");
            return false;
        }

        return true;
    }

    public void onSuccess(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SEND_CAR_UPDATE) {
           name.setText(intent.getStringExtra("variant_name"));
           variantId=intent.getStringExtra("variant_id");
        }
    }

}
