package autroid.business.view.fragment.profile;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.ProfileOfferAdapter;
import autroid.business.model.response.ShowroomOfferResponse;
import autroid.business.presenter.ShowroomOfferPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowroomOffersFragment extends Fragment {


    @BindView(R.id.offers_list)
    RecyclerView recListOffers;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    ShowroomOfferPresenter mPresenter;

    String showroomID,showroomName;
    private ProfileOfferAdapter mProfileOfferAdapter;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    public ShowroomOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showroom_offers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        mPresenter=new ShowroomOfferPresenter(this,mMainLayout);

        showroomID=getArguments().getString(Constant.KEY_ID);
        showroomName=getArguments().getString(Constant.Key_Business_Name);

        mTitle.setText(showroomName);

        LinearLayoutManager llmOffers;
        llmOffers = new LinearLayoutManager(getActivity());
        llmOffers.setOrientation(LinearLayoutManager.VERTICAL);
        recListOffers.setLayoutManager(llmOffers);
        recListOffers.setHasFixedSize(true);

        mScrollListener = new EndlessScrollListener(llmOffers) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getAllOffers(showroomID,pageNo);

            }
        };

        recListOffers.addOnScrollListener(mScrollListener);

        mPresenter.getAllOffers(showroomID,pageNo);
    }

    public void onSuccess(ShowroomOfferResponse showroomOfferResponse,int page) {

        if(page==0) {
            mProfileOfferAdapter = new ProfileOfferAdapter(getActivity(), showroomOfferResponse.getOfferBES(), true);
            recListOffers.setAdapter(mProfileOfferAdapter);
        }
        else {
            mProfileOfferAdapter.mList.addAll(showroomOfferResponse.getOfferBES());
            mProfileOfferAdapter.notifyDataSetChanged();
        }
    }
}
