package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.SearchBusinessAdapter;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.response.SavedBusinessResponse;
import autroid.business.presenter.SavedBusinessesPresenter;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.profile.ShowroomFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedBusinessesFragment extends Fragment implements OnClickBusinessCallback {

    LinearLayout mMainLayout;
    RecyclerView recList;
    @BindView(R.id.toolbar_title)
    TextView mName;

    SearchBusinessAdapter mSearchBusinessAdapter;
    SavedBusinessesPresenter mPresenter;

    public SavedBusinessesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_businesses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        mMainLayout= view.findViewById(R.id.main_layout);
        mPresenter=new SavedBusinessesPresenter(this,mMainLayout);

        recList= (RecyclerView) view.findViewById(R.id.showroom_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mName.setText(getString(R.string.drawer_businesses));

        mPresenter.getSearchedBusiness();
    }

    public void onSuccess(SavedBusinessResponse searchBusinessResponse) {
        mSearchBusinessAdapter=new SearchBusinessAdapter(searchBusinessResponse.getBusiness(),getActivity(),this);
        recList.setAdapter(mSearchBusinessAdapter);
    }

    @Override
    public void onBusinessClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new ShowroomFragment(), "MainSearchFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }
}
