package autroid.business.view.fragment.booking;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.booking.BookingSlotAdapter;
import autroid.business.adapter.booking.OrderConvenienceAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingSlotCallback;
import autroid.business.model.bean.OrderConvenienceBE;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.GetTimeSlotRequest;
import autroid.business.model.response.AddBookingResponse;
import autroid.business.model.response.BookingConvenienceResponse;
import autroid.business.model.response.BookingSlotResponse;
import autroid.business.presenter.AddBookingPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.carsales.BookingCheckoutFragment;
import io.realm.Realm;

public class BookingScheduleFragment extends Fragment implements View.OnClickListener,DatePickerDialog.OnDateSetListener, BookingSlotCallback {

    AddBookingRequest objAddBookingRequest;
    Button btnDone;
    AddBookingPresenter mPresenter;
    RelativeLayout mMainLayout;
    LinearLayout mAddressLayout;

    String strDate="",strTime="",strConvenience="Self Drop",strRequirements="";
    View view;

    CalendarView calendarView;
    RecyclerView mList,mListConvenience;


   /* @BindView(R.id.address)
    EditText mAddress;
    @BindView(R.id.tv_locality)
    TextView mLocality;*/

    @BindView(R.id.convenience_pickup)
    TextView mPickup;
    @BindView(R.id.convenience_self_drop)
    TextView mSelfDrop;
    @BindView(R.id.convenience_doorstep)
    TextView mDoorstep;
    @BindView(R.id.charges_text)
    TextView mChangesText;
    @BindView(R.id.ll_convenience)
    LinearLayout mConvenience;

    @BindView(R.id.requirements)
    EditText mRequirents;

    private FirebaseAnalytics mFirebaseAnalytics;
    private String strLocation;
    private BookingSlotResponse bookingSlotResponse;

