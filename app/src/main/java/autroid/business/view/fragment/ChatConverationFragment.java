package autroid.business.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
//import com.qiscus.sdk.chat.core.data.remote.QiscusApi;
//import com.qiscus.sdk.chat.core.util.QiscusRxExecutor;

import java.util.List;
import autroid.business.R;
import autroid.business.interfaces.OnChatItemCallback;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.view.activity.ChatActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatConverationFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
  //  private RecentConversationAdapter adapter;
    EndlessScrollListener mScrollListener = null;
    TextView mTitle;

    String outerUri=null;

    public ChatConverationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_converation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


        mTitle=view.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.recent_chat));

        Bundle bundle=getArguments();
        if(bundle!=null) {
            outerUri=bundle.getString(Constant.KEY_TYPE);
        }

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open navigation drawer when click navigation back button
               getActivity().onBackPressed();
            }
        });

//        adapter = new RecentConversationAdapter(getActivity(),this);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerRecentConversation);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page++;
//                launchChat(page);
            }
        };
        recyclerView.addOnScrollListener(mScrollListener);

//        launchChat(1);
    }

//    private void launchChat(int page) {
//        QiscusRxExecutor.execute(QiscusApi.getInstance().getChatRooms(page, 10, true), new QiscusRxExecutor
//                .Listener<List<QiscusChatRoom>>(){
//            @Override
//            public void onSuccess(List<QiscusChatRoom> chatRoomList) {
//                //success get chat room list
//                swipeRefreshLayout.setRefreshing(false);
//                adapter.addOrUpdate(chatRoomList);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                //error get chat room list
//            }
//        });
//
//    }

//    @Override
//    public void onRefresh() {
//        if(null!=adapter) {
//            adapter.clear();
//            launchChat(1);
//        }
//        else {
//            swipeRefreshLayout.setRefreshing(false);
//        }
//    }


}
