package autroid.business.view.fragment.search;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
//import com.qiscus.sdk.Qiscus;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.search.MainSearchBookingAdapter;
import autroid.business.adapter.search.MainSearchLeadsAdapter;
import autroid.business.adapter.search.MainSearchUserAdapter;
import autroid.business.adapter.orders.OrdersAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.interfaces.OrdersListener;
import autroid.business.model.bean.BookingsBE;
import autroid.business.model.bean.LeadsBE;
import autroid.business.model.bean.OrderDataBE;
import autroid.business.model.bean.UserBE;
import autroid.business.model.response.MultipleDataResponse;
import autroid.business.presenter.MainSearchPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.profile.ShowroomFragment;
import autroid.business.view.fragment.leads.LeadDetailFragment;
import autroid.business.view.fragment.orders.OrderDetailsFragment;

public class MainSearchFragment extends Fragment implements OnClickBusinessCallback,View.OnClickListener,LeadsCallback, MainSearchListener, OrdersListener {

    static final int USER=1;
    static final int BOOKING=2;
    static final int ORDER=3;
    static final int LEAD=4;

    int currentSelection=1;

    EditText editText;

    Button user,booking,order,leads;

    TextView empty;

    MainSearchPresenter searchPresenter;
    RelativeLayout mMainLayout;
    RecyclerView recyclerView;

    MainSearchUserAdapter userSearchAdapter;
    MainSearchBookingAdapter bookingSearchAdapter;
    OrdersAdapter ordersAdapter;
    MainSearchLeadsAdapter leadsSearchAdapter;

    FirebaseAnalytics mFirebaseAnalytics;

    ArrayList<BookingsBE> bookingsList;
    ArrayList<UserBE> usersList;
    ArrayList<OrderDataBE> orderList;
    ArrayList<LeadsBE> leadsList;

    public MainSearchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        editText = view.findViewById(R.id.search_txt);
        mMainLayout= view.findViewById(R.id.main_layout);
        user=view.findViewById(R.id.user);
        booking=view.findViewById(R.id.booking);
        order=view.findViewById(R.id.order);
        leads=view.findViewById(R.id.lead);
        empty=view.findViewById(R.id.empty_text);
        recyclerView = view.findViewById(R.id.base_recycler);

        usersList=new ArrayList<>();
        bookingsList=new ArrayList<>();
        orderList=new ArrayList<>();
        leadsList=new ArrayList<>();

        searchPresenter =new MainSearchPresenter(this,mMainLayout);

        editText.requestFocus();
        Utility.showSoftKeyboard(getActivity(),editText);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Search",null);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        userSearchAdapter=new MainSearchUserAdapter(usersList,this);
      //  bookingSearchAdapter=new MainSearchBookingAdapter(bookingsList,this);
        //ordersAdapter=new OrdersAdapter(orderList,this,"searchOrder"); //apply realm here
      //  leadsSearchAdapter=new MainSearchLeadsAdapter(leadsList,this);


        user.setOnClickListener(v->{
            currentSelection=1;
            v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            booking.setBackgroundColor(getResources().getColor(R.color.matt_black));
            order.setBackgroundColor(getResources().getColor(R.color.matt_black));
            leads.setBackgroundColor(getResources().getColor(R.color.matt_black));
            if(!editText.getText().toString().trim().equals(""))searchPresenter.getAnyData(editText.getText().toString().trim(),"user");
        });

        booking.setOnClickListener(v->{
            currentSelection=2;
            v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            user.setBackgroundColor(getResources().getColor(R.color.matt_black));
            order.setBackgroundColor(getResources().getColor(R.color.matt_black));
            leads.setBackgroundColor(getResources().getColor(R.color.matt_black));
            if(!editText.getText().toString().trim().equals(""))searchPresenter.getAnyData(editText.getText().toString().trim(),"booking");
        });

        order.setOnClickListener(v->{
            currentSelection=3;
            v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            user.setBackgroundColor(getResources().getColor(R.color.matt_black));
            booking.setBackgroundColor(getResources().getColor(R.color.matt_black));
            leads.setBackgroundColor(getResources().getColor(R.color.matt_black));
            if(!editText.getText().toString().trim().equals(""))searchPresenter.getAnyData(editText.getText().toString().trim(),"order");
        });


        leads.setOnClickListener(v->{
            currentSelection=4;
            v.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            user.setBackgroundColor(getResources().getColor(R.color.matt_black));
            booking.setBackgroundColor(getResources().getColor(R.color.matt_black));
            order.setBackgroundColor(getResources().getColor(R.color.matt_black));
            if(!editText.getText().toString().trim().equals(""))searchPresenter.getAnyData(editText.getText().toString().trim(),"lead");
        });

