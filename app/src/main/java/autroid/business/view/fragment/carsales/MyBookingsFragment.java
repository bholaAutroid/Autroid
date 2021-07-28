package autroid.business.view.fragment.carsales;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.analytics.FirebaseAnalytics;

import autroid.business.R;
import autroid.business.adapter.cars.MyBookingsAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.BookingsClickCallback;
import autroid.business.model.bean.BookingAddressBE;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.presenter.carsales.MyBookingsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.profile.ShowroomFragment;
import autroid.business.view.fragment.booking.BookingRescheduleFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */

public class MyBookingsFragment extends Fragment implements View.OnClickListener, BookingsClickCallback {

    FloatingActionButton mAddButton;

    RecyclerView mPendingList;

    MyBookingsPresenter mPresenter;

    RelativeLayout mMainLayout;

    MyBookingsAdapter mBookingsAdapter;

    RealmController mRealmController;

    Realm mRealm;

    TextView mEmptyText,mTitle;

    SwipeRefreshLayout mSwipeRefreshLayout;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    private FirebaseAnalytics mFirebaseAnalytics;

    private Dialog dialog;

    public MyBookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Bookings",null);

        mAddButton= (FloatingActionButton) view.findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(this);

        mTitle=view.findViewById(R.id.title);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();
        mRealmController.clearBookings();

        mEmptyText=view.findViewById(R.id.default_booking);
        mPendingList=view.findViewById(R.id.pending_booking_list);
        LinearLayoutManager llPending,llConfirmed,llCompleted;
        llPending = new LinearLayoutManager(getActivity());
        llPending.setOrientation(LinearLayoutManager.VERTICAL);
        mPendingList.setLayoutManager(llPending);

