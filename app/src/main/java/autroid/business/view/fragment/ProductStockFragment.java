package autroid.business.view.fragment;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.ProductStockAdapter;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.realm.ProductRealm;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.ProductStockResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.presenter.ProductStockPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.view.activity.EditProductActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.RealmGalleryActivity;
import autroid.business.view.activity.addProduct.ProductCategoryActivity;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */

public class ProductStockFragment extends Fragment implements View.OnClickListener,OnClickCallBack{

    RecyclerView recList;
    ProductStockAdapter productStockAdapter;

    RelativeLayout mMainLayout;
    ProductStockPresenter mPresenter;

    FloatingActionButton mAddButton;
    ProductStockResponse mProductStockResponse;
    TextView mSaved,mPublished;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private Realm realm;
    RealmController realmController;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    FirebaseAnalytics mFirebaseAnalytics;

    public ProductStockFragment() {
        // Required empty public constructor
    }

    public static ProductStockFragment newInstance() {
        ProductStockFragment fragment = new ProductStockFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_stock, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Product Stock",null);

        realmController.clearProducts();

        recList= (RecyclerView) view.findViewById(R.id.product_stock_list);
        mAddButton= (FloatingActionButton) view.findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(this);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mSaved= (TextView) view.findViewById(R.id.txt_saved);
        mPublished= (TextView) view.findViewById(R.id.txt_published);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new ProductStockPresenter(this,mMainLayout);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                pageNo=0;
                realmController.clearProducts();
                getProduct(pageNo);
            }
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                getProduct(pageNo);

            }
        };

        recList.addOnScrollListener(mScrollListener);

        getProduct(pageNo);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new ProductCategoryActivity(), "ProductCategoryActivity", true, false, null, ((HomeScreen) getActivity()).currentFrameId);
                break;
        }
    }

    private void getProduct(int page){
        Boolean isLoading=false;
        if(page==0){
            isLoading=true;
        }
        else {
            isLoading=false;
        }
        mPresenter.getProductStock(page,isLoading);
    }

    public void onSuccess(ProductStockResponse productStockResponse,Boolean isLoading){
        this.mProductStockResponse=productStockResponse;
        mSwipeRefreshLayout.setRefreshing(false);
        mSaved.setText(productStockResponse.getResponseData().getTotal());
        mPublished.setText(productStockResponse.getResponseData().getPublished());

        for(int i=0;i<productStockResponse.getResponseData().getProducts().size();i++){
            ProductRealm productRealm=new ProductRealm();

            productRealm.setBusinessName(productStockResponse.getResponseData().getProducts().get(i).getBusiness().getName());
            productRealm.setCategory(productStockResponse.getResponseData().getProducts().get(i).getCategory().getCategory());
            productRealm.setCreated_at(productStockResponse.getResponseData().getProducts().get(i).getCreated_at());
            productRealm.setDescription(productStockResponse.getResponseData().getProducts().get(i).getDescription());
            productRealm.setId(productStockResponse.getResponseData().getProducts().get(i).getId());
            productRealm.setPrice(productStockResponse.getResponseData().getProducts().get(i).getPrice());
            productRealm.setTitle(productStockResponse.getResponseData().getProducts().get(i).getTitle());
            productRealm.setShow(productStockResponse.getResponseData().getProducts().get(i).getCategory().isIs_show());
            if(productStockResponse.getResponseData().getProducts().get(i).getDetail()!=null) {
                productRealm.setBrand(productStockResponse.getResponseData().getProducts().get(i).getDetail().getBrand());
                productRealm.setSpecification(productStockResponse.getResponseData().getProducts().get(i).getDetail().getSpecification());
              //  productRealm.setModel_no(productStockResponse.getResponseData().getProducts().get(i).getDetail().getModel_no());
            }

            if(productStockResponse.getResponseData().getProducts().get(i).getThumbnails().size()>0){
                RealmList<MediaRealm> mediaRealms=new RealmList<>();
                for(int j=0;j<productStockResponse.getResponseData().getProducts().get(i).getThumbnails().size();j++){
                    MediaRealm mediaRealm=new MediaRealm();
                    mediaRealm.setId(productStockResponse.getResponseData().getProducts().get(i).getThumbnails().get(j).getId());
                    mediaRealm.setPath(productStockResponse.getResponseData().getProducts().get(i).getThumbnails().get(j).getFile_address());
                    mediaRealms.add(mediaRealm);
                }
                productRealm.setMedia(mediaRealms);
            }
            realm.beginTransaction();
            realm.copyToRealm(productRealm);
            realm.commitTransaction();
        }

        if(isLoading) {
            productStockAdapter = new ProductStockAdapter(realmController.getProducts(), true, this);
            recList.setAdapter(productStockAdapter);
        }
    }

    @Override
    public void onImageClick(String id) {

            Intent intent = new Intent(getActivity(), RealmGalleryActivity.class);
            intent.putExtra(Constant.KEY_TYPE,Constant.VALUE_PRODUCT);
            intent.putExtra(Constant.KEY_ID,id);
            startActivity(intent);

    }

    @Override
    public void onPublishUnPublishClick(String id) {
        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        mPresenter.publishCar(publishUnpublishRequest,id);

    }

    public void publishUnpublishResponse(PublishResponse baseResponse,String id){

        Bundle params = new Bundle();
        params.putBoolean("publish_product", baseResponse.getGetPublishResponse().isPublish());
        mFirebaseAnalytics.logEvent("publish_product", params);

        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
        mPublished.setText(baseResponse.getGetPublishResponse().getPublished()+"");
        realmController.updateProductPublishStatus(id,baseResponse.getGetPublishResponse().isPublish());
        productStockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditButtonClick(String id,String status) {
        Intent intent=new Intent(getActivity(), EditProductActivity.class);
        intent.putExtra(Constant.KEY_ID,id);
        startActivity(intent);
    }

    @Override
    public void onShareButtonClick(String id) {
        Utility.share(getActivity(),"");
    }

    @Override
    public void onTitleClick(String id) {

    }
}
