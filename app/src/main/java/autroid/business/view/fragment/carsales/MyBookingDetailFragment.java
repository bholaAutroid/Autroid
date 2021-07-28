package autroid.business.view.fragment.carsales;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.cars.BookingTrackingAdapter;
import autroid.business.camera.DisplayGridFragment;
import autroid.business.model.bean.InspectionDataBE;
import autroid.business.model.bean.InspectionImageBE;
import autroid.business.model.response.BookingDetailsResponse;
import autroid.business.model.response.CarInspectionResponse;
import autroid.business.presenter.MyBookingDetailsPresenter;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.jobcard.JobsCarDetails;
import autroid.business.view.fragment.jobcard.JobsClaimDetails;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyBookingDetailFragment extends Fragment implements View.OnClickListener {

    MyBookingDetailsPresenter mPresenter;

    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.layout_services)
    LinearLayout mBookingDetail;
    @BindView(R.id.layout_estimate)
    LinearLayout mEstimate;
    @BindView(R.id.layout_jobcard)
    LinearLayout mJobCard;
    @BindView(R.id.layout_gallery)
    LinearLayout mgallery;
    @BindView(R.id.layout_invoice)
    LinearLayout mLayoutInvoice;
    @BindView(R.id.layout_claim_info)
    LinearLayout mClaimInfo;

    @BindView(R.id.booking_no)
    TextView mBookingNO;
    @BindView(R.id.car)
    TextView mCar;

    @BindView(R.id.claim)
    TextView mClaim;
    @BindView(R.id.invoice)
    TextView mInvoice;
    @BindView(R.id.gallery)
    TextView mGallery;
    @BindView(R.id.jobcard)
    TextView mtvJobCard;

    @BindView(R.id.tracking)
    RecyclerView mTracking;

    String bookingID;
    BookingDetailsResponse jobResponse;

    private String stages[] = {"NewJob", "Estimation", "In-Process", "QC", "StoreApproval", "GMApproval", "Ready"};


    public MyBookingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_booking_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter = new MyBookingDetailsPresenter(this, mMainLayout);

        mBookingDetail.setOnClickListener(this);
        mEstimate.setOnClickListener(this);

        bookingID = getArguments().getString(Constant.KEY_ID);
        mPresenter.getBookingDetails(bookingID, "id");
        mJobCard.setOnClickListener(this);
        mgallery.setOnClickListener(this);
        mClaimInfo.setOnClickListener(this);
        mLayoutInvoice.setOnClickListener(this);

        mTracking.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void onSuccess(BookingDetailsResponse jobResponse) {
        this.jobResponse = jobResponse;
        mBookingNO.setText("JOB #" + jobResponse.getBookingData().get(0).getBooking_no());
        mCar.setText(jobResponse.getBookingData().get(0).getCarDetail().getTitle() + "\n" + jobResponse.getBookingData().get(0).getCarDetail().getRegistration_no());

        if (jobResponse.getBookingData().get(0).getJob_no() == null || jobResponse.getBookingData().get(0).getJob_no().length() == 0) {
            mgallery.setEnabled(false);
            mGallery.setTextColor(getResources().getColor(R.color.text_black));

            mJobCard.setEnabled(false);
            mtvJobCard.setTextColor(getResources().getColor(R.color.text_black));

           /* mLayoutInvoice.setEnabled(false);
            mInvoice.setTextColor(getResources().getColor(R.color.text_black));*/

            mClaimInfo.setEnabled(false);
            mClaim.setTextColor(getResources().getColor(R.color.text_black));

            BookingTrackingAdapter bookingTrackingAdapter = new BookingTrackingAdapter(getActivity(), stages, jobResponse.getBookingData().get(0).getLogs(), "");
            mTracking.setAdapter(bookingTrackingAdapter);

        } else {

            if (jobResponse.getBookingData().get(0).getInsuranceData() != null)
                if (!jobResponse.getBookingData().get(0).getInsuranceData().isClaim()) {
                    mClaimInfo.setEnabled(false);
                    mClaim.setTextColor(getResources().getColor(R.color.text_black));
                    //@drawable/button_white_10dp
                }

            BookingTrackingAdapter bookingTrackingAdapter = new BookingTrackingAdapter(getActivity(), stages, jobResponse.getBookingData().get(0).getLogs(), jobResponse.getBookingData().get(0).getDelivery_date() + " " + jobResponse.getBookingData().get(0).getDelivery_time());
            mTracking.setAdapter(bookingTrackingAdapter);

        }

    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_services: {
                BookingDetailDialogFragment bookingDetailDialogFragment = new BookingDetailDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, bookingID);
                bundle.putBoolean(Constant.KEY_IS_VIEW, true);
                bookingDetailDialogFragment.setArguments(bundle);
                bookingDetailDialogFragment.show(getChildFragmentManager(), "BookingDetailDialogFragment");
                break;
            }
            case R.id.layout_estimate:
                BookingDetailDialogFragment bookingDetailDialogFragment = new BookingDetailDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, bookingID);
                bundle.putBoolean(Constant.KEY_IS_VIEW, false);
                bookingDetailDialogFragment.setArguments(bundle);
                bookingDetailDialogFragment.show(getChildFragmentManager(), "BookingDetailDialogFragment");
                // ((HomeScreenActivity) getActivity()).showFullScreen();
                //((HomeScreenActivity) getActivity()).addFragment(new BookingDetailDialogFragment(), "BookingDetailDialogFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
                break;

            case R.id.layout_jobcard:
                if (jobResponse != null) {
                    JobsCarDetails jobsCarDetails = new JobsCarDetails();
                    Bundle jobBundle = new Bundle();
                    jobBundle.putSerializable(Constant.RESPONSE, jobResponse);
                    jobBundle.putBoolean(Constant.KEY_IS_VIEW, true);
                    jobsCarDetails.setArguments(jobBundle);
                    jobsCarDetails.show(getChildFragmentManager(), "JobsCarDetails");
                }
                break;

            case R.id.layout_gallery:
                mPresenter.getCarImages(bookingID);
                break;

            case R.id.layout_claim_info:
                JobsClaimDetails jobsClaimDetails = new JobsClaimDetails();
                Bundle claimBundle = new Bundle();
                claimBundle.putSerializable(Constant.INSURANCE_DETAILS, jobResponse.getBookingData().get(0).getInsuranceData());
                jobsClaimDetails.setArguments(claimBundle);
                jobsClaimDetails.show(getChildFragmentManager(), "JobsClaimDetails");
                break;

            case R.id.layout_invoice:

                if (jobResponse != null) {
                    PaymentDetailsFragment paymentDetailsFragment = new PaymentDetailsFragment();
                    Bundle paymentBundle = new Bundle();
                    paymentBundle.putSerializable(Constant.VALUE, jobResponse.getBookingData().get(0));
                    paymentBundle.putString(Constant.BOOKING_ID,bookingID);
                    paymentDetailsFragment.setArguments(paymentBundle);
                    paymentDetailsFragment.show(getChildFragmentManager(), "PaymentDetailsFragment");
                }

                break;
        }
    }

    public void onImagesSuccess(CarInspectionResponse response) {

        ArrayList<InspectionImageBE> arrayList = new ArrayList<>();

        for (InspectionDataBE data : response.getInspectionData()) {
            InspectionImageBE image = new InspectionImageBE();
            image.setImgUrl(data.getImageAddress());
            image.setImgId(data.getImageId());
            image.setIndex(data.getIndex());
            arrayList.add(image);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("inspection_list", arrayList);
        ((AwsHomeActivity) getActivity()).addFragment(new DisplayGridFragment(), "DisplayGrid", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }
}