        mMainLayout=view.findViewById(R.id.main_layout);
        mPresenter=new MyBookingsPresenter(this,mMainLayout);
        mPresenter.getBookings(pageNo);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
               // realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);
                pageNo=0;
                mRealmController.clearBookings();
                mPresenter.getBookings(pageNo);
            }
        });

        mScrollListener = new EndlessScrollListener(llPending) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getBookings(pageNo);
            }
        };
        mPendingList.addOnScrollListener(mScrollListener);





       /* Boolean isShowcaseFirstTime=PreferenceManager.getInstance().getBooleanPreference(getActivity(),Constant.SP_SHOWCASE_BOOKING,false);
        Boolean isShowcaseFirstTimegGarage=PreferenceManager.getInstance().getBooleanPreference(getActivity(),Constant.SP_SHOWCASE_GARAGE,false);
            if(!isShowcaseFirstTime && isShowcaseFirstTimegGarage) {
                builder = new GuideView.Builder(getActivity())
                        .setTitle("Bookings")
                        .setContentText("Book your car service")
                        .setGravity(GuideView.Gravity.center)
                        .setDismissType(GuideView.DismissType.outside)
                        .setTargetView(mAddButton)
                        .setGuideListener(new GuideView.GuideListener() {
                            @Override
                            public void onDismiss(View view) {
                                switch (view.getId()) {
                                    case R.id.fab_add:
                                        PreferenceManager.getInstance().putBooleanPreference(getActivity(),Constant.SP_SHOWCASE_BOOKING,true);
                                        return;
                                }
                                mGuideView = builder.build();
                                mGuideView.show();
                            }
                        });

                mGuideView = builder.build();
                mGuideView.show();
            }*/
        }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_add:
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle=new Bundle();
                ((AwsHomeActivity) getActivity()).addFragment(new BookingCategoryFragment(), "BookingCategoryFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
                break;
        }
    }

    public void onSuccess(BookingsResponse bookingsResponse){

        mSwipeRefreshLayout.setRefreshing(false);

        mTitle.setText("My Bookings ("+bookingsResponse.getTotalResponse().getTotalResult()+")");

        if(pageNo==0) {

            if (bookingsResponse.getGetBookings().size() == 0) {
                mEmptyText.setVisibility(View.VISIBLE);
            } else {
                mEmptyText.setVisibility(View.GONE);
            }

        }

        for(int i=0;i<bookingsResponse.getGetBookings().size();i++){

            if(bookingsResponse.getGetBookings().get(i).getListing().equalsIgnoreCase("booking")) {
                BookingRealm bookingRealm = new BookingRealm();
                bookingRealm.setBookingId(bookingsResponse.getGetBookings().get(i).getId());
                bookingRealm.setJob_no(bookingsResponse.getGetBookings().get(i).getJob_no());
                bookingRealm.setVehicleTitle(bookingsResponse.getGetBookings().get(i).getCar().getTitle());
                bookingRealm.setCarId(bookingsResponse.getGetBookings().get(i).getCar().getId());
                bookingRealm.setDated(bookingsResponse.getGetBookings().get(i).getDate());
                bookingRealm.setPrice(bookingsResponse.getGetBookings().get(i).getPayment().getTotal());
                bookingRealm.setPickupAmount((float) bookingsResponse.getGetBookings().get(i).getPayment().getPick_up_charges());
                bookingRealm.setCareager_cash(bookingsResponse.getGetBookings().get(i).getPayment().getCareager_cash());
                bookingRealm.setPaidTotal(bookingsResponse.getGetBookings().get(i).getPayment().getPaid_total());
                bookingRealm.setCoupon(bookingsResponse.getGetBookings().get(i).getPayment().getCoupon());
                bookingRealm.setDiscount_type(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount_type());
                bookingRealm.setDiscount(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount());
                bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
                bookingRealm.setBusinessName(bookingsResponse.getGetBookings().get(i).getBusiness().getName());
                bookingRealm.setBusinessId(bookingsResponse.getGetBookings().get(i).getBusiness().getId());
                bookingRealm.setShortId(bookingsResponse.getGetBookings().get(i).getBooking_no());
                bookingRealm.setStatus(bookingsResponse.getGetBookings().get(i).getStatus());
                bookingRealm.setTimeSlot(bookingsResponse.getGetBookings().get(i).getTime_slot());
                bookingRealm.setListing(bookingsResponse.getGetBookings().get(i).getListing());
                bookingRealm.setRegistrationNumber(bookingsResponse.getGetBookings().get(i).getCar().getRegistration_no());
                bookingRealm.setPrimaryStatus("MyBooking");

                if (bookingsResponse.getGetBookings().get(i).getDue() != null) {
                    bookingRealm.setDue(bookingsResponse.getGetBookings().get(i).getDue().getDue());
                }


                if (bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().size() > 0)
                    bookingRealm.setCarImage(bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().get(0).getFile_address());


                mRealm.beginTransaction();
                mRealm.copyToRealm(bookingRealm);
                mRealm.commitTransaction();
            }
            else if(bookingsResponse.getGetBookings().get(i).getListing().equalsIgnoreCase("order")) {
                BookingRealm bookingRealm = new BookingRealm();
                bookingRealm.setBookingId(bookingsResponse.getGetBookings().get(i).getId());
                bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
                bookingRealm.setDated(bookingsResponse.getGetBookings().get(i).getDate());
                bookingRealm.setPrice(bookingsResponse.getGetBookings().get(i).getPayment().getTotal());
                bookingRealm.setPickupAmount((float) bookingsResponse.getGetBookings().get(i).getPayment().getPick_up_charges());
                bookingRealm.setPaidTotal(bookingsResponse.getGetBookings().get(i).getPayment().getPaid_total());
                bookingRealm.setCoupon(bookingsResponse.getGetBookings().get(i).getPayment().getCoupon());
                bookingRealm.setDiscount_type(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount_type());
                bookingRealm.setDiscount(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount());
                bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
                bookingRealm.setBusinessName(bookingsResponse.getGetBookings().get(i).getBusiness().getName());
                bookingRealm.setBusinessId(bookingsResponse.getGetBookings().get(i).getBusiness().getId());
                bookingRealm.setShortId(bookingsResponse.getGetBookings().get(i).getOrder_no());
                bookingRealm.setTimeSlot(bookingsResponse.getGetBookings().get(i).getTime_slot());
                bookingRealm.setListing(bookingsResponse.getGetBookings().get(i).getListing());
                bookingRealm.setStatus(bookingsResponse.getGetBookings().get(i).getStatus());
                bookingRealm.setPrimaryStatus("MyBooking");



                if(bookingsResponse.getGetBookings().get(i).getAddress()!=null) {
                    String address = setAddress(bookingsResponse.getGetBookings().get(i).getAddress());
                    bookingRealm.setAddress(address);
                }

                if (bookingsResponse.getGetBookings().get(i).getDue() != null) {
                     bookingRealm.setDue(bookingsResponse.getGetBookings().get(i).getDue().getDue());
                }



                mRealm.beginTransaction();
                mRealm.copyToRealm(bookingRealm);
                mRealm.commitTransaction();
            }

        }

        if(pageNo==0) {
                mBookingsAdapter = new MyBookingsAdapter(mRealmController.getBookings("MyBooking"), true, getActivity(), this);
                mPendingList.setAdapter(mBookingsAdapter);
        }

    }

    private String setAddress(BookingAddressBE bookingAddressBE) {
        String address=bookingAddressBE.getAddress();
        if(bookingAddressBE.getArea().length()>0)
            address=address+", "+bookingAddressBE.getArea();

        if(bookingAddressBE.getLandmark().length()>0){
            address=address+", "+bookingAddressBE.getLandmark();
        }

        if(bookingAddressBE.getCity().length()>0){
            address=address+", "+bookingAddressBE.getCity();
        }

        if(bookingAddressBE.getState().length()>0){
            address=address+", "+bookingAddressBE.getState();
        }

        if(bookingAddressBE.getZip().length()>0){
            address=address+" "+bookingAddressBE.getZip();
        }

        return address;
    }

    @Override
    public void onReBookClick(String businessId,String businessName,String carID,String carName,String status) {

    }

    @Override
    public void onCancelClick(String bookingID,String status) {
        if(status.equalsIgnoreCase("Pending")|| status.equalsIgnoreCase("Confirmed") || status.equalsIgnoreCase("Approval")) {
            deletePostDialog(bookingID);
        }
        else {
            Utility.showResponseMessage(mMainLayout,"This Booking can't be Cancelled");
        }
    }

    @Override
    public void onOrderCancelClick(String bookingID, String status) {
    }

    @Override
    public void onDetailClick(String bookingID) {


        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,bookingID);

        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new MyBookingDetailFragment(), "MyBookingDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onOrderDetailClick(String bookingID) {

    }

    @Override
    public void onRescheduleClick(String bookingID,String status,String businessId) {
        if(status.equalsIgnoreCase("Pending")|| status.equalsIgnoreCase("Confirmed") || status.equalsIgnoreCase("Approval")) {
            BookingRescheduleFragment bookingRescheduleFragment = new BookingRescheduleFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID, bookingID);
            bundle.putString(Constant.KEY_VENDOR_ID,businessId);
            bundle.putBoolean(Constant.KEY_MY_BOOKING,true);
            bookingRescheduleFragment.setArguments(bundle);
            bookingRescheduleFragment.show(getChildFragmentManager(), "BookingRescheduleFragment");
        }
    }

    void deletePostDialog(final String bookingID){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure, You want to cancel this booking?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookingStatusRequest bookingStatusRequest=new BookingStatusRequest();
                        bookingStatusRequest.setId(bookingID);
                        bookingStatusRequest.setStatus("Cancelled");
                        mPresenter.updateStatus(bookingStatusRequest);
                    }
                });

        builder1.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public void onBusinessClick(String businessId) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,businessId);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new ShowroomFragment(), "ShowroomFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onInvoiceClick(String bookingID) {

      /*  Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiURL.INVOICE_PREMIX+bookingID+".pdf"));
        startActivity(browserIntent);*/

//        BookingDetailDialogFragment bookingDetailDialogFragment=new BookingDetailDialogFragment();
//        Bundle bundle=new Bundle();
//        bundle.putString(Constant.KEY_ID,bookingID);
//        bundle.putBoolean(Constant.KEY_IS_VIEW,false);
//        bookingDetailDialogFragment.setArguments(bundle);
//        bookingDetailDialogFragment.show(getChildFragmentManager(),"BookingDetailDialogFragment");
    }

    @Override
    public void onPayDue(String bookingId, String status, float due, String type) {

    }


    public void onStatusSuccess(BaseResponse baseResponse, BookingStatusRequest bookingStatusRequest) {
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());

        Bundle params = new Bundle();
        params.putString("updated_status",bookingStatusRequest.getStatus());
        mFirebaseAnalytics.logEvent("bookings", params);
        mRealmController.updateBookingStatus(bookingStatusRequest.getId(),bookingStatusRequest.getStatus());
    }

}
