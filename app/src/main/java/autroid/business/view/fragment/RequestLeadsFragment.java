package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import autroid.business.R;
import autroid.business.adapter.RequestLeadsAdapter;
import autroid.business.model.response.RequestLeadResponse;
import autroid.business.presenter.RequestLeadPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestLeadsFragment extends Fragment {

    RecyclerView mLeads;
    FrameLayout mMainLayout;
    RequestLeadPresenter mPresenter;
    RequestLeadsAdapter mRequestLeadsAdapter;

    public static RequestLeadsFragment newInstance() {
        RequestLeadsFragment fragment = new RequestLeadsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_leads, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLeads= (RecyclerView) view.findViewById(R.id.leads_list);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mLeads.setLayoutManager(llm);

        mMainLayout= (FrameLayout) view.findViewById(R.id.main_layout);
        mPresenter=new RequestLeadPresenter(this,mMainLayout);

        mPresenter.getAllLeads();

    }

    public void onItemSuccess(RequestLeadResponse mRequestLeadResponse){

        if(mRequestLeadResponse.getResponseData().size()>0){
            mRequestLeadsAdapter=new RequestLeadsAdapter(mRequestLeadResponse.getResponseData());
            mLeads.setAdapter(mRequestLeadsAdapter);
        }
    }
}
