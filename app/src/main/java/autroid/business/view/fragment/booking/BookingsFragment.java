package autroid.business.view.fragment.booking;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import autroid.business.R;
import autroid.business.adapter.BookingPendingAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.BookingStatusRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BookingsResponse;
import autroid.business.model.response.StatusCountResponse;
import autroid.business.presenter.BookingsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.jobcard.JobCardDetailFragment;
import autroid.business.view.fragment.jobcard.JobCardUserFragment;
import autroid.business.view.fragment.jobcard.JobCardCarFragment;
import autroid.business.view.fragment.leads.LeadCreateFragment;
import io.realm.Realm;
import io.realm.RealmList;

import static android.content.Context.CLIPBOARD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */

public class BookingsFragment extends Fragment implements View.OnClickListener,BookingStatusCallback {

    RecyclerView mBookingList;
    BookingsPresenter mPresenter;
    ConstraintLayout mMainLayout;
    BookingPendingAdapter mBookingPendingAdapter;
    RealmController mRealmController;
    Realm mRealm;

    TextView mTitle,mDefMessage;
    FloatingActionButton fab;

    private FirebaseAnalytics mFirebaseAnalytics;

    private String bookingStatus[] = {"Pending", "Confirmed", "Completed", "Rejected", "Cancelled", "Missed"};
    private String bookingStatusCount[] = {"Pending", "Confirmed", "Completed", "Rejected", "Cancelled", "Missed"};

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout mSwipeRefreshLayout;

    int pageNo = 0;
    String stage = "", sortBy = "date";

    private Boolean isSorted = false, isSearch = false,isJobCard=false;
    private StatusCountResponse statusCountResponse;


    public BookingsFragment() {
        // Required empty public constructor
    }

