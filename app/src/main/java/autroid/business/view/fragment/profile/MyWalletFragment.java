package autroid.business.view.fragment.profile;


import android.app.Application;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.aws.AwsHomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.cars.TransactionAdapter;
import autroid.business.model.response.CarEagerCoinsResponse;
import autroid.business.presenter.CarEagerCoinsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWalletFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.ll_transactions)
    RelativeLayout mBtnTransactions;


    @BindView(R.id.line_transactions)
    View lineTransaction;

    @BindView(R.id.total_earned)
    TextView mTotalCoins;
    @BindView(R.id.available_coins)
    TextView mAvailableCoins;
    @BindView(R.id.referral_txt)
    TextView mName;

    @BindView(R.id.user_image)
    ImageView mImage;

    @BindView(R.id.default_wallet)
    TextView mDefault;


    @BindView(R.id.layout_transaction)
    RelativeLayout mLayoutTransactions;

    @BindView(R.id.transaction_list)
    ExpandableListView mList;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.ll_total_referrals)
    Button mllReferral;

    @BindView(R.id.share_refer_code)
    LinearLayout shareReferralCode;
    @BindView(R.id.ll_share)
    LinearLayout mLLShare;

    //ll_total_referrals

    CarEagerCoinsPresenter mPresenter;

    TransactionAdapter mTransactionAdapter;
    private Realm realm;
    RealmController realmController;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private String content="CarEager, world-class services & certified used cars at the lowest prices. Use my referral code ";

    private String link="https://careager.page.link/u9DC";

    public MyWalletFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_eager_cash, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);
/*
        UserRealm userRealm=realmController.getUserLoggedDetail(true);
        if(userRealm!=null){

            Picasso.with(getActivity()).load(userRealm.getAvatar()).placeholder(R.drawable.ic_avatar_placeholder).into(mImage);
        }*/


        shareReferralCode.setOnClickListener(this);
        mLLShare.setOnClickListener(this);

        mllReferral.setOnClickListener(this);
       // mBtnTransactions.setOnClickListener(this);

        mPresenter=new CarEagerCoinsPresenter(this,mMainLayout);
        mPresenter.getTransactions();


        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                mPresenter.getTransactions();
            }
        });

        mSwipeRefreshLayout.setEnabled(false);
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (mList == null || mList.getChildCount() == 0) ? 0 : mList  .getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


       /* mList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                setListViewHeight(parent, groupPosition);
                return false;
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_total_referrals:
                ((AwsHomeActivity) getActivity()).addFragment(new MyReferralsFragment(), "MyReferralsFragment", true, false, null, ((AwsHomeActivity) getActivity()).currentFrameId);
                /*CoinPopupFragment coinPopupFragment=new CoinPopupFragment();
                coinPopupFragment.show(getChildFragmentManager(),"GoogleSearchFragment");*/
                break;
                case R.id.share_refer_code:
                Utility.share(getActivity(),content+mName.getText().toString()+" to earn 500 in your CarEager Wallet - \n+"+link);
                break;
            case R.id.ll_share:
                Utility.share(getActivity(),content+mName.getText().toString()+" to earn 500 in your CarEager Wallet - \n"+link);
                break;
        }
    }

    public void carEagerCoinsSuccess(CarEagerCoinsResponse carEagerCoinsResponse) {

            mSwipeRefreshLayout.setRefreshing(false);
            mTotalCoins.setText("₹"+carEagerCoinsResponse.getGetCoins().getTotal());
            mAvailableCoins.setText("₹"+carEagerCoinsResponse.getGetCoins().getUnused());
            mName.setText(carEagerCoinsResponse.getGetCoins().getReferral_code());

            if(carEagerCoinsResponse.getGetCoins().getList().size()>0) {
                mDefault.setVisibility(View.GONE);
                mTransactionAdapter = new TransactionAdapter(getActivity(), carEagerCoinsResponse.getGetCoins().getList());
                mList.setAdapter(mTransactionAdapter);
                mList.expandGroup(0);
            }
            else {
                mDefault.setVisibility(View.VISIBLE);
            }
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }
}
