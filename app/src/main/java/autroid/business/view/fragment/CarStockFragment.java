package autroid.business.view.fragment;


import android.app.Application;
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
import autroid.business.adapter.CarStockAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.response.CarListResponse;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.PublishResponse;
import autroid.business.presenter.CarStockPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.editCar.EditCarActivity;
import autroid.business.view.activity.editCar.EditNewCarActivity;
import autroid.business.view.fragment.addcar.AddCarStep1Fragment;
import autroid.business.view.fragment.carsales.UsedCarDetailFragment;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarStockFragment extends Fragment implements View.OnClickListener,OnClickCallBack {

    RecyclerView recList;

    RelativeLayout mMainLayout;
    CarStockPresenter mPresenter;
    CarStockAdapter carStockAdapter;
    FloatingActionButton mAddButton;
    TextView mSaved,mPublished;

    CarListResponse mCarStockResponse;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Realm realm;
    RealmController realmController;
    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    private FirebaseAnalytics mFirebaseAnalytics;


    public static CarStockFragment newInstance() {
        CarStockFragment fragment = new CarStockFragment();
        return fragment;
    }

    public CarStockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_stock, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Car Stock",null);

        realmController.clearCars();

        recList= (RecyclerView) view.findViewById(R.id.car_stock_list);
        mAddButton= (FloatingActionButton) view.findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(this);

        mPublished= (TextView) view.findViewById(R.id.txt_published);
        mSaved= (TextView) view.findViewById(R.id.txt_saved);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new CarStockPresenter(this,mMainLayout);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                pageNo=0;
                realmController.clearCars();
                getCarStock(pageNo);
            }
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                getCarStock(pageNo);

            }
        };

        recList.addOnScrollListener(mScrollListener);
        getCarStock(pageNo);

    }

    private void getCarStock(int page){
        Boolean isLoading=false;
        if(page==0){
            isLoading=true;
        }
        else {
            isLoading=false;
        }
        mPresenter.getCarStock(page,isLoading);
    }

    public void onSuccess(CarListResponse carStockResponse){
        mCarStockResponse=carStockResponse;
        mSwipeRefreshLayout.setRefreshing(false);
        mSaved.setText(carStockResponse.getGetCarStock().getTotal());
        mPublished.setText(carStockResponse.getGetCarStock().getPublished());

        for(int i=0;i<carStockResponse.getGetCarStock().getStocks().size();i++){
            CarRealm carRealm=new CarRealm();
            carRealm.setCreatedDate(carStockResponse.getGetCarStock().getStocks().get(i).getCreated_at());
            carRealm.setFuelType(carStockResponse.getGetCarStock().getStocks().get(i).getFuel_type());
            carRealm.setId(carStockResponse.getGetCarStock().getStocks().get(i).getId());
            carRealm.setKm(carStockResponse.getGetCarStock().getStocks().get(i).getDriven());
            carRealm.setPrice(carStockResponse.getGetCarStock().getStocks().get(i).getPrice());
            //carRealm.setPriceNumeric(carStockResponse.getGetCarStock().getStocks().get(i).getNumericPrice());
            carRealm.setAccidential(carStockResponse.getGetCarStock().getStocks().get(i).getAccidental());
            carRealm.setColor(carStockResponse.getGetCarStock().getStocks().get(i).getVehicle_color());
            carRealm.setDescription(carStockResponse.getGetCarStock().getStocks().get(i).getDescription());
            carRealm.setInsurance(carStockResponse.getGetCarStock().getStocks().get(i).getInsurance());
            carRealm.setMileage(carStockResponse.getGetCarStock().getStocks().get(i).getMileage());
            carRealm.setOwnership(carStockResponse.getGetCarStock().getStocks().get(i).getOwner());
            carRealm.setRegistrationNo(carStockResponse.getGetCarStock().getStocks().get(i).getRegistration_no());
            carRealm.setServiceRecord(carStockResponse.getGetCarStock().getStocks().get(i).getService_history());
            carRealm.setTransmission(carStockResponse.getGetCarStock().getStocks().get(i).getTransmission());
            carRealm.setMaker(carStockResponse.getGetCarStock().getStocks().get(i).getMaker());
            carRealm.setModel(carStockResponse.getGetCarStock().getStocks().get(i).getModel());
            carRealm.setVariant(carStockResponse.getGetCarStock().getStocks().get(i).getVariant());
            carRealm.setVariantId(carStockResponse.getGetCarStock().getStocks().get(i).getVariant_id());
            carRealm.setStatus(carStockResponse.getGetCarStock().getStocks().get(i).getVehicle_status());
            carRealm.setLatitude(carStockResponse.getGetCarStock().getStocks().get(i).getGeometry().get(0));
            carRealm.setLongitude(carStockResponse.getGetCarStock().getStocks().get(i).getGeometry().get(1));
            carRealm.setBodyStyle(carStockResponse.getGetCarStock().getStocks().get(i).getBody_style());

            carRealm.setPublish(carStockResponse.getGetCarStock().getStocks().get(i).getPublish());
            carRealm.setPublisherAddress(carStockResponse.getGetCarStock().getStocks().get(i).getUser().getAddress().getLocation());
            carRealm.setPublisherName(carStockResponse.getGetCarStock().getStocks().get(i).getUser().getName());
            carRealm.setTitle(carStockResponse.getGetCarStock().getStocks().get(i).getTitle());
            carRealm.setYear(carStockResponse.getGetCarStock().getStocks().get(i).getManufacture_year());

            if(carStockResponse.getGetCarStock().getStocks().get(i).getThumbnails().size()>0){
                RealmList<MediaRealm> mediaRealms=new RealmList<>();
                for(int j=0;j<carStockResponse.getGetCarStock().getStocks().get(i).getThumbnails().size();j++){
                    MediaRealm mediaRealm=new MediaRealm();
                    mediaRealm.setId(carStockResponse.getGetCarStock().getStocks().get(i).getThumbnails().get(j).getId());
                    mediaRealm.setPath(carStockResponse.getGetCarStock().getStocks().get(i).getThumbnails().get(j).getFile_address());
                    mediaRealms.add(mediaRealm);
                }
                carRealm.setMedia(mediaRealms);
            }

            realm.beginTransaction();
            realm.copyToRealm(carRealm);
            realm.commitTransaction();

        }

        if(pageNo==0) {
           // carStockAdapter = new CarStockAdapter(realmController.getCars(), true, this);
            //recList.setAdapter(carStockAdapter);
        }
        else {
            carStockAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                ((AwsHomeActivity)getActivity()).makeDrawerVisible();
                ((AwsHomeActivity)getActivity()).addFragment(new AddCarStep1Fragment(), FragmentTags.FRAGMENT_ADD_CAR,true,false,null,((AwsHomeActivity) getActivity()).currentFrameId);
                break;
        }
    }

    @Override
    public void onImageClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity)getActivity()).addFragment(new UsedCarDetailFragment(), FragmentTags.FRAGMENT_CAR_STOCK_Detail,true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onPublishUnPublishClick(String id) {

        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        //mPresenter.publishCar(publishUnpublishRequest,id);


        /* publish unpublish logic remaining*/
    }

    public void publishUnpublishResponse(PublishResponse baseResponse,String id){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
        mPublished.setText(baseResponse.getGetPublishResponse().getPublished()+"");

        Bundle params = new Bundle();
        params.putBoolean("publish_car", baseResponse.getGetPublishResponse().isPublish());
        mFirebaseAnalytics.logEvent("publish_car", params);

        //realmController.updateCarsPublishStatus(id,baseResponse.getGetPublishResponse().isPublish(),Constant.KEY_CAR_TYPE);
        carStockAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditButtonClick(String id,String status) {
        if(status.equalsIgnoreCase("Used")) {
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,id);
            ((AwsHomeActivity) getActivity()).makeDrawerVisible();
            ((AwsHomeActivity) getActivity()).addFragment(new EditCarActivity(), "EditCarActivity", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
        }
        else
        {
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,id);
            ((AwsHomeActivity) getActivity()).makeDrawerVisible();
            ((AwsHomeActivity) getActivity()).addFragment(new EditNewCarActivity(), "EditNewCarActivity", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
        }
    }

    @Override
    public void onShareButtonClick(String id) {
        Utility.share(getActivity(),"");
    }

    @Override
    public void onTitleClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity)getActivity()).addFragment(new UsedCarDetailFragment(), FragmentTags.FRAGMENT_CAR_STOCK_Detail,true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }

}
