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

import autroid.business.view.fragment.profile.profile_tab.profileutilities.ConstantsProfile;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.PrefrenceMangerProfile;
import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.ProfileProductStockAdapter;
import autroid.business.model.response.ShowroomProductResponse;
import autroid.business.presenter.ShowroomProductPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowroomProductsFragment extends Fragment {

    @BindView(R.id.products_list)
    RecyclerView recListProduct;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.toolbar_title)
    TextView mTitle;

    ShowroomProductPresenter mPresenter;

    String showroomID,showroomName;
    private ProfileProductStockAdapter mProfileProductStockAdapter;

    EndlessScrollListener mScrollListener = null;

    private PrefrenceMangerProfile mangerProfile;

    int pageNo=0;

    public ShowroomProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_showroom_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        mPresenter=new ShowroomProductPresenter(this,mMainLayout);

//        showroomID=getArguments().getString(Constant.KEY_ID);
//        showroomName=getArguments().getString(Constant.Key_Business_Name);

        mangerProfile=new PrefrenceMangerProfile( getActivity() );
        String id=mangerProfile.getString( ConstantsProfile.KEY_ID_ ).trim();
        String name=mangerProfile.getString( ConstantsProfile.KEY_BUSINESS_NAME_).trim();

        if (id!=null){
            showroomID=id;
            showroomName=name;
        }


//        mTitle.setText(showroomName);

        LinearLayoutManager llmCars;
        llmCars = new LinearLayoutManager(getActivity());
        llmCars.setOrientation(LinearLayoutManager.VERTICAL);
        recListProduct.setLayoutManager(llmCars);
        recListProduct.setHasFixedSize(true);

        mScrollListener = new EndlessScrollListener(llmCars) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mPresenter.getAllProducts(showroomID,pageNo);

            }
        };

        recListProduct.addOnScrollListener(mScrollListener);

        mPresenter.getAllProducts(showroomID,pageNo);
    }


    @Override
    public void onStart() {
        super.onStart();

        String id=mangerProfile.getString( ConstantsProfile.KEY_ID_ );
        String name=mangerProfile.getString( ConstantsProfile.KEY_BUSINESS_NAME_ );

        if (id!=null){
            showroomID=id;
            showroomName=name;
        }

    }

    @Override
    public void onResume() {
        super.onResume();


        String id=mangerProfile.getString( ConstantsProfile.KEY_ID_ );
        String name=mangerProfile.getString( ConstantsProfile.KEY_BUSINESS_NAME_ );

        if (id!=null){
            showroomID=id;
            showroomName=name;
        }

    }


    public void onSuccess(ShowroomProductResponse showroomProductResponse,int page) {

        if(page==0) {
            mProfileProductStockAdapter = new ProfileProductStockAdapter(getActivity(), showroomProductResponse.getProducts(), true);
            recListProduct.setAdapter(mProfileProductStockAdapter);
        }
        else {
            mProfileProductStockAdapter.mList.addAll(showroomProductResponse.getProducts());
            mProfileProductStockAdapter.notifyDataSetChanged();
        }
    }
}
