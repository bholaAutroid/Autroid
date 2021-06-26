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
import autroid.business.adapter.ProfileCarStockAdapter;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.response.ShowroomCarsResponse;
import autroid.business.presenter.ShowroomCarPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.FragmentTags;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.carsales.UsedCarDetailFragment;

/**
 * A simple {@link Fragment} subclass.
 */

public class ShowroomCarsFragment extends Fragment implements OnClickCallBack {

    ShowroomCarPresenter mPresenter;
    @BindView(R.id.car_stock_list)
    RecyclerView recListCars;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    String showroomID,showroomName;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;
    private ProfileCarStockAdapter mProfileCarStockAdapter;

    public ShowroomCarsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showroom_cars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        mPresenter=new ShowroomCarPresenter(this,mMainLayout);

        showroomID=getArguments().getString(Constant.KEY_ID);
        showroomName=getArguments().getString(Constant.Key_Business_Name);

        mTitle.setText(showroomName);

        LinearLayoutManager llmCars;
        llmCars = new LinearLayoutManager(getActivity());
        llmCars.setOrientation(LinearLayoutManager.VERTICAL);
        recListCars.setLayoutManager(llmCars);
        recListCars.setHasFixedSize(true);

        mScrollListener = new EndlessScrollListener(llmCars) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getAllCars(showroomID,pageNo);

            }
        };

        recListCars.addOnScrollListener(mScrollListener);

        mPresenter.getAllCars(showroomID,pageNo);
    }

    public void onSuccess(ShowroomCarsResponse showroomCarsResponse, int page) {

        if(page==0) {
             mProfileCarStockAdapter = new ProfileCarStockAdapter(getActivity(), showroomCarsResponse.getCarDetailBES(), true, this);
            recListCars.setAdapter(mProfileCarStockAdapter);
        }else {
            if(mProfileCarStockAdapter!=null) {
                mProfileCarStockAdapter.mList.addAll(showroomCarsResponse.getCarDetailBES());
                mProfileCarStockAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onImageClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((HomeScreen)getActivity()).makeDrawerVisible();
        bundle.putBoolean(Constant.KEY_IS_VIEW,true);
        ((HomeScreen)getActivity()).addFragment(new UsedCarDetailFragment(), FragmentTags.FRAGMENT_CAR_STOCK_Detail,true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onPublishUnPublishClick(String id) {

    }

    @Override
    public void onEditButtonClick(String id,String status) {

    }

    @Override
    public void onShareButtonClick(String id) {

    }

    @Override
    public void onTitleClick(String id) {

    }
}
