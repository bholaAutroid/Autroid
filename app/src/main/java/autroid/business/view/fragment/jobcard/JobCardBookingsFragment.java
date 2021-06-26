package autroid.business.view.fragment.jobcard;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import autroid.business.R;
import autroid.business.adapter.jobcard.JobCardBookingAdapter;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.BookingDetailFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobCardBookingsFragment extends Fragment implements BookingStatusCallback, View.OnClickListener {

    RecyclerView mBookingList;
    Button mSkip;

    RealmController mRealmController;
    Realm mRealm;
    private JobCardBookingAdapter mJobCardBookingAdapter;

    public JobCardBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_card_bookings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Utility.hideSoftKeyboard(getActivity());

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController = RealmController.getInstance();

        mBookingList = view.findViewById(R.id.booking_list);
        mSkip=view.findViewById(R.id.skip);
        mSkip.setOnClickListener(this);

        LinearLayoutManager llPending;
        llPending = new LinearLayoutManager(getActivity());
        llPending.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llPending);

        mJobCardBookingAdapter = new JobCardBookingAdapter(mRealmController.getBookings("Jobcard"), true, getActivity(), this);
        mBookingList.setAdapter(mJobCardBookingAdapter);
    }

    @Override
    public void confirmBooking(int pos, String bookingId) {

    }

    @Override
    public void rejectBooking(int pos, String bookingId) {

    }

    @Override
    public void onDetailClick(String bookingId, String statusFromRealm) {
        BookingDetailFragment bookingDetailFragment=new BookingDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,bookingId);
        bookingDetailFragment.setArguments(bundle);
        bookingDetailFragment.show(getChildFragmentManager(),"BookingDetailFragment");
    }

    @Override
    public void onCallClick(String number) {

    }

    @Override
    public void onChatClick(String userId) {

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

    }

    @Override
    public void createJobCard(String userId, String bookingId) {
        Bundle bun = new Bundle();
        bun.putBoolean(Constant.IS_BOOKING, true);
        bun.putString(Constant.USER_ID, userId);
        bun.putString(Constant.BOOKING_ID, bookingId);
        ((HomeScreen) getActivity()).addFragment(new JobCardCarFragment(), "CarFragment", true, false, bun, ((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.skip:
                Bundle bundle=getArguments();
                ((HomeScreen) getActivity()).addFragment(new JobCardCarFragment(), "JobCardCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                break;
        }
    }
}
