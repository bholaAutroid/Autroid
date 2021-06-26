package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.booking.OrderConvenienceAdapter;
import autroid.business.adapter.jobcard.AddressAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingSlotCallback;
import autroid.business.model.bean.BookingAddressBE;
import autroid.business.model.request.AddressRequest;
import autroid.business.model.request.UpdateMemberRequest;
import autroid.business.model.response.AddressResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingAddressResponse;
import autroid.business.model.response.PinResponse;
import autroid.business.presenter.jobcard.JobCardAddressPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;

public class JobCardAddressFragment extends DialogFragment implements View.OnClickListener, BookingSlotCallback {

    LinearLayout mainLayout, linearSelectAddress, linearNewAddress, convenienceLayout;

    JobCardAddressPresenter addressPresenter;

    AddressAdapter addressAdapter;

    ArrayList<BookingAddressBE> arrayList;

    RecyclerView recyclerView,mListConvenience;

    Button selectAddress, addAddress;

    String bookingId, userId, strConvenience = "Self Drop";

    EditText address;

    EditText zipCode, town, state, landMark;

    boolean isAddressUpdate;

    @BindView(R.id.convenience_pickup)
    TextView mPickup;

    @BindView(R.id.convenience_self_drop)
    TextView mSelfDrop;

    @BindView(R.id.convenience_doorstep)
    TextView mDoorstep;
    private String strCharges="0";

    public JobCardAddressFragment() {
    }


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
        return inflater.inflate(R.layout.job_card_address_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this, view);

        mainLayout = view.findViewById(R.id.main_layout);
        convenienceLayout = view.findViewById(R.id.convenience_layout);

        bookingId = getArguments().getString(Constant.BOOKING_ID);
        userId = getArguments().getString(Constant.USER_ID);

        if (getArguments() != null && getArguments().getBoolean(Constant.KEY_TYPE)) {
            isAddressUpdate = true;
            convenienceLayout.setVisibility(View.GONE);
        }

        linearSelectAddress = view.findViewById(R.id.linear_select_address);
        linearNewAddress = view.findViewById(R.id.ll_address);
        recyclerView = view.findViewById(R.id.address_list);
        selectAddress = view.findViewById(R.id.select_address);
        zipCode = view.findViewById(R.id.zipcode);
        town = view.findViewById(R.id.town);
        state = view.findViewById(R.id.state);
        addAddress = view.findViewById(R.id.add_address);
        address = view.findViewById(R.id.address);
        landMark = view.findViewById(R.id.landmark);

        mListConvenience=view.findViewById(R.id.booking_convenience);

        LinearLayoutManager llConfirmed = new LinearLayoutManager(getActivity());
        llConfirmed.setOrientation(LinearLayoutManager.HORIZONTAL);
        mListConvenience.setLayoutManager(llConfirmed);

        mPickup.setOnClickListener(this);
        mDoorstep.setOnClickListener(this);
        mSelfDrop.setOnClickListener(this);

        arrayList = new ArrayList<>();
        addressAdapter = new AddressAdapter(arrayList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(addressAdapter);

        addressPresenter = new JobCardAddressPresenter(this, mainLayout);
        addressPresenter.getAddress(bookingId);

        zipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() == 6)
                    addressPresenter.getPinData(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        selectAddress.setOnClickListener(v -> {
            if (addressAdapter.addressId != null && !isAddressUpdate) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKING_ID, bookingId);
                bundle.putString(Constant.ADDRESS_ID, addressAdapter.addressId);
                bundle.putString(Constant.KEY_TYPE, strConvenience);
                bundle.putString(Constant.KEY_CHARGES, strCharges);
                ((HomeScreen) getActivity()).addFragment(new JobCardParticularsFragment(), "ParticularsFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                getDialog().dismiss();
            } else if (addressAdapter.addressId != null) {
                UpdateMemberRequest request = new UpdateMemberRequest();
                request.setAddress(addressAdapter.addressId);
                request.setBooking(bookingId);
                addressPresenter.setSelectedAddress(request);
            }
        });