    public static BookingsFragment newInstance() {
        BookingsFragment fragment = new BookingsFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stage=getArguments().getString(Constant.KEY_TYPE);
        sortBy=getArguments().getString(Constant.Key_Source);
        isJobCard=getArguments().getBoolean(Constant.IS_JOBCARD);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController = RealmController.getInstance();
        mRealmController.clearBookingsByStage(stage);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Bookings", null);

        mDefMessage=view.findViewById(R.id.default_message);
        fab=view.findViewById(R.id.fab_add);
        mTitle=view.findViewById(R.id.title);
        mMainLayout = view.findViewById(R.id.main_layout);

        fab.hide();

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                mRealmController.clearBookingsByStage(stage);
                pageNo = 0;
                isSearch = false;
                if(!isJobCard) getBooking();
            }
        });

        mBookingList = (RecyclerView) view.findViewById(R.id.booking_list);

        LinearLayoutManager llPending;
        llPending = new LinearLayoutManager(getActivity());
        llPending.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llPending);

        mPresenter = new BookingsPresenter(this, mMainLayout);
       // mPresenter.getStatusCount();


        if(!isJobCard) {
            getBooking();
        }
        else {
            mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getBookings("Jobcard"), true, getActivity(), this);
            mBookingList.setAdapter(mBookingPendingAdapter);
        }

        mScrollListener = new EndlessScrollListener(llPending) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo = page;
                isSearch = false;
                if(!isJobCard) getBooking();
            }
        };

        mBookingList.addOnScrollListener(mScrollListener);

        fab.setOnClickListener(v->{
            Bundle bundle=new Bundle();
            bundle.putBoolean(Constant.IS_MANUAL,true);
            ((AwsHomeActivity)getActivity()).addFragment(new JobCardUserFragment(),"JobCardUserFragment",true,false,bundle,((AwsHomeActivity)getActivity()).currentFrameId);
        });

    }

    @Override
    public void onClick(View view) {
            switch (view.getId()) {
        }
    }

    public void onSuccess(BookingsResponse bookingsResponse/*, String status*/, int page) {

        mSwipeRefreshLayout.setRefreshing(false);

        if(stage.equals(Constant.APPROVAL))fab.show();

        if(stage.equalsIgnoreCase(BookingsPagerFragment.stages[0])){
            mTitle.setText("APPROVAL ("+bookingsResponse.getTotalResponse().getTotalResult()+")");
        }else if(stage.equalsIgnoreCase(BookingsPagerFragment.stages[1])){
            mTitle.setText("UPCOMING BOOKINGS ("+bookingsResponse.getTotalResponse().getTotalResult()+")");
        } else if(stage.equalsIgnoreCase(BookingsPagerFragment.stages[2])){
            mTitle.setText("MISSED BOOKINGS ("+bookingsResponse.getTotalResponse().getTotalResult()+")");
        } /*else if(status.equalsIgnoreCase(BookingsPagerFragment.stages[3])){
            mTitle.setText("MISSED BOOKINGS ("+bookingsResponse.getTotalResponse().getTotalResult()+")");
        }*/

        for (int i = 0; i < bookingsResponse.getGetBookings().size(); i++) {

            BookingRealm bookingRealm = new BookingRealm();
            bookingRealm.setBookingId(bookingsResponse.getGetBookings().get(i).getId());
            bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
            bookingRealm.setDated(bookingsResponse.getGetBookings().get(i).getDate());

            if(bookingsResponse.getGetBookings().get(i).getPayment()!=null) {
                bookingRealm.setPrice(bookingsResponse.getGetBookings().get(i).getPayment().getTotal());
                bookingRealm.setPaidTotal(bookingsResponse.getGetBookings().get(i).getPayment().getPaid_total());
                bookingRealm.setCoupon(bookingsResponse.getGetBookings().get(i).getPayment().getCoupon());
                bookingRealm.setDiscount_type(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount_type());
                bookingRealm.setDiscount(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount());
            }

            if(bookingsResponse.getGetBookings().get(i).getCar()!=null){
                bookingRealm.setVehicleTitle(bookingsResponse.getGetBookings().get(i).getCar().getTitle());
                bookingRealm.setRegistrationNumber(bookingsResponse.getGetBookings().get(i).getCar().getRegistration_no());
            }

            if(bookingsResponse.getGetBookings().get(i).getUser()!=null){
                bookingRealm.setProviderName(bookingsResponse.getGetBookings().get(i).getUser().getName());
                bookingRealm.setmUserName(bookingsResponse.getGetBookings().get(i).getUser().getName());

                bookingRealm.setmUserNumber(bookingsResponse.getGetBookings().get(i).getUser().getContact_no());
                bookingRealm.setmUserId(bookingsResponse.getGetBookings().get(i).getUser().getId());
            }

            bookingRealm.setShortId(bookingsResponse.getGetBookings().get(i).getBooking_no());
            bookingRealm.setTimeSlot(bookingsResponse.getGetBookings().get(i).getTime_slot());

            bookingRealm.setPrimaryStatus(stage);
            bookingRealm.setStatus(bookingsResponse.getGetBookings().get(i).getStatus());

            if(bookingsResponse.getGetBookings().get(i).getServices()!=null) {
                if (bookingsResponse.getGetBookings().get(i).getServices().size() > 0) {
                    RealmList<SelectedBookingDataRealm> realmListMedia = new RealmList<>();

                    for (int j = 0; j < bookingsResponse.getGetBookings().get(i).getServices().size(); j++) {
                        SelectedBookingDataRealm selectedBookingDataRealm = new SelectedBookingDataRealm();
                        selectedBookingDataRealm.setPackageName(bookingsResponse.getGetBookings().get(i).getServices().get(j).getService());
                        selectedBookingDataRealm.setCost(bookingsResponse.getGetBookings().get(i).getServices().get(j).getCost());
                        realmListMedia.add(selectedBookingDataRealm);
                    }

                    bookingRealm.setSelectedBookingDataRealms(realmListMedia);
                }
            }

            mRealm.beginTransaction();
            mRealm.copyToRealm(bookingRealm);
            mRealm.commitTransaction();
        }

        if (page == 0) {
            if(bookingsResponse.getGetBookings().size()>0){
                mDefMessage.setVisibility(View.GONE);
            }
            else {
                mDefMessage.setVisibility(View.VISIBLE);
            }

            mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getBookings(stage), true, getActivity(), this);
            mBookingList.setAdapter(mBookingPendingAdapter);

          /*  if (!status.equalsIgnoreCase("Missed")) {
                mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getBookings(status), true, getActivity(), this);
                mBookingList.setAdapter(mBookingPendingAdapter);
            } else {
                mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getAllBookings(), true, getActivity(), this);
                mBookingList.setAdapter(mBookingPendingAdapter);
            }*/
        }

    }

    @Override
    public void confirmBooking(int pos, String bookingId) {
        BookingStatusRequest bookingStatusRequest = new BookingStatusRequest();
        bookingStatusRequest.setId(bookingId);
        bookingStatusRequest.setStatus(bookingStatus[1]);
        mPresenter.updateStatus(bookingStatusRequest);
    }

    @Override
    public void rejectBooking(int pos, String bookingId) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.reject_dialog,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Rejection Remark");
        builder.setView(view);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v->{
            EditText text=view.findViewById(R.id.remarks);
            if (text.getText().toString().trim().length()!=0) {
                BookingStatusRequest bookingStatusRequest=new BookingStatusRequest();
                bookingStatusRequest.setId(bookingId);
                bookingStatusRequest.setStatus(bookingStatus[3]);
                bookingStatusRequest.setRemark(text.getText().toString().trim());
                alertDialog.dismiss();
                mPresenter.updateStatus(bookingStatusRequest);
            }
        });


    }

    @Override
    public void onDetailClick(String bookingId, String statusFromRealm) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, bookingId);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