    private Realm realm;
    RealmController realmController;
    ArrayList<OrderConvenienceBE> convenienceBES=new ArrayList<>();
    private Boolean isAddressRequired,isCategory;
    private String strCharges,bookingId,userId,strBusinessId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        GlobalBus.getBus().register(this);
        return inflater.inflate(R.layout.fragment_date_time_slot, container, false);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_BOOKING_ADDRESS) {
            objAddBookingRequest.setAddress(intent.getStringExtra(Constant.KEY_ID));

            if(!isCategory)
                mPresenter.addBooking(objAddBookingRequest,false);
            else
                mPresenter.addCarBooking(objAddBookingRequest,true);
        }
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view=view;

        ButterKnife.bind(this,view);
        objAddBookingRequest= (AddBookingRequest) getArguments().getSerializable("AddBookingRequest");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Booking",null);

        isCategory=getArguments().getBoolean(Constant.Is_Category);
        if(isCategory)
        strBusinessId=getArguments().getString(Constant.KEY_VENDOR_ID);

        calendarView = view.findViewById(R.id.calendarView);
        long now = System.currentTimeMillis() - 1000;
        calendarView.setMinDate(now);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        btnDone= (Button) view.findViewById(R.id.booking_done);
        btnDone.setOnClickListener(this);
        mPickup.setOnClickListener(this);
        mDoorstep.setOnClickListener(this);
        mSelfDrop.setOnClickListener(this);

        mAddressLayout=view.findViewById(R.id.ll_address);
        mList=view.findViewById(R.id.booking_timesslot);

        LinearLayoutManager llPending,llConfirmed,llCompleted;
        llPending = new LinearLayoutManager(getActivity());
        llPending.setOrientation(LinearLayoutManager.HORIZONTAL);
        mList.setLayoutManager(llPending);

        mListConvenience=view.findViewById(R.id.booking_convenience);

        llConfirmed = new LinearLayoutManager(getActivity());
        llConfirmed.setOrientation(LinearLayoutManager.HORIZONTAL);
        mListConvenience.setLayoutManager(llConfirmed);

        mMainLayout=  view.findViewById(R.id.main_layout);
        mPresenter=new AddBookingPresenter(this,mMainLayout);

        TextView tvTitle = (TextView) view.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.select_details));
        btnDone.setEnabled(false);

        bookingId=getArguments().getString(Constant.KEY_ID);
        userId=getArguments().getString(Constant.USER_ID);

        String day=dayStringFormat(Calendar.getInstance().getTimeInMillis()+2*(1000 * 60 * 60 * 24));
        if(!day.equalsIgnoreCase("Monday")) {
            calendarView.setDate(Calendar.getInstance().getTimeInMillis() + 2 * (1000 * 60 * 60 * 24), false, true);
            String targetdatevalue=getDate(Calendar.getInstance().getTimeInMillis() + 2 * (1000 * 60 * 60 * 24),"yyyy-MM-dd");
            strDate=targetdatevalue;

            GetTimeSlotRequest getTimeSlotRequest=new GetTimeSlotRequest();
            getTimeSlotRequest.setDate(targetdatevalue);
            getTimeSlotRequest.setServices(objAddBookingRequest.getServices());
            //getTimeSlotRequest.setLabel(objAddBookingRequest.getLabel());
            if(!isCategory) {
                getTimeSlotRequest.setBusiness(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS));

                mPresenter.getSlots(bookingId, targetdatevalue);
            }
            else {
                getTimeSlotRequest.setBusiness(strBusinessId);

                mPresenter.getMySlots(getTimeSlotRequest);
            }
        } else {
            calendarView.setDate(Calendar.getInstance().getTimeInMillis() + 3 * (1000 * 60 * 60 * 24), false, true);
            String targetdatevalue=getDate(Calendar.getInstance().getTimeInMillis() + 3 * (1000 * 60 * 60 * 24),"yyyy-MM-dd");
            strDate=targetdatevalue;

            GetTimeSlotRequest getTimeSlotRequest=new GetTimeSlotRequest();
            getTimeSlotRequest.setDate(targetdatevalue);
            getTimeSlotRequest.setBusiness(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS));
            getTimeSlotRequest.setServices(objAddBookingRequest.getServices());
            //getTimeSlotRequest.setLabel(objAddBookingRequest.getLabel());

            if(!isCategory) {
                getTimeSlotRequest.setBusiness(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS));


                mPresenter.getSlots(bookingId, targetdatevalue);
            }
            else {
                getTimeSlotRequest.setBusiness(strBusinessId);
                mPresenter.getMySlots(getTimeSlotRequest);
            }
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                try {
                    String input_date =zeroPrefix(i2) + "-" + zeroPrefix(i1 + 1) + "-" + zeroPrefix(i);
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    Date dt1 = format1.parse(input_date);
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", dt1);
                    Log.d("Day",dayOfTheWeek);


                        strDate=zeroPrefix(i2) + "-" + zeroPrefix(i1 + 1) + "-" + zeroPrefix(i);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date sourceDate = null;
                        try {
                            sourceDate = dateFormat.parse(strDate);
                            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String targetdatevalue= targetFormat.format(sourceDate);
                            strDate=targetdatevalue;
                            GetTimeSlotRequest getTimeSlotRequest=new GetTimeSlotRequest();
                            getTimeSlotRequest.setDate(targetdatevalue);
                            getTimeSlotRequest.setBusiness(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS));
                            getTimeSlotRequest.setServices(objAddBookingRequest.getServices());
                            getTimeSlotRequest.setLabel(objAddBookingRequest.getLabel());

                            if(!isCategory) {
                                getTimeSlotRequest.setBusiness(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS));

                                mPresenter.getSlots(bookingId, targetdatevalue);
                            }
                            else {
                                getTimeSlotRequest.setBusiness(strBusinessId);


                                mPresenter.getMySlots(getTimeSlotRequest);
                            }

                            strTime="";
                            //objAddBookingRequest.setDate(targetdatevalue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }




                }catch (Exception e){

                }

            }
        });


        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController = new RealmController(appCtx);

        if(!isCategory)
            mPresenter.getConvenience(bookingId,"");
        else
            mPresenter.getBookingConvenience(objAddBookingRequest.getBusiness());

        // ServiceConvenienceBE serviceConvenienceBE=new ServiceConvenienceBE();



       /* mConvenience.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                View radioButton = mConvenience.findViewById(checkedId);
                int index = mConvenience.indexOfChild(radioButton);

                // Add logic here

                switch (index) {
                    case 1: // first button
                        mAddressLayout.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "Selected button number " + index, Toast.LENGTH_SHORT).show();
                        break;
                    case 0: // secondbutton
                        mAddressLayout.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Selected button number " + index,  Toast.LENGTH_SHORT).show();
                        break;
                    case 2: // secondbutton
                        mAddressLayout.setVisibility(View.VISIBLE);
                        //Toast.makeText(getApplicationContext(), "Selected button number " + index,  Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });*/
    }

    private void showConvenience(Boolean isDoorstep) {

        convenienceBES.add(new OrderConvenienceBE("Self Drop",0,"",false,true));
        convenienceBES.add(new OrderConvenienceBE("Pickup",299,"Free for invoices above ₹5000",true,true));
        convenienceBES.add(new OrderConvenienceBE("Doorstep",299,"Free for invoices above ₹5000",true,isDoorstep));
        convenienceBES.add(new OrderConvenienceBE("Towing",1999,"Free in case of an Insurance Claim",true,true));
        convenienceBES.add(new OrderConvenienceBE("Flatbed Towing",2999,"Free in case of an Insurance Claim",true,true));



    }


