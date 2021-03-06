package autroid.business.view.fragment.leadgeneration;


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
import autroid.business.adapter.leadgeneration.ServiceDueAdapter;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.bean.ServiceDueBE;
import autroid.business.model.realm.ServiceDueRealm;
import autroid.business.model.response.ServiceDueResponse;
import autroid.business.presenter.ServiceReminderPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceReminderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainSearchListener{

        int pageNo = 0;

    RecyclerView recyclerView;

    RelativeLayout mainLayout;

    FloatingActionButton addButton;

    ServiceReminderPresenter insuranceDuePresenter;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView title;


    public ServiceReminderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_reminder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController = new RealmController(getActivity().getApplication());

        realmController.clearServiceDue();

        title = view.findViewById(R.id.title);

        title.setText("Service Due");

        mainLayout = view.findViewById(R.id.main_layout);

        recyclerView = view.findViewById(R.id.estimate_list);

        addButton = view.findViewById(R.id.fab_add);

        addButton.hide();

        recyclerView.setLayoutManager(layoutManager);

        insuranceDuePresenter = new ServiceReminderPresenter(this, mainLayout);

        insuranceDuePresenter.getService(pageNo,Boolean.TRUE);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        mScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNo = page;
                insuranceDuePresenter.getService(page,Boolean.FALSE);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

    }

    @Override
    public void onRefresh() {
        realmController.clearServiceDue();
        pageNo = 0;
        insuranceDuePresenter.getService(pageNo,Boolean.TRUE);
    }

    public void onSuccess(ServiceDueResponse response) {

        swipeRefreshLayout.setRefreshing(false);

        title.setText("Service Due (" + response.getTotalResponse().getTotalResult() + ")");

        for (ServiceDueBE data : response.getGetServiceData()) {

            ServiceDueRealm serviceDueRealm = new ServiceDueRealm();

            serviceDueRealm.setId(data.getId());
            serviceDueRealm.setTitle(data.getCar().getTitle());
            serviceDueRealm.setRegistrationNo(data.getCar().getRegistration_no());
            serviceDueRealm.setUserName(data.getUser().getName());
            serviceDueRealm.setContactNumber(data.getUser().getContact_no());
            serviceDueRealm.setEmail(data.getUser().getEmail());
            serviceDueRealm.setFollowupDate(data.getService_reminder());
            serviceDueRealm.setRemark(data.getRemarks().get(data.getRemarks().size()-1).getRemark());

         
            realm.beginTransaction();
            realm.copyToRealm(serviceDueRealm);
            realm.commitTransaction();
        }

        if (pageNo == 0) {
            ServiceDueAdapter insuranceDueAdapter = new ServiceDueAdapter(realmController.getServiceDue(), true, this);
            recyclerView.setAdapter(insuranceDueAdapter);
        }
    }

    @Override
    public void onClickSearchItem(String id, String status, Bundle data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putBoolean(Constant.KEY_TYPE, false);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new LeadGenerateDetailFragment(), "InsuranceDueDetail", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

}
