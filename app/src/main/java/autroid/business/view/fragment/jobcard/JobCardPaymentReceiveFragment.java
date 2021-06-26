package autroid.business.view.fragment.jobcard;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.request.JobCardPaymentRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.PaymentModeResponse;
import autroid.business.presenter.jobcard.JobCardPaymentReceivePresenter;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobCardPaymentReceiveFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.transaction_id)
    EditText mTransactionId;

    @BindView(R.id.amount)
    EditText mAmount;

    @BindView(R.id.due)
    TextView mDue;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.payment_mode)
    AppCompatSpinner mPaymentMode;

    @BindView(R.id.btn_done)
    Button mDone;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    JobCardPaymentReceivePresenter mPresenter;

    String bookingId,stringAmount,strMode,strTransactionID,strDate;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    int currentDay, currentMonth, currentYear;




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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_job_card_payment_receive, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mPresenter=new JobCardPaymentReceivePresenter(this,mMainLayout);

        bookingId=getArguments().getString(Constant.KEY_ID);

        mDue.setText("Due : "+getArguments().getString(Constant.KEY_TYPE));
        mPresenter.getMode();

        mDone.setOnClickListener(this);

        mDate.setOnClickListener(v->{
            ((HomeScreen) getActivity()).disableTextview(mDate);
            datePickerDialog.show();
        });


        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_done:
                if(validate()) {
                    JobCardPaymentRequest jobCardPaymentRequest = new JobCardPaymentRequest();
                    jobCardPaymentRequest.setAmount(stringAmount);
                    jobCardPaymentRequest.setBooking(bookingId);
                    jobCardPaymentRequest.setPayment_mode(strMode);
                    jobCardPaymentRequest.setTransaction_id(strTransactionID);
                    jobCardPaymentRequest.setDate(strDate);
                    mPresenter.receivePayment(jobCardPaymentRequest);
                }
                break;
        }
    }

    public void onSuccesPayment(PaymentModeResponse body) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_remark, body.getGetMode());
        mPaymentMode.setAdapter(arrayAdapter);
    }

    private boolean validate(){
        boolean flag=true;

        stringAmount=mAmount.getText().toString();
        strTransactionID=mTransactionId.getText().toString();
        strMode=mPaymentMode.getSelectedItem().toString();
        strDate=mDate.getText().toString();
        if(stringAmount.length()==0){
            flag=false;
            Toast.makeText(getActivity(), "Please Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else if(!strMode.equalsIgnoreCase("Cash") && strTransactionID.trim().length()==0){
            flag=false;
            Toast.makeText(getActivity(), "Please Enter Transaction ID", Toast.LENGTH_SHORT).show();
        }


        return flag;
    }

    public void onSuccesPaymentReceive(BaseResponse body) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
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

        mDate.setText(targetdatevalue);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

}