/*    public void setDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(BookingScheduleFragment.this,this, year, month, day);

        final Calendar later = Calendar.getInstance();
        later.add(Calendar.MONTH, 1);
        datePickerDialog.setTitle("Select Date");
        //datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "",datePickerDialog);  // hide cancel button
        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

              //  datePickerDialog.dismiss();
                objAddBookingRequest.setDate(year+"-"+monthOfYear+"-"+dayOfMonth);

            }});


        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        setTime();
    }

    private void setTime(){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(BookingScheduleFragment.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                objAddBookingRequest.setTime_slot(selectedHour+"-"+selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "",mTimePicker);  // hide cancel button
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.booking_done:

                ((HomeScreen)getActivity()).disableButton(btnDone);

                if(validate()){
                    objAddBookingRequest.setPayment_mode("Online");
                    objAddBookingRequest.setPayment_status("Pending");
                    objAddBookingRequest.setStatus("Pending");
                    objAddBookingRequest.setConvenience(strConvenience);
                    objAddBookingRequest.setDate(strDate);
                    objAddBookingRequest.setTime_slot(strTime);
                    objAddBookingRequest.setTransaction_id("");
                    objAddBookingRequest.setCharges(strCharges);
                    objAddBookingRequest.setBooking(bookingId);
                    objAddBookingRequest.setRequirements(strRequirements);

                    if (!strConvenience.equalsIgnoreCase("Self Drop")) {
                            BookingAddressFragment bookingAddressFragment = new BookingAddressFragment();
                            Bundle bundle=new Bundle();
                            bundle.putString(Constant.KEY_ID,bookingId);
                            bundle.putString(Constant.USER_ID,userId);
                            bundle.putBoolean(Constant.Is_Category,isCategory);
                            bookingAddressFragment.setArguments(bundle);
                            bookingAddressFragment.show(getChildFragmentManager(), "BookingAddressFragment");
                    } else {

                        if(!isCategory)
                            mPresenter.addBooking(objAddBookingRequest,false);
                        else
                            mPresenter.addCarBooking(objAddBookingRequest,true);

                    }
                }

                /* int selectedId=mConvenience.getCheckedRadioButtonId();
                   mConvenienceButton=(RadioButton)findViewById(selectedId);
                objAddBookingRequest.setConvenience(mConvenienceButton.getText().toString());*/
               //
                break;

            case R.id.convenience_doorstep:
                mChangesText.setVisibility(View.VISIBLE);
                strConvenience=mDoorstep.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_red_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);
                mDoorstep.setTextColor(getResources().getColor(R.color.white));
                mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                break;

            case R.id.convenience_pickup:
                mChangesText.setVisibility(View.VISIBLE);
                strConvenience=mPickup.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_red_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_white_color);

                if(mDoorstep.isEnabled())
                     mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mPickup.setTextColor(getResources().getColor(R.color.white));
                mSelfDrop.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                break;

            case R.id.convenience_self_drop:
                mChangesText.setVisibility(View.GONE);
                strConvenience=mSelfDrop.getText().toString();
                mDoorstep.setBackgroundResource(R.drawable.rectangle_white_color);
                mPickup.setBackgroundResource(R.drawable.rectangle_white_color);
                mSelfDrop.setBackgroundResource(R.drawable.rectangle_red_color);

                if(mDoorstep.isEnabled())
                     mDoorstep.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mPickup.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
                mSelfDrop.setTextColor(getResources().getColor(R.color.white));

                break;

        }
    }



 /*   public void setDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year, month, day);
        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker()
                .setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.setTitle("Select Date");
        //datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "",datePickerDialog);  // hide cancel button
        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //  datePickerDialog.dismiss();
                try {
                    String input_date =zeroPrefix(dayOfMonth) + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(year);
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    Date dt1 = format1.parse(input_date);
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", dt1);
                    Log.d("Day",dayOfTheWeek);
                    
                    if(dayOfTheWeek.equalsIgnoreCase("Monday")){
                        Toast.makeText(getActivity(), "CarEager Xpress remains closed on Mondays", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mDate.setText(zeroPrefix(dayOfMonth) + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(year));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date sourceDate = null;
                        try {
                            strDate=mDate.getText().toString();
                            sourceDate = dateFormat.parse(strDate);
                            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String targetdatevalue= targetFormat.format(sourceDate);

                            mPresenter.getSlots(targetdatevalue,objAddBookingRequest.getBusiness());

                            //objAddBookingRequest.setDate(targetdatevalue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        datePickerDialog.dismiss();
                    }
                }catch (Exception e){

                }

            }});
        datePickerDialog.show();
    }*/

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    private boolean validate(){
        boolean flag=true;


        strRequirements=mRequirents.getText().toString();

        if(strDate.length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select date");
        }
        else if(strTime.length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select time slot");
        }

        else if(objAddBookingRequest.getFromCategory()) {
            if(strConvenience.length()==0){
                flag=false;
                Utility.showResponseMessage(mMainLayout,"Please select convenience");
            }
        }




        return flag;
    }

    public void onBookingSuccess(AddBookingResponse addBookingResponse,Boolean isCheckout) {
       // Utility.showResponseMessage(mMainLayout,addBookingResponse.getResponseMessage());
        Bundle params = new Bundle();
        params.putString("booking_number", addBookingResponse.getmGetBookingData().getBooking_no());
        mFirebaseAnalytics.logEvent("add_booking", params);
        if(isCheckout) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID, addBookingResponse.getmGetBookingData().getId());
            ((HomeScreen) getActivity()).addFragment(new BookingCheckoutFragment(), "BookingCheckoutFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else {
            Toast.makeText(getActivity(), addBookingResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
            ((HomeScreen) getActivity()).clearStackLocal();


        }

        /*Intent bundle=new Intent(getActivity(),PaymentFragment.class);
        bundle.putExtra(Constant.KEY_ID,addBookingResponse.getmGetBookingData().getUser());
        bundle.putExtra(Constant.Key_Name,addBookingResponse.getmGetBookingData().getFirstname());
        bundle.putExtra(Constant.Key_Email,addBookingResponse.getmGetBookingData().getEmail());
        bundle.putExtra(Constant.Key_Mobile,addBookingResponse.getmGetBookingData().getPhone());
        bundle.putExtra(Constant.KEY_TYPE,addBookingResponse.getmGetBookingData().getProductinfo());
        bundle.putExtra(Constant.KEY_TXN_ID,addBookingResponse.getmGetBookingData().getTxnid());
        bundle.putExtra(Constant.KEY_AMOUNT,addBookingResponse.getmGetBookingData().getAmount());
        startActivity(bundle);*/

//

//        getActivity().onBackPressed();

       /*  String tags[]={"BookingCarFragment","EditCarFragment","ServiceProviderCarEagerFragment","BookingBusinessFragment","BookingServicePackagesFragment"};

       */
    }

    public void dismissDialog(String tag){

        getFragmentManager().popBackStack();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
         ft.addToBackStack(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //Log.i(tag, "keyCode: " + keyCode);
                    if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                       getActivity().onBackPressed();
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e){

        }
    }


    public void onSlotsSuccess(BookingSlotResponse bookingSlotResponse) {
        this.bookingSlotResponse=bookingSlotResponse;
        btnDone.setEnabled(true);
        BookingSlotAdapter adapterCategory = new BookingSlotAdapter(getActivity(),bookingSlotResponse.getGetSlots(),this);
        mList.setAdapter(adapterCategory);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String dayStringFormat(long msecs) {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime(new Date(msecs));

        int dow = cal.get(Calendar.DAY_OF_WEEK);

        switch (dow) {
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            case Calendar.SUNDAY:
                return "Sunday";
        }

        return "Unknown";
    }

    @Override
    public void onSlotClick(Boolean status, String slot) {
        if(!status){
            Utility.showResponseMessage(mMainLayout,"This slot is full for selected date");
        }
        else {
            strTime=slot;
        }
    }

    @Override
    public void onConvenienceClick(String slot, int charges) {
        strConvenience=slot;
      //  this.isAddressRequired=status;
        this.strCharges = charges + "";
       /* if(status){

               // mChangesText.setText("Charges: ₹" + charges +" ("+message+")");
                mChangesText.setVisibility(View.VISIBLE);
        }
        else {
            mChangesText.setVisibility(View.GONE);

        }*/
    }

    public void onConvenienceSuccess(BookingConvenienceResponse bookingConvenienceResponse) {
        OrderConvenienceAdapter orderConvenienceAdapter = new OrderConvenienceAdapter(getActivity(),bookingConvenienceResponse.getGetData().getConvenience(), this);
        mListConvenience.setAdapter(orderConvenienceAdapter);
    }
}
