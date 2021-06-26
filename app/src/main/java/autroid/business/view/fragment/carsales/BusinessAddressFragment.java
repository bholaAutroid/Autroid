package autroid.business.view.fragment.carsales;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.response.BusinessPinResponse;
import autroid.business.presenter.carsales.BusinessAddressPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class BusinessAddressFragment extends DialogFragment {

    private Button addAddress;

    private EditText address, landmark, zipCode, state, town;

    private String latitude, longitude;

    private LinearLayout mainLayout;

    private BusinessAddressPresenter businessAddressPresenter;

    public BusinessAddressFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_opacity80)));
        return inflater.inflate(R.layout.fragment_business_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);

        businessAddressPresenter = new BusinessAddressPresenter(this, mainLayout);
    }

    private void findViews(View view) {
        address = view.findViewById(R.id.address);
        landmark = view.findViewById(R.id.landmark);
        zipCode = view.findViewById(R.id.zipcode);
        town = view.findViewById(R.id.town);
        state = view.findViewById(R.id.state);
        mainLayout = view.findViewById(R.id.main_layout);
        addAddress = view.findViewById(R.id.add_address);

        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 6)
                    businessAddressPresenter.getPinData(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        addAddress.setOnClickListener(v -> {
            if (validateAddress()) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_ADD_ADDRESS);
                broadcastIntent.putExtra(Constant.ADDRESS, address.getText().toString().trim());
                broadcastIntent.putExtra(Constant.LANDMARK, landmark.getText().toString().trim());
                broadcastIntent.putExtra(Constant.ZIP, zipCode.getText().toString().trim());
                broadcastIntent.putExtra(Constant.TOWN, town.getText().toString().trim());
                broadcastIntent.putExtra(Constant.STATE, state.getText().toString().trim());
                broadcastIntent.putExtra(Constant.LATITUDE, latitude);
                broadcastIntent.putExtra(Constant.LONGITUDE, longitude);
                Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                GlobalBus.getBus().post(sendEvent);
                Utility.hideSoftKeyboard(getActivity());
                getDialog().dismiss();
            }
        });

    }


    public void onSuccessPinResponse(BusinessPinResponse response) {

        if(response.getPinData().size()>0){
            latitude=response.getPinData().get(0).getLatitude();
            longitude=response.getPinData().get(0).getLongitude();
            town.setText(response.getPinData().get(0).getRegion());
            state.setText(response.getPinData().get(0).getState());
        }else{
            town.setText("");
            state.setText("");
        }
    }

    private boolean validateAddress() {

        if (address.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid Address");
            return false;
        } else if (zipCode.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid ZipCode");
            return false;
        } else if (town.getText().toString().trim().length()==0) {
            Utility.showResponseMessage(mainLayout, "Please enter town/city");
            return false;
        } else if (state.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid State");
            return false;
        }
        return true;
    }

}
