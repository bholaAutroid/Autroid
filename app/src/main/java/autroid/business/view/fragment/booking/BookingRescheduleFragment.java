package autroid.business.view.fragment.booking;


import android.app.Application;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.booking.BookingRescheduleSlotAdapter;
import autroid.business.model.request.BookingRescheduleRequest;
import autroid.business.model.request.GetTimeSlotRequest;
import autroid.business.model.response.BookingRescheduleResponse;
import autroid.business.model.response.BookingSlotResponse;
import autroid.business.presenter.BookingReschedulePresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */

public class BookingRescheduleFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    TextView mDate;
    AppCompatSpinner mTimeslot;
    Button btnDone;
    LinearLayout mMainLayout;
    String strDate,strTime;
    boolean isCre;
    private BookingReschedulePresenter mPresenter;
    private String strId,strBussId;

    private Realm realm;
    RealmController realmController;
    private BookingSlotResponse bookingSlotResponse;

    private Boolean isMyBooking;
    private GetTimeSlotRequest getTimeSlotRequestNew;


    public BookingRescheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_reschedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDone= (Button) view.findViewById(R.id.booking_done);
        btnDone.setOnClickListener(this);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        strId=getArguments().getString(Constant.KEY_ID);
        isCre=getArguments().getBoolean(Constant.IS_CRE);
        strBussId= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_BUSINESS);

        isMyBooking=getArguments().getBoolean(Constant.KEY_MY_BOOKING);
        mDate= (TextView) view.findViewById(R.id.date_text);
        mDate.setOnClickListener(this);
        mTimeslot= (AppCompatSpinner) view.findViewById(R.id.spn_timeslot);

        if(isMyBooking){
            getTimeSlotRequestNew=new GetTimeSlotRequest();
            getTimeSlotRequestNew.setBooking(strId);
            getTimeSlotRequestNew.setBusiness(getArguments().getString(Constant.KEY_VENDOR_ID));
        }

       /* ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text_black, getResources().getStringArray(R.array.time_slot));
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text_black);
        mTimeslot.setAdapter(adapterBrand);*/

        mMainLayout=  view.findViewById(R.id.main_layout);
        mPresenter=new BookingReschedulePresenter(this,mMainLayout);
    }

    public void onSuccess(BookingRescheduleResponse bookingRescheduleResponse) {

        Toast.makeText(getActivity(), bookingRescheduleResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();

        if(isCre)realmController.updateLeadBookingSchedule(strId,bookingRescheduleResponse.getGetData().getDate(),bookingRescheduleResponse.getGetData().getTime_slot());
        else realmController.updateBookingSchedule(strId,bookingRescheduleResponse.getGetData().getStatus(),bookingRescheduleResponse.getGetData().getDate(),bookingRescheduleResponse.getGetData().getTime_slot());

        getDialog().dismiss();
        getActivity().onBackPressed();
    }

    private boolean validate(){

        boolean flag=true;

        strDate=mDate.getText().toString();

        if(strDate.length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select date");
        } else if(mTimeslot.getSelectedItemPosition()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select time slot");
        } else {
            if(bookingSlotResponse!=null) strTime = bookingSlotResponse.getGetSlots().get(mTimeslot.getSelectedItemPosition()).getSlot();
            else flag=false;
        }

        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.booking_done:
                if(validate()){

                    ((HomeScreen) getActivity()).disableButton(btnDone);

                    BookingRescheduleRequest bookingRescheduleRequest=new BookingRescheduleRequest();
                    bookingRescheduleRequest.setTime_slot(strTime);
                    bookingRescheduleRequest.setId(strId);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date sourceDate = null;
                    try {
                        sourceDate = dateFormat.parse(strDate);
                        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String targetdatevalue= targetFormat.format(sourceDate);
                        // mPresenter.getSlots(targetdatevalue,strId);
                        bookingRescheduleRequest.setDate(targetdatevalue);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(isMyBooking) mPresenter.reScheduleMyBookings(bookingRescheduleRequest);
                    else mPresenter.reScheduleBookings(bookingRescheduleRequest);

                }

                /* int selectedId=mConvenience.getCheckedRadioButtonId();
                mConvenienceButton=(RadioButton)findViewById(selectedId);
                objAddBookingRequest.setConvenience(mConvenienceButton.getText().toString());*/
                //
                break;
            case R.id.date_text:
                setDate();
                break;

        }
    }

    public void setDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),null, year, month, day);
        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMinDate(now);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.setTitle("Select Date");
        //datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setVisibility(View.GONE);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "",datePickerDialog);  // hide cancel button
        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                //  datePickerDialog.dismiss();
                try {
                    String input_date = zeroPrefix(dayOfMonth) + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(year);
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    Date dt1 = format1.parse(input_date);
                    String dayOfTheWeek = (String) DateFormat.format("EEEE", dt1);
                    Log.d("Day", dayOfTheWeek);


                        mDate.setText(zeroPrefix(dayOfMonth) + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(year));

                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            Date sourceDate = null;
                            sourceDate = dateFormat.parse(mDate.getText().toString());
                            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String targetdatevalue= targetFormat.format(sourceDate);

                            GetTimeSlotRequest getTimeSlotRequest=new GetTimeSlotRequest();
                            getTimeSlotRequest.setDate(targetdatevalue);
                            getTimeSlotRequest.setBusiness(strBussId);
                            getTimeSlotRequest.setBooking(strId);

                            if(isMyBooking){
                                getTimeSlotRequestNew.setDate(targetdatevalue);
                                mPresenter.getMySlots(getTimeSlotRequestNew);
                            }
                            else
                                mPresenter.getSlots(strId,targetdatevalue);

                            //  bookingRescheduleRequest.setDate(targetdatevalue);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //datePickerDialog.dismiss();


                    datePickerDialog.dismiss();

                }catch (Exception e){

                }

            }});
        datePickerDialog.show();
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }


    public void onSlotsSuccess(BookingSlotResponse bookingSlotResponse) {
        this.bookingSlotResponse=bookingSlotResponse;
        BookingRescheduleSlotAdapter adapter = new BookingRescheduleSlotAdapter(getActivity(), R.layout.layout_spinner_text,bookingSlotResponse.getGetSlots(),"Select timeslot");
        mTimeslot.setAdapter(adapter);
    }
}