//    @Override
//    public void onDetailClick(String bookingID) {
//    }


    @Override
    public void onCallClick(String contact) {
        Utility.onCallClick(contact,getActivity());
    }

    @Override
    public void onChatClick(String userId) {

       //Utility.onChatClick(userId,getActivity());
    }

    @Override
    public void onRescheduleClick(String bookingID, String status) {
        if (status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Confirmed")) {
            BookingRescheduleFragment bookingRescheduleFragment = new BookingRescheduleFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID, bookingID);
            bookingRescheduleFragment.setArguments(bundle);
            bookingRescheduleFragment.show(getChildFragmentManager(), "BookingRescheduleFragment");
        }
    }

    @Override
    public void completeBooking(int pos, String bookingId) {
        BookingStatusRequest bookingStatusRequest = new BookingStatusRequest();
        bookingStatusRequest.setId(bookingId);
        bookingStatusRequest.setStatus(bookingStatus[2]);
        mPresenter.updateStatus(bookingStatusRequest);
    }

    @Override
    public void onAddressClick(String address, String convenience) {
        addressCall(address, convenience);
    }

    @Override
    public void createLead(String name, String mobile, String email, String bookingId) {
        // mRealmController.getBookings(status,bookingId);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.Key_Business_Name, name);
        bundle.putString(Constant.Key_Mobile, mobile);
        bundle.putString(Constant.Key_Email, email);
        bundle.putString(Constant.Key_Source, "Booking: " + bookingId);
        ((AwsHomeActivity) getActivity()).addFragment(new LeadCreateFragment(), "LeadCreateFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void createJobCard(String userId, String bookingId) {
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_BOOKING, true);
        bundle.putString(Constant.USER_ID, userId);
        bundle.putString(Constant.BOOKING_ID, bookingId);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarFragment(), "CarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }


    public void onStatusSuccess(BaseResponse baseResponse, BookingStatusRequest bookingStatusRequest) {
        Utility.showResponseMessage(mMainLayout, baseResponse.getResponseMessage());

        Bundle params = new Bundle();
        params.putString("updated_status", bookingStatusRequest.getStatus());
        mFirebaseAnalytics.logEvent("bookings", params);

        mRealmController.updateBookingStatus(bookingStatusRequest.getId(), bookingStatusRequest.getStatus());
        mBookingPendingAdapter.notifyDataSetChanged();
    }

    public void onSuccessNumber(BookingsResponse bookingsResponse) {

        //   mStatus.setSelection(0);
        if (bookingsResponse.getGetBookings().size() > 0) {
            mRealmController.clearBookings();
        }

        for (int i = 0; i < bookingsResponse.getGetBookings().size(); i++) {
            String status = bookingsResponse.getGetBookings().get(i).getStatus();
            BookingRealm bookingRealm = new BookingRealm();
            bookingRealm.setBookingId(bookingsResponse.getGetBookings().get(i).getId());
            bookingRealm.setVehicleTitle(bookingsResponse.getGetBookings().get(i).getCar().getTitle());
            bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
            bookingRealm.setDated(bookingsResponse.getGetBookings().get(i).getDate());
            bookingRealm.setUpdatedAt(bookingsResponse.getGetBookings().get(i).getUpdated_at());
            bookingRealm.setPrice(bookingsResponse.getGetBookings().get(i).getPayment().getTotal());
            bookingRealm.setPaidTotal(bookingsResponse.getGetBookings().get(i).getPayment().getPaid_total());

            bookingRealm.setDiscount_type(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount_type());
            bookingRealm.setDiscount(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount());
            bookingRealm.setmUserName(bookingsResponse.getGetBookings().get(i).getUser().getName());
            bookingRealm.setmUserNumber(bookingsResponse.getGetBookings().get(i).getUser().getContact_no());
            bookingRealm.setmUserId(bookingsResponse.getGetBookings().get(i).getUser().getId());
            bookingRealm.setShortId(bookingsResponse.getGetBookings().get(i).getBooking_no());
            bookingRealm.setTimeSlot(bookingsResponse.getGetBookings().get(i).getTime_slot());
            bookingRealm.setRegistrationNumber(bookingsResponse.getGetBookings().get(i).getCar().getRegistration_no());
            bookingRealm.setStatus(status);

            if (bookingsResponse.getGetBookings().get(i).getAddress() != null) {
                String address = bookingsResponse.getGetBookings().get(i).getAddress().getAddress();
                if (bookingsResponse.getGetBookings().get(i).getAddress().getArea().length() > 0)
                    address = address + ", " + bookingsResponse.getGetBookings().get(i).getAddress().getArea();

                if (bookingsResponse.getGetBookings().get(i).getAddress().getLandmark().length() > 0) {
                    address = address + ", " + bookingsResponse.getGetBookings().get(i).getAddress().getLandmark();
                }

                if (bookingsResponse.getGetBookings().get(i).getAddress().getCity().length() > 0) {
                    address = address + ", " + bookingsResponse.getGetBookings().get(i).getAddress().getCity();
                }

                if (bookingsResponse.getGetBookings().get(i).getAddress().getState().length() > 0) {
                    address = address + ", " + bookingsResponse.getGetBookings().get(i).getAddress().getState();
                }

                if (bookingsResponse.getGetBookings().get(i).getAddress().getZip().length() > 0) {
                    address = address + " " + bookingsResponse.getGetBookings().get(i).getAddress().getZip();
                }

                bookingRealm.setAddress(address);
            }

            if (bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().size() > 0)
                bookingRealm.setCarImage(bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().get(0).getFile_address());
            if (bookingsResponse.getGetBookings().get(i).getServices().size() > 0) {
                RealmList<SelectedBookingDataRealm> realmListMedia = new RealmList<>();

                for (int j = 0; j < bookingsResponse.getGetBookings().get(i).getServices().size(); j++) {
                    SelectedBookingDataRealm selectedBookingDataRealm = new SelectedBookingDataRealm();
                    selectedBookingDataRealm.setPackageName(bookingsResponse.getGetBookings().get(i).getServices().get(j).getService());
                    selectedBookingDataRealm.setCost(bookingsResponse.getGetBookings().get(i).getServices().get(j).getCost());
                    realmListMedia.add(selectedBookingDataRealm);
                }

                bookingRealm.setSelectedBookingDataRealms(realmListMedia);
            }

            mRealm.beginTransaction();
            mRealm.copyToRealm(bookingRealm);
            mRealm.commitTransaction();
            mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getAllBookings(), true, getActivity(), this);
            mBookingList.setAdapter(mBookingPendingAdapter);
        }
    }

    private void getBooking() {
        if (!isSearch) mPresenter.getBookings(stage, pageNo,sortBy);
    }


    private void addressCall(final String address, String convenience) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());

        builder1.setTitle(convenience);
        builder1.setMessage(address);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Copy",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", address);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Address Copied", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });


        builder1.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
