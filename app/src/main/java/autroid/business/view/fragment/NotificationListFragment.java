package autroid.business.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.NotificationListAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.response.NotificationsListResponse;
import autroid.business.presenter.NotificationListPresenter;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationListFragment extends Fragment {

    @BindView(R.id.notification_list)
    RecyclerView mList;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;
    NotificationListPresenter mPresenter;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    NotificationListAdapter mNotificationListAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    public NotificationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        LinearLayoutManager llm,llmPeople,llmPlace,llmTags;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);

        mTitle.setText("Notifications");

        mPresenter=new NotificationListPresenter(this,mMainLayout);
        mPresenter.getNotifications(pageNo);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);
                pageNo=0;
                mPresenter.getNotifications(pageNo);
            }
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getNotifications(pageNo);
            }
        };
        mList.addOnScrollListener(mScrollListener);

        PreferenceManager.getInstance().putIntegerPreference(getActivity(), Constant.SP_NOTIFICATION_COUNT, 0);
        Intent data = new Intent();
        data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_NOTIFICATION_COUNT);
        Events.SendEvent sendEvent = new Events.SendEvent(data);
        GlobalBus.getBus().post(sendEvent);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {

    }

    public void onSuccess(NotificationsListResponse notificationsListResponse) {

        mSwipeRefreshLayout.setRefreshing(false);

        if(pageNo==0) {
            mNotificationListAdapter = new NotificationListAdapter(getActivity(), notificationsListResponse.getGetNotificationsListResponse());
            mList.setAdapter(mNotificationListAdapter);
        }
        else {
            if(null!=mNotificationListAdapter) {
                mNotificationListAdapter.mList.addAll(notificationsListResponse.getGetNotificationsListResponse());
                mNotificationListAdapter.notifyDataSetChanged();
            }
        }
    }
}