        addAddress.setOnClickListener(v -> {
            if (validateAddress()) addressPresenter.setAddress(createAddressRequest());
        });

    }

    public void onSuccessAddressResponse(BookingAddressResponse addressResponse) {
        showAddresses(addressResponse);
        if (!isAddressUpdate) showConvenience(addressResponse);
    }

    public void onSuccessPinResponse(PinResponse response) {
        Utility.hideSoftKeyboard(getActivity());
        town.setText(response.getPinData().getCity());
        state.setText(response.getPinData().getState());
    }


    public void onSuccessAddAddress(AddressResponse addressResponse) {

        if (!isAddressUpdate) {
            String addressId = addressResponse.getAddress().getId();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.BOOKING_ID, bookingId);
            bundle.putString(Constant.ADDRESS_ID, addressId);
            bundle.putString(Constant.KEY_TYPE, strConvenience);
            bundle.putString(Constant.KEY_CHARGES, strCharges);
            ((HomeScreen) getActivity()).addFragment(new JobCardParticularsFragment(), "ParticularsFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            getDialog().dismiss();
        } else {
            UpdateMemberRequest request=new UpdateMemberRequest();
            request.setBooking(bookingId);
            request.setAddress(addressResponse.getAddress().getId());
            addressPresenter.setSelectedAddress(request);
        }
    }

    public void onSuccessAddressUpdate(BaseResponse response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }

    private boolean validateAddress() {

        if (address.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid Address");
            return false;
        } else if (zipCode.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid ZipCode");
            return false;
        } else if (town.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid City");
            return false;
        } else if (state.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid State");
            return false;
        }
        return true;
    }

    private AddressRequest createAddressRequest() {

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setAddress(address.getText().toString().trim());
        addressRequest.setArea("");
        addressRequest.setLandmark(landMark.getText().toString().trim());
        addressRequest.setUser(userId);
        addressRequest.setCity(town.getText().toString().trim());
        addressRequest.setState(state.getText().toString().trim());
        addressRequest.setZip(zipCode.getText().toString().trim());

        return addressRequest;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.convenience_doorstep:
                strConvenience = mDoorstep.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_red_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);
                mDoorstep.setTextColor(getResources().getColor(R.color.white));
                mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                break;

            case R.id.convenience_pickup:
                strConvenience = mPickup.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_red_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);
                mPickup.setTextColor(getResources().getColor(R.color.white));
                mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                break;

            case R.id.convenience_self_drop:
                strConvenience = mSelfDrop.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_red_color);
                mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mSelfDrop.setTextColor(getResources().getColor(R.color.white));
                break;

        }
    }

    private void showAddresses(BookingAddressResponse addressResponse) {
        if (addressResponse.getUserAddressResponse().getAddressList().size() > 0) {
            linearSelectAddress.setVisibility(View.VISIBLE);
            arrayList.addAll(getAddressesList(addressResponse.getUserAddressResponse().getAddressList()));
            addressAdapter.notifyDataSetChanged();
        }
    }

    private void showConvenience(BookingAddressResponse addressResponse) {
        if (addressResponse.getUserAddressResponse().getConvenienceList().size() > 0) {
            OrderConvenienceAdapter orderConvenienceAdapter = new OrderConvenienceAdapter(getActivity(),addressResponse.getUserAddressResponse().getConvenienceList(), this);
            mListConvenience.setAdapter(orderConvenienceAdapter);
            
          /*  for (ConvenienceBE data : addressResponse.getUserAddressResponse().getConvenienceList()) {
                if (data.isChecked() && data.getConvenience().equals("Self Drop")) {
                    strConvenience = data.getConvenience();
                    mDoorstep.setEnabled(false);
                    mPickup.setEnabled(false);
                    mSelfDrop.setEnabled(false);
                    mSelfDrop.setBackgroundResource(R.drawable.rectangle_red_color);
                    mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                    mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                    mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                    mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                    mSelfDrop.setTextColor(getResources().getColor(R.color.white));
                } else if (data.isChecked() && data.getConvenience().equals("Pickup")) {
                    strConvenience = data.getConvenience();
                    mDoorstep.setEnabled(false);
                    mSelfDrop.setEnabled(false);
                    mPickup.setEnabled(false);
                    mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);
                    mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                    mPickup.setBackgroundResource(R.drawable.rectangle_red_color);
                    mPickup.setTextColor(getResources().getColor(R.color.white));
                    mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                    mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                } else if (data.isChecked() && data.getConvenience().equals("Doorstep")) {
                    strConvenience = data.getConvenience();
                    mPickup.setEnabled(false);
                    mSelfDrop.setEnabled(false);
                    mDoorstep.setEnabled(false);
                    mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);
                    mDoorstep.setBackgroundResource(R.drawable.rectangle_red_color);
                    mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                    mDoorstep.setTextColor(getResources().getColor(R.color.white));
                    mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                    mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                }
            }*/
        }
    }


    private ArrayList<BookingAddressBE> getAddressesList(ArrayList<BookingAddressBE> bookingAddress) {

        ArrayList<BookingAddressBE> arrayList = new ArrayList<>();

        for (BookingAddressBE data : bookingAddress) {
            if (data.getChecked() && !isAddressUpdate) {
                arrayList.add(data);
              //  linearNewAddress.setVisibility(View.GONE);
                return arrayList;
            }
        }
        return bookingAddress;
    }

    @Override
    public void onSlotClick(Boolean status, String slot) {
        
    }

    @Override
    public void onConvenienceClick(String slot, int charges) {
        strConvenience=slot;
        //  this.isAddressRequired=status;
        this.strCharges = charges + "";
    }
}
