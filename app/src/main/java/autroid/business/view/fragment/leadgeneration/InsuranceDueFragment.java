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
import autroid.business.adapter.leadgeneration.InsuranceDueAdapter;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.bean.InsuranceDueBE;
import autroid.business.model.realm.InsuranceDueRealm;
import autroid.business.model.response.InsuranceDueResponse;
import autroid.business.presenter.leadgeneration.InsuranceDuePresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.view.activity.HomeScreen;
import io.realm.Realm;

public class InsuranceDueFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, MainSearchListener {

    int pageNo = 0;

    RecyclerView recyclerView;

    RelativeLayout mainLayout;

    FloatingActionButton addButton;

    InsuranceDuePresenter insuranceDuePresenter;

    Realm realm;

    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView title;

    public InsuranceDueFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leads, container, false); //same fragment as leads is used
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        this.realm = RealmController.with(getActivity()).getRealm();

        realmController = new RealmController(getActivity().getApplication());

        realmController.clearInsuranceDue();

        title = view.findViewById(R.id.title);

        title.setText("Insurance Due");

        mainLayout = view.findViewById(R.id.main_layout);

        recyclerView = view.findViewById(R.id.estimate_list);

        addButton = view.findViewById(R.id.fab_add);

        addButton.hide();

        recyclerView.setLayoutManager(layoutManager);

        insuranceDuePresenter = new InsuranceDuePresenter(this, mainLayout);

        insuranceDuePresenter.getInsuranceData(pageNo,Boolean.TRUE);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        mScrollListener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNo = page;
                insuranceDuePresenter.getInsuranceData(page,Boolean.FALSE);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

    }

    @Override
    public void onRefresh() {
        realmController.clearInsuranceDue();
        pageNo = 0;
        insuranceDuePresenter.getInsuranceData(pageNo,Boolean.TRUE);
    }

    public void onSuccess(InsuranceDueResponse response) {

        swipeRefreshLayout.setRefreshing(false);

        title.setText("Insurance Due (" + response.getTotalResponse().getTotalResult() + ")");

        for (InsuranceDueBE data : response.getGetInsuranceDue()) {

            InsuranceDueRealm insuranceDueRealm = new InsuranceDueRealm();

            insuranceDueRealm.setId(data.getId());
            insuranceDueRealm.setTitle(data.getTitle());
            insuranceDueRealm.setRegistrationNo(data.getRegistration_no());
            insuranceDueRealm.setUserName(data.getUser().getName());
            insuranceDueRealm.setContactNumber(data.getUser().getContact_no());
            insuranceDueRealm.setEmail(data.getUser().getEmail());
            insuranceDueRealm.setPolicyHolder(data.getInsuranceData().getPolicy_holder());
            insuranceDueRealm.setPolicyNumber(data.getInsuranceData().getPolicy_no());
            insuranceDueRealm.setInsuranceCompany(data.getInsuranceData().getInsurance_company());
            insuranceDueRealm.setExpire(data.getInsuranceData().getExpire());
            insuranceDueRealm.setPremium(Integer.toString(data.getInsuranceData().getPremium()));
            if (data.getInsuranceData().getExpire() != null) insuranceDueRealm.setExpire(data.getInsuranceData().getExpire());
            else insuranceDueRealm.setExpire("");

            realm.beginTransaction();
            realm.copyToRealm(insuranceDueRealm);
            realm.commitTransaction();
        }

        if (pageNo == 0) {
            InsuranceDueAdapter insuranceDueAdapter = new InsuranceDueAdapter(realmController.getInsuranceDueData(), true, this);
            recyclerView.setAdapter(insuranceDueAdapter);
        }
    }

    @Override
    public void onClickSearchItem(String id, String status, Bundle data) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putBoolean(Constant.KEY_TYPE, true);
        ((HomeScreen) getActivity()).makeDrawerVisible();
        ((HomeScreen) getActivity()).addFragment(new LeadGenerateDetailFragment(), "InsuranceDueDetail", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }
}