package autroid.business.view.fragment.leads;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import autroid.business.R;
import autroid.business.adapter.leads.LeadsAdapter;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.bean.LeadsBE;
import autroid.business.model.realm.LeadsRealm;
import autroid.business.model.response.LeadsResponse;
import autroid.business.presenter.LeadsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

public class LeadsListFragment extends Fragment implements LeadsCallback,View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,DatePickerDialog.OnDateSetListener {

    int pageNo=0,filterByPriority=0,currentDay, currentMonth, currentYear;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    String status="",filterByDate="",filterByStatus="";

    TextView title,date,resetFilter;

    RecyclerView recyclerView;

    RelativeLayout mainLayout;

    FloatingActionButton addButton;

    LeadsPresenter leadsPresenter;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;

    HorizontalScrollView filterScroller;

    AppCompatSpinner statusSpinner,prioritySpinner;

    boolean isFilterApplied;

    public LeadsListFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        setUpDateData();

        getBundleData();

        leadsPresenter=new LeadsPresenter(this,mainLayout);

        getData();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController=new RealmController(getActivity().getApplication());

        realmController.clearSpecificLeads(status);

        addButton.setOnClickListener(this);

        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);

        mScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                getData();
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    filterByStatus=statusSpinner.getSelectedItem().toString().trim();
                    realmController.clearSpecificLeads(status);
                    isFilterApplied=true;
                    leadsPresenter.getLeads(0,filterByStatus,filterByPriority,filterByDate,isFilterApplied);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0){
                    filterByPriority=position;
                    realmController.clearSpecificLeads(status);
                    isFilterApplied=true;
                    leadsPresenter.getLeads(0,filterByStatus,filterByPriority,filterByDate,isFilterApplied);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date.setOnClickListener(v->datePickerDialog.show());

        resetFilter.setOnClickListener(v->{
            if(isFilterApplied) {
                realmController.clearSpecificLeads(status);
                isFilterApplied = false;
                filterByStatus = Constant.ALL;
                filterByPriority = 0;
                filterByDate = "";
                statusSpinner.setSelection(0);
                prioritySpinner.setSelection(0);
                date.setText("Date");
                leadsPresenter.getLeads(0, filterByStatus, filterByPriority,filterByDate, isFilterApplied);
            }
        });
    }

    private void findViews(View view){
        title=view.findViewById(R.id.title);
        mainLayout=view.findViewById(R.id.main_layout);
        recyclerView=view.findViewById(R.id.estimate_list);
        addButton=view.findViewById(R.id.fab_add);
        addButton.hide();
        filterScroller=view.findViewById(R.id.horizontal_scroll);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        date=view.findViewById(R.id.date);
        statusSpinner=view.findViewById(R.id.status);
        prioritySpinner=view.findViewById(R.id.priority);
        resetFilter=view.findViewById(R.id.filter_reset);
    }

    private void setUpDateData() {
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
    }

    private void getBundleData() {
        status=getArguments().getString(Constant.KEY_TYPE);
        if(status.equals(Constant.OPEN))filterByStatus=status;
        else filterByStatus=Constant.ALL;
        title.setText(status+" Leads");
    }

    public void onSuccess(LeadsResponse baseResponse) {

        swipeRefreshLayout.setRefreshing(false);

        title.setText(status+" Leads"+"("+baseResponse.getTotalResponse().getTotalResult()+")");

        if (status.equals(Constant.OPEN))addButton.show();
        else if(status.equals(Constant.FOLLOW_UP))filterScroller.setVisibility(View.VISIBLE);

        for(LeadsBE data:baseResponse.getLeadsBES()){

            LeadsRealm leadsRealm=new LeadsRealm();
            leadsRealm.setId(data.getId());
            leadsRealm.setName(data.getName());
            leadsRealm.setContactNo(data.getContact_no());
            leadsRealm.setEmail(data.getEmail());
            leadsRealm.setUpdatedAt(data.getUpdated_at());
            leadsRealm.setCreatedAt(data.getCreated_at());
            leadsRealm.setSource(data.getSource());
            leadsRealm.setPrimaryStatus(status);//primaryStatus
            leadsRealm.setUserId(data.getUser());

            if(data.getRemark()!=null) {
                leadsRealm.setStatus(data.getRemark().getStatus());//secondaryStatus
                leadsRealm.setAssignee_remark(data.getRemark().getAssignee_remark());
            }

            if(data.getFollow_up().getDate()==null) leadsRealm.setFollow_up_date("");
            else leadsRealm.setFollow_up_date(data.getFollow_up().getDate().substring(0,10));

            realm.beginTransaction();
            realm.copyToRealm(leadsRealm);
            realm.commitTransaction();

        }

        if(pageNo==0) {
            LeadsAdapter leadsAdapter = new LeadsAdapter(realmController.getLeads(status), true, this);
            recyclerView.setAdapter(leadsAdapter);
        }

    }

    @Override
    public void onStatusClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        bundle.putString(Constant.VALUE,"LeadList");
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity)getActivity()).addFragment(new LeadDetailFragment(),"LeadDetailFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onImportantClick(String id) {
    }

    @Override
    public void onChatClick(String id) {
    }

    @Override
    public void onCallClick(String number) {
    }

    @Override
    public void onConvertLead(String name, String email, String contact, String source) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                ((AwsHomeActivity)getActivity()).disableFabButton(addButton);
                new LeadCreateFragment().show(getFragmentManager(),"LeadCreateFragment");
                break;
        }
    }

    private void getData(){
        leadsPresenter.getLeads(pageNo,filterByStatus,filterByPriority,filterByDate,isFilterApplied);
    }

    @Override
    public void onRefresh() {
        realmController.clearSpecificLeads(status);
        pageNo=0;
        getData();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date sourceDate;
        String targetdatevalue = null;
        try {
            sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetdatevalue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(targetdatevalue);

        filterByDate=date.getText().toString().trim();
        isFilterApplied=true;
        realmController.clearSpecificLeads(status);
        leadsPresenter.getLeads(0,filterByStatus,filterByPriority,filterByDate,isFilterApplied);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }
}