        editText.setOnEditorActionListener((textView,actionId,keyEvent)->{
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Utility.hideSoftKeyboard(getActivity());
                    if(currentSelection==USER) searchPresenter.getAnyData(editText.getText().toString().trim(),"user");
                    else if(currentSelection==BOOKING)searchPresenter.getAnyData(editText.getText().toString().trim(),"booking");
                    else if(currentSelection==ORDER)searchPresenter.getAnyData(editText.getText().toString().trim(),"order");
                    else if(currentSelection==LEAD)searchPresenter.getAnyData(editText.getText().toString().trim(),"lead");
                    return true;
                }
                return false;
        });


    }

    public void onSuccessAnyData(MultipleDataResponse multipleDataResponse){
        switch (currentSelection){
            case USER:
                usersList.clear();
                if(multipleDataResponse.getMultipleDataBE().getUsersList()!=null)usersList.addAll(multipleDataResponse.getMultipleDataBE().getUsersList());
                userSearchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(null);
                if(usersList.size()!=0){
                    empty.setVisibility(View.GONE);
                    recyclerView.setAdapter(userSearchAdapter);
                } else empty.setVisibility(View.VISIBLE);
                break;

            case BOOKING:
                bookingsList.clear();
               if(multipleDataResponse.getMultipleDataBE().getBookingsList()!=null) bookingsList.addAll(multipleDataResponse.getMultipleDataBE().getBookingsList());
                bookingSearchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(null);
                if(bookingsList.size()!=0){
                    empty.setVisibility(View.GONE);
                    recyclerView.setAdapter(bookingSearchAdapter);
                } else empty.setVisibility(View.VISIBLE);
                break;

            case ORDER:
                orderList.clear();
                if(multipleDataResponse.getMultipleDataBE().getOrderList()!=null)orderList.addAll(multipleDataResponse.getMultipleDataBE().getOrderList());
                ordersAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(null);
                if(orderList.size()!=0){
                    empty.setVisibility(View.GONE);
                    recyclerView.setAdapter(ordersAdapter);
                } else empty.setVisibility(View.VISIBLE);
                break;

            case LEAD:
                leadsList.clear();
                if(multipleDataResponse.getMultipleDataBE().getLeadsList()!=null)leadsList.addAll(multipleDataResponse.getMultipleDataBE().getLeadsList());
                leadsSearchAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(null);
                if(leadsList.size()!=0){
                    empty.setVisibility(View.GONE);
                    recyclerView.setAdapter(leadsSearchAdapter);
                } else empty.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onBusinessClick(String id) {

        Bundle params = new Bundle();
        params.putString("business_id", id);
        mFirebaseAnalytics.logEvent("search", params);

        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((HomeScreen)getActivity()).makeDrawerVisible();
        ((HomeScreen) getActivity()).addFragment(new ShowroomFragment(), "MainSearchFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public void onStatusClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((HomeScreen) getActivity()).addFragment(new SearchDetailFragment(), "SearchDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onImportantClick(String id) {

    }

    @Override
    public void onChatClick(String id) {
        try {
//            Qiscus.buildChatWith(id) //here we use email as userID. But you can make it whatever you want.
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
    public void onCardClick(String orderId) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,orderId);
        bundle.putBoolean(Constant.IS_BOOKING_DETAILS,false);
        ((HomeScreen)getActivity()).addFragment(new OrderDetailsFragment(),"DetailsFragment",true,false,bundle,(((HomeScreen) getActivity()).currentFrameId));

    }

    @Override
    public void onConvertLead(String name, String email, String contact, String source) {

    }

    @Override
    public void onClickSearchItem(String id, String status,Bundle data) {

        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);

        if(currentSelection==USER){
            ((HomeScreen) getActivity()).addFragment(new SearchDetailFragment(), "SearchDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
        }else if(currentSelection==BOOKING){
            bundle.putString(Constant.KEY_TYPE,status);
            bundle.putBoolean(Constant.IS_MAIN_SEARCH,true);
            ((HomeScreen) getActivity()).addFragment(new MainSearchBookingDetailsFragment(), "BookingDetailsFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
        } else if(currentSelection==LEAD){
            bundle.putString(Constant.VALUE,"MainSearch");
            bundle.putBundle(Constant.BUNDLE_DATA,data);
            ((HomeScreen)getActivity()).addFragment(new LeadDetailFragment(),"LeadDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
        }
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_REFRESH_LEAD)refreshData(currentSelection);
        else if(intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_REFRESH_BOOKING)refreshData(currentSelection);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }


    void refreshData(int currentSelection){
        if(currentSelection==LEAD)searchPresenter.getAnyData(editText.getText().toString().trim(),"lead");
        else if(currentSelection==BOOKING)searchPresenter.getAnyData(editText.getText().toString().trim(),"booking");
    }
}
