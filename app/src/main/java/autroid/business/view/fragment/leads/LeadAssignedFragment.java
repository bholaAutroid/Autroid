package autroid.business.view.fragment.leads;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.qiscus.sdk.Qiscus;

import autroid.business.R;
import autroid.business.adapter.leads.LeadsAssignedAdapter;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.realm.LeadsAssignedRealm;
import autroid.business.model.response.LeadsResponse;
import autroid.business.presenter.LeadAssignedPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.view.activity.HomeScreen;
import io.realm.Realm;

public class LeadAssignedFragment extends Fragment implements LeadsCallback, SwipeRefreshLayout.OnRefreshListener{

    int pageNo=0;

    String status="";

    TextView title;

    RecyclerView recyclerView;

    RelativeLayout mainLayout;

    FloatingActionButton addButton;

    LeadAssignedPresenter leadAssignedPresenter;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;

    HorizontalScrollView filterScroller;

    public LeadAssignedFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        getBundleData();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController=new RealmController(getActivity().getApplication());

        realmController.clearAssignedLeads(status);

        recyclerView.setLayoutManager(layoutManager);

        leadAssignedPresenter=new LeadAssignedPresenter(this,mainLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        getData();

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
    }

    private void getBundleData() {
        status=getArguments().getString(Constant.KEY_TYPE);
        title.setText("ASSIGNED LEADS");
    }

    private void findViews(View view){

        title=view.findViewById(R.id.title);

        mainLayout=view.findViewById(R.id.main_layout);

        recyclerView=view.findViewById(R.id.estimate_list);

        addButton=view.findViewById(R.id.fab_add);

        addButton.hide();

        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
    }

    public void onSuccess(LeadsResponse baseResponse) {

        swipeRefreshLayout.setRefreshing(false);

        title.setText("ASSIGNED LEADS"+"("+baseResponse.getTotalResponse().getTotalResult()+")");

        for(int i=0;i<baseResponse.getLeadsBES().size();i++){
            LeadsAssignedRealm leadsAssignedRealm=new LeadsAssignedRealm();
            leadsAssignedRealm.setUpdatedAt(baseResponse.getLeadsBES().get(i).getUpdated_at());
            leadsAssignedRealm.setCreatedAt(baseResponse.getLeadsBES().get(i).getCreated_at());
            leadsAssignedRealm.setSource(baseResponse.getLeadsBES().get(i).getSource());
            leadsAssignedRealm.setId(baseResponse.getLeadsBES().get(i).getId());
            leadsAssignedRealm.setPrimaryStatus(status);//primaryStatus
            leadsAssignedRealm.setUserId(baseResponse.getLeadsBES().get(i).getUser());
            leadsAssignedRealm.setType(baseResponse.getLeadsBES().get(i).getType());
            if(baseResponse.getLeadsBES().get(i).getRemark()!=null) {
                leadsAssignedRealm.setStatus(baseResponse.getLeadsBES().get(i).getRemark().getStatus());//secondaryStatus
                leadsAssignedRealm.setAssignee_remark(baseResponse.getLeadsBES().get(i).getRemark().getAssignee_remark());
            }
            leadsAssignedRealm.setName(baseResponse.getLeadsBES().get(i).getName());
            leadsAssignedRealm.setContactNo(baseResponse.getLeadsBES().get(i).getContact_no());
            leadsAssignedRealm.setEmail(baseResponse.getLeadsBES().get(i).getEmail());
            if(baseResponse.getLeadsBES().get(i).getFollow_up().getDate()==null) leadsAssignedRealm.setFollow_up_date("");
            else leadsAssignedRealm.setFollow_up_date(baseResponse.getLeadsBES().get(i).getFollow_up().getDate().substring(0,10));

            realm.beginTransaction();
            realm.copyToRealm(leadsAssignedRealm);
            realm.commitTransaction();

        }

        if(pageNo==0) {
            LeadsAssignedAdapter leadsAssignedAdapter = new LeadsAssignedAdapter(realmController.getAssignedLeads(), true, this);
            recyclerView.setAdapter(leadsAssignedAdapter);
        }

    }

    @Override
    public void onStatusClick(String id) {
        ((HomeScreen)getActivity()).makeDrawerVisible();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        bundle.putString(Constant.KEY_TYPE,status);
        bundle.putString(Constant.VALUE,"LeadAssigned");
        ((HomeScreen)getActivity()).addFragment(new LeadDetailFragment(),"LeadDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onImportantClick(String id) {
    }

    @Override
    public void onChatClick(String id) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        try {
//            Qiscus.buildChatWith(id) //here we use email as userID. But you can make it whatever you want.
//                    .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                        @Override
//                        public void onSuccess(Intent intent) {
//                            progressDialog.dismiss();
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            //do anything if error occurs
//                            progressDialog.dismiss();
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
    public void onConvertLead(String name, String email, String contact, String source) {

    }

    private void getData(){
        leadAssignedPresenter.getAssignedLeads(pageNo);
    }


    @Override
    public void onRefresh() {
        realmController.clearAssignedLeads(status);
        pageNo=0;
        getData();
    }

}
