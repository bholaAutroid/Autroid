package autroid.business.view.fragment.leads;

import android.os.Bundle;
import androidx.annotation.NonNull;
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

import autroid.business.R;
import autroid.business.adapter.booking.LeadBookingAdapter;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.bean.BookingsBE;
import autroid.business.model.realm.LeadBookingRealm;
import autroid.business.model.response.BookingsResponse;
import autroid.business.presenter.LeadBookingPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.search.MainSearchBookingDetailsFragment;
import io.realm.Realm;

public class LeadBookingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BookingStatusCallback {

    int pageNo=0;

    RecyclerView recyclerView;

    RelativeLayout mainLayout;

    FloatingActionButton addButton;

    LeadBookingPresenter leadBookingPresenter;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView title;

    public LeadBookingFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController=new RealmController(getActivity().getApplication());

        realmController.clearAssignedLeads();

        title=view.findViewById(R.id.title);

        title.setText("Lead Booking");

        mainLayout=view.findViewById(R.id.main_layout);

        recyclerView=view.findViewById(R.id.estimate_list);

        addButton=view.findViewById(R.id.fab_add);

        addButton.hide();

        recyclerView.setLayoutManager(layoutManager);

        leadBookingPresenter = new LeadBookingPresenter(this,mainLayout);

        leadBookingPresenter.getData(pageNo);

        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        mScrollListener=new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNo=page;
                leadBookingPresenter.getData(page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

    }

    @Override
    public void onRefresh() {
        realmController.clearAssignedLeads();
        pageNo=0;
        leadBookingPresenter.getData(pageNo);
    }

    public void onSucessLeadBooking(BookingsResponse response) {

        swipeRefreshLayout.setRefreshing(false);

        title.setText("Lead Booking ("+response.getTotalResponse().getTotalResult()+")");

        for (BookingsBE data:response.getGetBookings()) {
            LeadBookingRealm leadBookingRealm=new LeadBookingRealm();
            leadBookingRealm.setId(data.getId());
            leadBookingRealm.setName(data.getUser().getName());
            leadBookingRealm.setContactNo(data.getUser().getContact_no());
            leadBookingRealm.setEmail(data.getUser().getEmail());
            leadBookingRealm.setStatus(data.getStatus());
            leadBookingRealm.setBookingDate(data.getDate());
            leadBookingRealm.setBookingTime(data.getTime_slot());
            leadBookingRealm.setCreatedAt(data.getCreated_at());
            leadBookingRealm.setUpdatedAt(data.getUpdated_at());
            leadBookingRealm.setConvenience(data.getConvenience());
            leadBookingRealm.setBookingNo(data.getBooking_no());
            leadBookingRealm.setCarName(data.getCar().getTitle());
            leadBookingRealm.setRegistrationNo(data.getCar().getRegistration_no());

            realm.beginTransaction();
            realm.copyToRealm(leadBookingRealm);
            realm.commitTransaction();
        }

        if(pageNo==0) {
            LeadBookingAdapter leadBookingAdapter = new LeadBookingAdapter(realmController.getLeadBookings(), true,this);
            recyclerView.setAdapter(leadBookingAdapter);
        }

    }

    @Override
    public void confirmBooking(int pos, String bookingId) {

    }

    @Override
    public void rejectBooking(int pos, String bookingId) {

    }

    @Override
    public void onDetailClick(String bookingId,String status) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,bookingId);
        bundle.putString(Constant.KEY_TYPE,status);
        bundle.putBoolean(Constant.IS_CRE,true);
        ((AwsHomeActivity)getActivity()).addFragment(new MainSearchBookingDetailsFragment(),"BookingDetailsFragment",true,false,bundle,((AwsHomeActivity)getActivity()).currentFrameId);
    }

    @Override
    public void onCallClick(String number) {
        Utility.onCallClick(number,getActivity());
    }

    @Override
    public void onChatClick(String userId) {
        Utility.onChatClick(userId,getActivity());
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

    }

}
