package autroid.business.view.fragment.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.SearchUserAdapter;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.response.UserSearchResponse;
import autroid.business.presenter.MyReferralsPresenter;

public class MyReferralsFragment extends Fragment implements MainSearchListener {

    SearchUserAdapter userSearchAdapter;
    RecyclerView recyclerView;
    MyReferralsPresenter mPresenter;

    @BindView(R.id.main_layout)
    ConstraintLayout mMainlayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;


    public static MyReferralsFragment newInstance() {
        return new MyReferralsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_referrals_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        recyclerView = view.findViewById(R.id.base_recycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPresenter=new MyReferralsPresenter(this,mMainlayout);
        mPresenter.getAnyData();

        mTitle.setText(getString(R.string.my_referrals));

    }

    public void onSuccessAnyData(UserSearchResponse body) {
        userSearchAdapter=new SearchUserAdapter(getActivity(),body.getGetSearchUser());
        recyclerView.setAdapter(userSearchAdapter);
    }

    @Override
    public void onClickSearchItem(String id, String status, Bundle data) {

    }
}
