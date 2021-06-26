package autroid.business.view.fragment.booking;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
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

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.booking.BookingAddressAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingAddressCallback;
import autroid.business.model.request.BookingAddressRequest;
import autroid.business.model.response.AddBookingAddressResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingConvenienceResponse;
import autroid.business.presenter.BookingAddressPresenter;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingAddressFragment extends DialogFragment implements View.OnClickListener, BookingAddressCallback {

    @BindView(R.id.address1)
    EditText mAddress1;
    @BindView(R.id.address2)
    EditText mAddress2;
    @BindView(R.id.address3)
    EditText mAddress3;
    @BindView(R.id.address4)
    EditText mAddress4;
    @BindView(R.id.address5)
    EditText mAddress5;
    @BindView(R.id.zipcode)
    EditText mZipcode;
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.booking_done)
    Button mDone;
    @BindView(R.id.select_address)
    Button mSelectAddress;
    @BindView(R.id.address_list)
    RecyclerView mList;
    @BindView(R.id.ll_cross)
    LinearLayout mLLCross;
    @BindView(R.id.ll_selectaddress)
    LinearLayout mllSelectAddress;


    String strAddress1="",strAddress2="",strAddress3="",strAddress4="",strAddress5="",strZipcode="",strUser;

    BookingAddressPresenter mPresenter;

    BookingAddressAdapter bookingAddressAdapter;

    public BookingAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity80)));
        return inflater.inflate(R.layout.fragment_booking_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        GlobalBus.getBus().register(this);

        mPresenter=new BookingAddressPresenter(this,mMainLayout);

        if(getArguments().getBoolean(Constant.Is_Category)){
            strUser=PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS);

            mPresenter.getBookingConvenience(strUser);

        }else {
            mPresenter.getBookingAddress(getArguments().getString(Constant.KEY_ID));
            strUser=getArguments().getString(Constant.USER_ID);


        }


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);
        mList.setNestedScrollingEnabled(false);

        mDone.setOnClickListener(this);
        mSelectAddress.setOnClickListener(this);
        mLLCross.setOnClickListener(this);
        mMainLayout.setOnClickListener(this);

        mZipcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==6){
                    mPresenter.getBookingCity(s.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.booking_done:
                if(validate()){
                    BookingAddressRequest bookingAddressRequest=new BookingAddressRequest();
                    bookingAddressRequest.setAddress(strAddress1);
                    bookingAddressRequest.setArea(strAddress2);
                    bookingAddressRequest.setLandmark(strAddress3);
                    bookingAddressRequest.setCity(strAddress4);
                    bookingAddressRequest.setState(strAddress5);
                    bookingAddressRequest.setZip(strZipcode);

                    bookingAddressRequest.setUser(strUser);
                    mPresenter.addBookingAddress(bookingAddressRequest);
                }
                break;
            case R.id.select_address:
                if(bookingAddressAdapter.addressId!=null){
                    Intent data = new Intent();
                    data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_BOOKING_ADDRESS);
                    data.putExtra(Constant.KEY_ID,bookingAddressAdapter.addressId);

                    Events.SendEvent sendEvent =
                            new Events.SendEvent(data);
                    GlobalBus.getBus().post(sendEvent);
                    getDialog().dismiss();
                }
                break;
            case R.id.ll_cross:
                getDialog().dismiss();
                break;
            case R.id.main_layout:
                Utility.hideSoftKeyboard(getActivity());
                break;
        }
    }

    private boolean validate() {
        boolean flag=true;
        strAddress1=mAddress1.getText().toString();
        strAddress2=mAddress2.getText().toString();
        strAddress3=mAddress3.getText().toString();
        strAddress4=mAddress4.getText().toString();
        strAddress5=mAddress5.getText().toString();
        strZipcode=mZipcode.getText().toString();

        if(strAddress1.length()==0){
            flag=false;
            mAddress1.setError("Required");
        }
        else if(strZipcode.length()==0){
            flag=false;
            mZipcode.setError("Required");
        }

        return flag;
    }

    public void onAddressSuccess(BookingConvenienceResponse bookingAddressResponse) {
        if(bookingAddressResponse.getGetData().getAddress().size()>0){
            mllSelectAddress.setVisibility(View.VISIBLE);
        }
        else {
            mllSelectAddress.setVisibility(View.GONE);
        }
        bookingAddressAdapter=new BookingAddressAdapter(getActivity(),bookingAddressResponse.getGetData().getAddress(),this);
        mList.setAdapter(bookingAddressAdapter);
    }

    public void onAddAddressSuccess(AddBookingAddressResponse addBookingAddressResponse) {

        Intent data = new Intent();
        data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_BOOKING_ADDRESS);
        data.putExtra(Constant.KEY_ID,addBookingAddressResponse.getBookingAddressBE().getId());

        Events.SendEvent sendEvent =
                new Events.SendEvent(data);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }

    public void onCitySuccess(AddBookingAddressResponse addBookingAddressResponse) {
        mAddress4.setText(addBookingAddressResponse.getBookingAddressBE().getCity());
        mAddress5.setText(addBookingAddressResponse.getBookingAddressBE().getState());
    }

    @Override
    public void onRemoveAddress(int position, String id) {
        BookingAddressRequest bookingAddressRequest=new BookingAddressRequest();
        bookingAddressRequest.setId(id);
       // mPresenter.deleteAddress(bookingAddressRequest,position);
    }

    public void onRemoveSuccess(BaseResponse baseResponse, int pos) {
     //   mPresenter.getBookingAddress();
    }
}
