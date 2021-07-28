package autroid.business.view.fragment.search;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.CarAdapter;
import autroid.business.adapter.search.MainSearchBookingAdapter;
import autroid.business.adapter.search.MainSearchLeadsAdapter;
import autroid.business.interfaces.SearchUserDetailCallback;
import autroid.business.model.bean.BookingsBE;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.bean.LeadsBE;
import autroid.business.model.response.SearchDetailResponse;
import autroid.business.presenter.SearchDetailPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.carsales.UsedCarDetailFragment;
import autroid.business.view.fragment.jobcard.JobCardDetailFragment;
import autroid.business.view.fragment.leads.LeadDetailFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDetailFragment extends Fragment implements View.OnClickListener, SearchUserDetailCallback {

    SearchDetailPresenter mPresenter;
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;

    TextView name, contact, email, bookingDummyInfo,carDummyInfo;
    ImageView btnChat, btnCall;
    private SearchDetailResponse searchDetailResponse;
        RecyclerView bookingRecyclerView, leadsRecyclerView, carsRecyclerView;
    MainSearchBookingAdapter bookingAdapter;
    MainSearchLeadsAdapter leadsAdapter;
    CarAdapter carAdapter;
    ArrayList<BookingsBE> bookingsList;
    ArrayList<LeadsBE> leadsList;
    ArrayList<CarDetailBE> carList;

    Button mBooking,mLeads,mCars;
    LinearLayout mLlBooking,mLlLeads,mLlCars;

    public SearchDetailFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        bookingsList = new ArrayList<>();
        leadsList = new ArrayList<>();
        carList = new ArrayList<>();
        bookingAdapter = new MainSearchBookingAdapter(bookingsList,this);
        leadsAdapter = new MainSearchLeadsAdapter(leadsList, this);
        carAdapter = new CarAdapter(carList,this);

        mPresenter = new SearchDetailPresenter(this, mMainLayout);
        mPresenter.getSearchedUser(getArguments().getString(Constant.KEY_ID));

        name = view.findViewById(R.id.name);
        btnChat = view.findViewById(R.id.chat_btn);
        contact = view.findViewById(R.id.contact_no);
        email = view.findViewById(R.id.email);
        bookingRecyclerView = view.findViewById(R.id.booking_recycler);
        leadsRecyclerView = view.findViewById(R.id.leads_recycler);
        carsRecyclerView = view.findViewById(R.id.cars_recycler);
        bookingDummyInfo = view.findViewById(R.id.booking_dummy_info);
        carDummyInfo = view.findViewById(R.id.car_dummy_info);
        bookingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        leadsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookingRecyclerView.setAdapter(bookingAdapter);
        leadsRecyclerView.setAdapter(leadsAdapter);
        carsRecyclerView.setAdapter(carAdapter);
        btnChat.setOnClickListener(this);
        btnCall = view.findViewById(R.id.call_btn);
        btnCall.setOnClickListener(this);

        mBooking=view.findViewById(R.id.bookings);
        mLeads=view.findViewById(R.id.leads);
        mCars=view.findViewById(R.id.cars);

        mLlBooking=view.findViewById(R.id.ll_bookings);
        mLlLeads=view.findViewById(R.id.ll_leads);
        mLlCars=view.findViewById(R.id.ll_cars);

        mBooking.setOnClickListener(this);
        mLeads.setOnClickListener(this);
        mCars.setOnClickListener(this);
    }

    public void onSearchUserSuccess(SearchDetailResponse searchDetailResponse) {
        this.searchDetailResponse = searchDetailResponse;
        name.setText(searchDetailResponse.getGetSearchUser().getName()+" / "+searchDetailResponse.getGetSearchUser().getContact_no());
        //contact.setText(searchDetailResponse.getGetSearchUser().getContact_no());
        email.setText(searchDetailResponse.getGetSearchUser().getEmail());

        if (searchDetailResponse.getGetSearchUser().getCars()!=null && searchDetailResponse.getGetSearchUser().getCars().size() > 0) {
            carList.addAll(searchDetailResponse.getGetSearchUser().getCars());
            carAdapter.notifyDataSetChanged();
        }else carDummyInfo.setVisibility(View.VISIBLE);

        if (searchDetailResponse.getGetSearchUser().getBookings()!=null && searchDetailResponse.getGetSearchUser().getBookings().size() > 0) {
            bookingsList.addAll(searchDetailResponse.getGetSearchUser().getBookings());
            bookingAdapter.notifyDataSetChanged();
        }else bookingDummyInfo.setVisibility(View.VISIBLE);

        if (searchDetailResponse.getGetSearchUser().getLeads()!=null && searchDetailResponse.getGetSearchUser().getLeads().size() > 0) {
            leadsList.addAll(searchDetailResponse.getGetSearchUser().getLeads());
            leadsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_btn:
                if (searchDetailResponse != null) Utility.whatsAppChat(searchDetailResponse.getGetSearchUser().getContact_no(),getActivity());
                break;
            case R.id.call_btn:
                if (searchDetailResponse != null)Utility.onCallClick(searchDetailResponse.getGetSearchUser().getContact_no(),getActivity());
                break;
            case R.id.bookings:
                mLlLeads.setVisibility(View.GONE);
                mLlCars.setVisibility(View.GONE);
                mLlBooking.setVisibility(View.VISIBLE);

                mBooking.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mCars.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mLeads.setBackgroundColor(getResources().getColor(R.color.matt_black));

                break;
            case R.id.leads:
                mLlLeads.setVisibility(View.VISIBLE);
                mLlCars.setVisibility(View.GONE);
                mLlBooking.setVisibility(View.GONE);

                mBooking.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mCars.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mLeads.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                break;
            case R.id.cars:
                mLlLeads.setVisibility(View.GONE);
                mLlCars.setVisibility(View.VISIBLE);
                mLlBooking.setVisibility(View.GONE);

                mBooking.setBackgroundColor(getResources().getColor(R.color.matt_black));
                mCars.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                mLeads.setBackgroundColor(getResources().getColor(R.color.matt_black));
                break;
        }

    }



    @Override
    public void onBookingClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onCarClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        bundle.putBoolean(Constant.KEY_IS_VIEW,true);
        ((AwsHomeActivity)getActivity()).addFragment(new UsedCarDetailFragment(),"UsedCarDetailFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }


    @Override
    public void onLeadClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        bundle.putString(Constant.VALUE,"MainSearch");
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity)getActivity()).addFragment(new LeadDetailFragment(),"LeadDetailFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);

    }
}
