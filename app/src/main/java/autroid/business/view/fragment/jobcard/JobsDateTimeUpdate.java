package autroid.business.view.fragment.jobcard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.request.UpdateDeliveryDetailsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobsDateTimeUpdatePresenter;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;

public class JobsDateTimeUpdate extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    private RelativeLayout mainLayout;

    private String bookingId="";

    private TextView commitmentDate, commitmentTime;

    private Button update;

    private int currentDay, currentMonth, currentYear, currentHour, currentMin;

    private Calendar calendar;

    private DatePickerDialog datePickerDialog;

    private TimePickerDialog timePickerDialog;

    private JobsDateTimeUpdatePresenter presenter;

    public JobsDateTimeUpdate() {
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_date_time_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mainLayout = view.findViewById(R.id.main_layout);
        commitmentDate = view.findViewById(R.id.commitment_date);
        commitmentTime = view.findViewById(R.id.commitment_time);
        update = view.findViewById(R.id.update);

        presenter=new JobsDateTimeUpdatePresenter(this,mainLayout);

        bookingId=getArguments().getString(Constant.BOOKING_ID);
        commitmentDate.setText(getArguments().getString(Constant.DATE));
        commitmentTime.setText(getArguments().getString(Constant.TIME));

        commitmentDate.setOnClickListener(v -> {
            ((HomeScreen) getActivity()).disableTextview(commitmentDate);
            calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        commitmentTime.setOnClickListener(v -> {
            ((HomeScreen) getActivity()).disableTextview(commitmentTime);
            calendar = Calendar.getInstance();
            currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            currentMin = calendar.get(Calendar.MINUTE);
            timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
            timePickerDialog.show();
        });

        update.setOnClickListener(v -> {
            UpdateDeliveryDetailsRequest request = new UpdateDeliveryDetailsRequest();
            request.setBooking(bookingId);
            request.setDelivery_date(commitmentDate.getText().toString().trim());
            request.setDelivery_time(commitmentTime.getText().toString().trim());
            presenter.updateDeliveryDetails(request);
        });
    }

    public void onSuccess(BaseResponse response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date sourceDate = null;
        String targetdatevalue = null;
        try {
            sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetdatevalue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        commitmentDate.setText(targetdatevalue);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        commitmentTime.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }
}

