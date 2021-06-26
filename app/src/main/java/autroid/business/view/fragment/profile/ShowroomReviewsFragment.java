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
import autroid.business.adapter.ProfileReviewRatingAdapter;
import autroid.business.model.response.ShowroomReviewResponse;
import autroid.business.presenter.ShowroomReviewsPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowroomReviewsFragment extends Fragment {

    @BindView(R.id.reviews_list)
    RecyclerView recListReviews;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    ShowroomReviewsPresenter mPresenter;

    String showroomID,showroomName;
    private ProfileReviewRatingAdapter mProfileReviewRatingAdapter;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    public ShowroomReviewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showroom_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        mPresenter=new ShowroomReviewsPresenter(this,mMainLayout);

        showroomID=getArguments().getString(Constant.KEY_ID);
        showroomName=getArguments().getString(Constant.Key_Business_Name);

        mTitle.setText(showroomName);

        LinearLayoutManager llmReviews;
        llmReviews = new LinearLayoutManager(getActivity());
        llmReviews.setOrientation(LinearLayoutManager.VERTICAL);
        recListReviews.setLayoutManager(llmReviews);
        recListReviews.setHasFixedSize(true);

        mScrollListener = new EndlessScrollListener(llmReviews) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getAllReviews(showroomID,pageNo);

            }
        };

        recListReviews.addOnScrollListener(mScrollListener);

        mPresenter.getAllReviews(showroomID,pageNo);
    }

    public void onSuccess(ShowroomReviewResponse showroomReviewResponse,int page) {

        if(page==0) {
            mProfileReviewRatingAdapter = new ProfileReviewRatingAdapter(getActivity(), showroomReviewResponse.getRatingReviewBES(), true);
            recListReviews.setAdapter(mProfileReviewRatingAdapter);
        }
        else {
            mProfileReviewRatingAdapter.mList.addAll(showroomReviewResponse.getRatingReviewBES());
            mProfileReviewRatingAdapter.notifyDataSetChanged();
        }

    }
}
