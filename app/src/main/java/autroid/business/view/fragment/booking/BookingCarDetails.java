package autroid.business.view.fragment.booking;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.utils.Constant;

public class BookingCarDetails extends DialogFragment {

    RelativeLayout mainLayout;

    TextView carName,carReg,carVin,carEng;

    CarDetailBE carDetail;

    BookingDetailsResponse bookingDetailsResponse;

    public BookingCarDetails() {
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.booking_car_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        getBundleData();
        setUpData();
    }

    private void findViews(View view){
        mainLayout=view.findViewById(R.id.main_layout);
        carName=view.findViewById(R.id.car_name);
        carReg=view.findViewById(R.id.reg_no);
        carVin=view.findViewById(R.id.vin_no);
        carEng=view.findViewById(R.id.eng_no);
    }

    private void getBundleData(){
        bookingDetailsResponse= (BookingDetailsResponse) getArguments().getSerializable(Constant.RESPONSE);
        carDetail=bookingDetailsResponse.getBookingData().get(0).getCarDetail();
    }

    private void setUpData(){
        if(carDetail!=null){
            carName.setText(carDetail.getTitle());
            carReg.setText(carDetail.getRegistration_no());
            carVin.setText(carDetail.getVin());
            carEng.setText(carDetail.getEngine_no());
        }
    }
}
