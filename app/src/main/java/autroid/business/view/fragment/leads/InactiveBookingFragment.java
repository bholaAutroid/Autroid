package autroid.business.view.fragment.leads;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//import com.qiscus.sdk.Qiscus;

import autroid.business.R;
import autroid.business.adapter.BookingPendingAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.response.BookingsResponse;
import autroid.business.presenter.InactiveBookingPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.BookingDetailFragment;
import autroid.business.view.fragment.jobcard.JobCardCarFragment;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InactiveBookingFragment extends Fragment implements BookingStatusCallback {

    RecyclerView mBookingList;
    LinearLayout mMainLayout;
    BookingPendingAdapter mBookingPendingAdapter;
    RealmController mRealmController;
    Realm mRealm;
    InactiveBookingPresenter mPresenter;

    int pageNo=0;
    String status="Inactive";

    EndlessScrollListener mScrollListener = null;

    public InactiveBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inactive_booking, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();
        mRealmController.clearBookingsByStage("Inactive");


        mBookingList= (RecyclerView) view.findViewById(R.id.booking_list);

        LinearLayoutManager llPending,llConfirmed,llCompleted;
        llPending = new LinearLayoutManager(getActivity());
        llPending.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llPending);

        mMainLayout= (LinearLayout) view.findViewById(R.id.main_layout);
        mPresenter=new InactiveBookingPresenter(this,mMainLayout);

        mPresenter.getBookings(status,pageNo);

        mScrollListener = new EndlessScrollListener(llPending) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getBookings(status,pageNo);

            }
        };

        mBookingList.addOnScrollListener(mScrollListener);


    }

    public void onSuccess(BookingsResponse bookingsResponse, String status){

        for(int i=0;i<bookingsResponse.getGetBookings().size();i++){

            BookingRealm bookingRealm=new BookingRealm();
            bookingRealm.setBookingId(bookingsResponse.getGetBookings().get(i).getId());
            bookingRealm.setVehicleTitle(bookingsResponse.getGetBookings().get(i).getCar().getTitle());
            bookingRealm.setConvenience(bookingsResponse.getGetBookings().get(i).getConvenience());
            bookingRealm.setDated(bookingsResponse.getGetBookings().get(i).getDate());
            bookingRealm.setPrice(bookingsResponse.getGetBookings().get(i).getPayment().getTotal());
            bookingRealm.setPaidTotal(bookingsResponse.getGetBookings().get(i).getPayment().getPaid_total());
            bookingRealm.setCoupon(bookingsResponse.getGetBookings().get(i).getPayment().getCoupon());
            bookingRealm.setDiscount_type(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount_type());
            bookingRealm.setDiscount(bookingsResponse.getGetBookings().get(i).getPayment().getDiscount());
            bookingRealm.setProviderName(bookingsResponse.getGetBookings().get(i).getUser().getName());
            bookingRealm.setShortId(bookingsResponse.getGetBookings().get(i).getBooking_no());
            bookingRealm.setTimeSlot(bookingsResponse.getGetBookings().get(i).getTime_slot());
            bookingRealm.setRegistrationNumber(bookingsResponse.getGetBookings().get(i).getCar().getRegistration_no());
            bookingRealm.setmUserName(bookingsResponse.getGetBookings().get(i).getUser().getName());
            bookingRealm.setmUserNumber(bookingsResponse.getGetBookings().get(i).getUser().getContact_no());
            bookingRealm.setmUserId(bookingsResponse.getGetBookings().get(i).getUser().getId());
            bookingRealm.setStatus(status);
            if(bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().size()>0)
                bookingRealm.setCarImage(bookingsResponse.getGetBookings().get(i).getCar().getThumbnails().get(0).getFile_address());
            if(bookingsResponse.getGetBookings().get(i).getServices().size()>0) {
                RealmList<SelectedBookingDataRealm> realmListMedia = new RealmList<>();

                for (int j = 0; j < bookingsResponse.getGetBookings().get(i).getServices().size(); j++) {
                    SelectedBookingDataRealm selectedBookingDataRealm=new SelectedBookingDataRealm();
                    selectedBookingDataRealm.setPackageName(bookingsResponse.getGetBookings().get(i).getServices().get(j).getService());
                    selectedBookingDataRealm.setCost(bookingsResponse.getGetBookings().get(i).getServices().get(j).getCost());
                    realmListMedia.add(selectedBookingDataRealm);
                }

                bookingRealm.setSelectedBookingDataRealms(realmListMedia);
            }

            mRealm.beginTransaction();
            mRealm.copyToRealm(bookingRealm);
            mRealm.commitTransaction();
        }

        if(pageNo==0) {
            mBookingPendingAdapter = new BookingPendingAdapter(mRealmController.getBookings(status), true, getActivity(), this);
            mBookingList.setAdapter(mBookingPendingAdapter);
        }

    }

    @Override
    public void confirmBooking(int pos, String bookingId) {

    }

    @Override
    public void rejectBooking(int pos, String bookingId) {

    }

    @Override
    public void onDetailClick(String bookingID, String statusFromRealm) {
        BookingDetailFragment bookingDetailFragment=new BookingDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,bookingID);
        bookingDetailFragment.setArguments(bundle);
        bookingDetailFragment.show(getChildFragmentManager(),"BookingDetailFragment");
    }

    @Override
    public void onCallClick(String number) {
        try {
            if(number!=null) {
                if(number.length()>0) {
                    String phone = number;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        }
        catch (SecurityException e){

        }catch (Exception e){

        }
    }

    @Override
    public void onChatClick(String userId) {
        try {
//            Qiscus.buildChatWith(userId) //here we use email as userID. But you can make it whatever you want.
//                    .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                        @Override
//                        public void onSuccess(Intent intent) {
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            //do anything if error occurs
//                            throwable.printStackTrace();
//                            throwable.getLocalizedMessage();
//                        }
//                    });
        }catch (Exception e){

        }
    }

    @Override
    public void onRescheduleClick(String bookingID, String status) {

    }

    @Override
    public void completeBooking(int pos, String bookingId) {

    }

    @Override
    public void onAddressClick(String address, String convenience) {

    }

    @Override
    public void createLead(String name, String mobile, String email, String bookingId) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.Key_Business_Name,name);
        bundle.putString(Constant.Key_Mobile,mobile);
        bundle.putString(Constant.Key_Email,email);
        bundle.putString(Constant.Key_Source,"Booking: "+bookingId);
        ((AwsHomeActivity)getActivity()).addFragment(new LeadCreateFragment(),"LeadCreateFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void createJobCard(String userId,String bookingId) {
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        Bundle bundle=new Bundle();
        bundle.putBoolean(Constant.IS_BOOKING,true);
        bundle.putString(Constant.USER_ID,userId);
        bundle.putString(Constant.BOOKING_ID,bookingId);
        ((AwsHomeActivity)getActivity()).addFragment(new JobCardCarFragment(),"CarFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }
}
