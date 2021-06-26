package autroid.business.view.fragment.carpurchase;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import com.google.gson.Gson;
//import com.qiscus.sdk.Qiscus;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.CarStockAdapter;
import autroid.business.adapter.purchase.ModelFilterAdapter;
import autroid.business.adapter.purchase.SearchModelAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.PurchaseCarCallback;
import autroid.business.model.bean.CarItemsBE;
import autroid.business.model.realm.CarStockRealm;
import autroid.business.model.request.CarLeadRequest;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.model.response.CarLeadResponse;
import autroid.business.model.response.OldCarResponse;
import autroid.business.presenter.OldCarsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.HidingScrollListener;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;

import autroid.business.view.fragment.carsales.UsedCarDetailFragment;
import autroid.business.view.fragment.profile.ShowroomFragment;
import io.realm.Realm;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */

public class PurchaseCarFragment extends Fragment implements  View.OnClickListener, PurchaseCarCallback,AdapterView.OnItemSelectedListener{

    RelativeLayout mMainLayout;
    OldCarsPresenter mPresenter;

    RecyclerView recList;
    CarStockAdapter carStockAdapter;
    private Realm realm;
    RealmController realmController;

    private boolean isMap=true;
    LinearLayout mChangeView,mMapView,mListView;
    AppCompatImageView mMapImg,mListImg;
    TextView mKmTxt;

    OldCarResponse oldCarResponse;

    String strFuel="",strTransmission="",strBody="",strColor="",strMinBudget="",strMaxBudget="",strModel="",strRange="50";
    View view;

    View mapView;

    @BindView(R.id.transmission_txt)
    TextView mTransmission;
    @BindView(R.id.color_txt)
    TextView mColor;
    @BindView(R.id.body_style__txt)
    TextView mBodyStyle;
    @BindView(R.id.fuel_type__txt)
    TextView mFuelType;
    @BindView(R.id.price_txt_range)
    TextView mPriceTxt;
    @BindView(R.id.brands_txt)
    TextView mModelTxt;

    @BindView(R.id.clear_brand_filter)
    TextView mBrandClear;
    @BindView(R.id.clear_price_filter)
    ImageButton mPriceClear;
    @BindView(R.id.apply_brand_filter)
    TextView mBrandApply;
    @BindView(R.id.apply_price_filter)
    TextView mPriceApply;

    @BindView(R.id.back)
    ImageButton mBack;

    @BindView(R.id.bg_transparent)
    View mTransparent;

    @BindView(R.id._layout_price)
    LinearLayout mPriceLayout;

    @BindView(R.id._layout_brand)
    LinearLayout mBrandLayout;

    @BindView(R.id.searchView_brands)
    SearchModelAdapter completionViewBrand;

    @BindView(R.id.price_rangeSeekbar)
    CrystalRangeSeekbar mPriceBar;

    @BindView(R.id.price_txt)
    TextView mPrice;

    HorizontalScrollView mLayoutFilter;

    CarItemsBE p;
    CarItemsResponse carItemsResponse;
    Double latitude=0.0,longitude=0.0;

    private String spinnerSelectText[]={ "Color", "Select Owner","Fuel Type","Transmission","Body Style"};
    private String strBrandId="",postedBy="user,business",strBrandName="",strLocation="";

    TextView mLocation;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    /* Filters*/
    @BindView(R.id.layout_model)
    RelativeLayout mLayoutModel;
    @BindView(R.id.list_models)
    RecyclerView mListModels;
    @BindView(R.id.model_clear)
    ImageButton mModelClear;
    @BindView(R.id.model_ok)
    TextView mModelOk;
    @BindView(R.id.search_model)
    EditText mModelSearch;
    ModelFilterAdapter mModelFilterAdapter;

    @BindView(R.id.layout_transmission)
    RelativeLayout mLayoutTransmission;
    @BindView(R.id.list_transmission)
    RecyclerView mListTransmission;
    @BindView(R.id.transmission_clear)
    ImageButton mTransmissionClear;
    @BindView(R.id.transmission_ok)
    TextView mTransmissionOk;
    ModelFilterAdapter mTransmissionFilterAdapter;

    @BindView(R.id.layout_fuel_type)
    RelativeLayout mLayoutFuelType;
    @BindView(R.id.list_fuel_type)
    RecyclerView mListFuelType;
    @BindView(R.id.fuel_type_clear)
    ImageButton mFuelTypeClear;
    @BindView(R.id.fuel_type_ok)
    TextView mFuelTypeOk;
    ModelFilterAdapter mFuelTypeFilterAdapter;

    @BindView(R.id.layout_body_type)
    RelativeLayout mLayoutBodyType;
    @BindView(R.id.list_body_type)
    RecyclerView mListBodyType;
    @BindView(R.id.body_type_clear)
    ImageButton mBodyTypeClear;
    @BindView(R.id.body_type_ok)
    TextView mBodyTypeOk;

    @BindView(R.id.def_message)
    TextView mDefMessage;

    ModelFilterAdapter mBodyTypeFilterAdapter;

    @BindView(R.id.layout_color)
    RelativeLayout mLayoutColor;
    @BindView(R.id.list_color)
    RecyclerView mListColor;
    @BindView(R.id.color_clear)
    ImageButton mColorClear;
    @BindView(R.id.color_ok)
    TextView mColorOk;
    ModelFilterAdapter mColorFilterAdapter;
    private Dialog dialogOTP;

    SwipeRefreshLayout mSwipeRefreshLayout;
    private int y;

    @BindView(R.id.cb_user)
    AppCompatCheckBox mCbUser;
    @BindView(R.id.cb_business)
    AppCompatCheckBox mCbBusiness;

    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;

    public PurchaseCarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_used_car, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        this.view=view;

        GlobalBus.getBus().register(this);


        // strMinBudget=getArguments().getString(Constant.Key_Min_Price);
        //strMaxBudget=getArguments().getString(Constant.Key_Max_Price);
      //  latitude=getArguments().getDouble(Constant.Key_lat);
        //longitude=getArguments().getDouble(Constant.Key_lng);

        mMainLayout=view.findViewById(R.id.main_layout);
        mPresenter=new OldCarsPresenter(this,mMainLayout);

        mPriceTxt.setOnClickListener(this);
        mModelTxt.setOnClickListener(this);
        mModelOk.setOnClickListener(this);
        mModelClear.setOnClickListener(this);
        mTransmissionOk.setOnClickListener(this);
        mTransmissionClear.setOnClickListener(this);
        mFuelTypeOk.setOnClickListener(this);
        mFuelTypeClear.setOnClickListener(this);
        mBodyTypeOk.setOnClickListener(this);
        mBodyTypeClear.setOnClickListener(this);

        mColorOk.setOnClickListener(this);
        mColorClear.setOnClickListener(this);

        mPriceApply.setOnClickListener(this);
        mBrandApply.setOnClickListener(this);
        mPriceClear.setOnClickListener(this);
        mBrandClear.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mTransparent.setOnClickListener(this);

        mTransmission.setOnClickListener(this);
        mColor.setOnClickListener(this);
        mBodyStyle.setOnClickListener(this);
        mFuelType.setOnClickListener(this);

        LinearLayoutManager llmodel;
        llmodel = new LinearLayoutManager(getActivity());
        llmodel.setOrientation(LinearLayoutManager.VERTICAL);
        mListModels.setLayoutManager(llmodel);

        LinearLayoutManager llTransmission;
        llTransmission = new LinearLayoutManager(getActivity());
        llTransmission.setOrientation(LinearLayoutManager.VERTICAL);
        mListTransmission.setLayoutManager(llTransmission);

        LinearLayoutManager llFuelType;
        llFuelType = new LinearLayoutManager(getActivity());
        llFuelType.setOrientation(LinearLayoutManager.VERTICAL);
        mListFuelType.setLayoutManager(llFuelType);

        LinearLayoutManager llBodyType;
        llBodyType = new LinearLayoutManager(getActivity());
        llBodyType.setOrientation(LinearLayoutManager.VERTICAL);
        mListBodyType.setLayoutManager(llBodyType);

        LinearLayoutManager llColor;
        llColor = new LinearLayoutManager(getActivity());
        llColor.setOrientation(LinearLayoutManager.VERTICAL);
        mListColor.setLayoutManager(llColor);

        mLocation=view.findViewById(R.id.location);
        mLocation.setOnClickListener(this);

        mLayoutFilter=view.findViewById(R.id.horizontal_scroll);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);

                callWs();
            }
        });



        mPriceBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                mPrice.setText("₹ "+String.valueOf(minValue)+" Lakh - "+"₹ "+String.valueOf(maxValue)+" Lakh");

            }
        });

// set final value listener
        mPriceBar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));

                strMinBudget=String.valueOf(minValue);
                strMaxBudget=String.valueOf(maxValue);
            }
        });
        recList= view.findViewById(R.id.car_stock_list);

        recList.setHasFixedSize(true);
        recList.setItemViewCacheSize(20);
        recList.setDrawingCacheEnabled(true);
        recList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        recList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               /* if (dy > 0) {
                    // Scrolling up
                    mLayoutFilter.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                    mLayoutFilter.setVisibility(View.VISIBLE);
                }*/

            }
        });

        recList.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                mLayoutFilter.setVisibility(View.GONE);
            }
            @Override
            public void onShow() {
                mLayoutFilter.setVisibility(View.VISIBLE);
            }
        });

        this.realm = realmController.with(getActivity()).getRealm();
        realmController=RealmController.getInstance();

        realmController.clearCarStock(Constant.STOCK_TYPE_OLD_CARS);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        setFilter();

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                mProgressBar.setVisibility(View.VISIBLE);

                callWsPager();
            }

        };

        recList.addOnScrollListener(mScrollListener);

        mModelSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mModelFilterAdapter!=null)
                    mModelFilterAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        callWs();

        mCbUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                assignPostedBy(isChecked,mCbBusiness.isChecked());
                callWs();
            }
        });

        mCbBusiness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                assignPostedBy(mCbUser.isChecked(),isChecked);
                callWs();
            }
        });




//        getView().setFocusableInTouchMode(true);
//        getView().requestFocus();
//
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        if (isAdded() && isVisible() && getUserVisibleHint()) {
//                            // ... do your thing
//                            return true;
//                        }
//
//                    }
//                }
//                return false;
//            }
//        });

    }

    private void assignPostedBy(boolean isUser,boolean isBusiness) {
        if (isUser && isBusiness)postedBy="user"+","+"business";
        else if(isUser)postedBy="user";
        else if (isBusiness)postedBy="business";
        else postedBy="user"+","+"business";
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();



    }


    public void onSuccess(OldCarResponse carListResponse){

        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

        this.oldCarResponse=carListResponse;
        for(int i=0;i<carListResponse.getStocks().size();i++){
           // addMarker(carListResponse.getStocks().get(i).getLatitude(),carListResponse.getStocks().get(i).getLongitude(),carListResponse.getStocks().get(i).getMaker(),carListResponse.getStocks().get(i).getDescription(),carListResponse.getStocks().get(i).getId());

                CarStockRealm carStockRealm=new CarStockRealm();
                carStockRealm.setTitle(carListResponse.getStocks().get(i).getTitle());
               // carStockRealm.setVariant(carListResponse.getStocks().get(i).getVariant());
                //carStockRealm.setDescription(carListResponse.getStocks().get(i).getDescription());
                carStockRealm.setOdometer(carListResponse.getStocks().get(i).getOdometer());
                //carStockRealm.setKm(carListResponse.getStocks().get(i).getDriven());
                carStockRealm.setFuelType(carListResponse.getStocks().get(i).getFuel_type());
                carStockRealm.setId(carListResponse.getStocks().get(i).getId());
                //carStockRealm.setMaker(carListResponse.getStocks().get(i).getMaker());
                //carStockRealm.setModel(carListResponse.getStocks().get(i).getModel());
                //carStockRealm.setMileage(carListResponse.getStocks().get(i).getMileage());
                //carStockRealm.setPublish(carListResponse.getStocks().get(i).getPublish());
                carStockRealm.setPrice(carListResponse.getStocks().get(i).getPrice());
                //carStockRealm.setStatus(carListResponse.getStocks().get(i).getVehicle_status());
                carStockRealm.setYear(carListResponse.getStocks().get(i).getManufacture_year());
                //carStockRealm.setTransmission(carListResponse.getStocks().get(i).getTransmission());
                //carStockRealm.setColor(carListResponse.getStocks().get(i).getVehicle_color());
                //carStockRealm.setCreatedDate(carListResponse.getStocks().get(i).getPosted_by());
                carStockRealm.setPublisherId(carListResponse.getStocks().get(i).getUser().getId());
                carStockRealm.setPublisherName(carListResponse.getStocks().get(i).getUser().getName());
                //carStockRealm.setPublisherAddress(carListResponse.getStocks().get(i).getLocation());
                //carStockRealm.setCreatedDate(carListResponse.getStocks().get(i).getCreated_at());
                //carStockRealm.setLatitude(carListResponse.getStocks().get(i).getGeometry().get(0));
                //carStockRealm.setLongitude(carListResponse.getStocks().get(i).getGeometry().get(1));
                //carStockRealm.setBodyStyle(carListResponse.getStocks().get(i).getBody_style());
                //carStockRealm.setOwnership(carListResponse.getStocks().get(i).getOwner());
                //carStockRealm.setInsurance(carListResponse.getStocks().get(i).getInsurance());
                //carStockRealm.setServiceRecord(carListResponse.getStocks().get(i).getService_history());
                //carStockRealm.setAccidential(carListResponse.getStocks().get(i).getAccidental());
                //carStockRealm.setRegistrationNo(carListResponse.getStocks().get(i).getRegistration_no());
                //carStockRealm.setBookmarked(carListResponse.getStocks().get(i).getIs_bookmarked());
                carStockRealm.setInternalType(Constant.STOCK_TYPE_OLD_CARS);
                carStockRealm.setUserType(carListResponse.getStocks().get(i).getUser().getAccount_info().getType());
                carStockRealm.setChat(carListResponse.getStocks().get(i).isChatEnable());


                if(carListResponse.getStocks().get(i).getThumbnails().size()>0){
                    carStockRealm.setFeatureImg(carListResponse.getStocks().get(i).getThumbnails().get(0).getFile_address());
            }

              //  addMarker(carListResponse.getStocks().get(i).getGeometry().get(1),carListResponse.getStocks().get(i).getGeometry().get(0),carStockRealm);

                realm.beginTransaction();
                realm.copyToRealm(carStockRealm);
                realm.commitTransaction();
        }
            if(pageNo==0) {
                carStockAdapter = new CarStockAdapter(realmController.getCarStock(Constant.STOCK_TYPE_OLD_CARS), true, this);
                recList.setAdapter(carStockAdapter);

                if(carListResponse.getStocks().size()>0){
                    mDefMessage.setVisibility(View.GONE);
                }
                else {
                    mDefMessage.setVisibility(View.VISIBLE);
                }
            }
    }

    @Override
    public void onClick(View view) {
        int bigPadding=20;
        int smallPadding=15;
        switch (view.getId()){
            case R.id.brands_txt:
                mTransparent.setVisibility(View.VISIBLE);
                mLayoutModel.setVisibility(View.VISIBLE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.transmission_txt:
                mTransparent.setVisibility(View.VISIBLE);
                mLayoutTransmission.setVisibility(View.VISIBLE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.fuel_type__txt:
                mTransparent.setVisibility(View.VISIBLE);
                mLayoutFuelType.setVisibility(View.VISIBLE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.body_style__txt:
                mTransparent.setVisibility(View.VISIBLE);
                mLayoutBodyType.setVisibility(View.VISIBLE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.color_txt:
                mTransparent.setVisibility(View.VISIBLE);
                mLayoutColor.setVisibility(View.VISIBLE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.model_ok:
                mTransparent.setVisibility(View.GONE);
                mLayoutModel.setVisibility(View.GONE);
                int count=0;
                strModel="";
                for(int i=0;i<mModelFilterAdapter.mFilteredList.size();i++){
                    if(mModelFilterAdapter.mFilteredList.get(i).getChecked()){
                        count++;
                        if(strModel.length()==0)
                            strModel=mModelFilterAdapter.mFilteredList.get(i).getValue();
                        else
                            strModel=strModel+","+mModelFilterAdapter.mFilteredList.get(i).getValue();

                    }
                }
                if(count>0) {
                    String styledText = "<font color='#FF0000'>●</font>";
                    mModelTxt.setTextColor(getResources().getColor(R.color.white));
                    mModelTxt.setText(Html.fromHtml("Model "+styledText));
                }
                else {
                    mModelTxt.setTextColor(getResources().getColor(R.color.white));
                    mModelTxt.setText(Html.fromHtml("Model"));

                }
                callWs();
                break;
            case R.id.model_clear:
                mTransparent.setVisibility(View.GONE);

                mLayoutModel.setVisibility(View.GONE);
                break;
            case R.id.transmission_clear:
                mTransparent.setVisibility(View.GONE);

                mLayoutTransmission.setVisibility(View.GONE);

                break;
            case R.id.transmission_ok:
                mTransparent.setVisibility(View.GONE);

                mLayoutTransmission.setVisibility(View.GONE);
                int countTrans=0;
                strTransmission="";
                for(int i=0;i<mTransmissionFilterAdapter.mFilteredList.size();i++){
                    if(mTransmissionFilterAdapter.mFilteredList.get(i).getChecked()){
                        countTrans++;
                        if(strTransmission.length()==0)
                            strTransmission=mTransmissionFilterAdapter.mFilteredList.get(i).getValue();
                        else
                            strTransmission=strTransmission+","+mTransmissionFilterAdapter.mFilteredList.get(i).getValue();

                    }
                }

                if(countTrans>0) {
                    String styledText = "<font color='#FF0000'>●</font>";
                    mTransmission.setTextColor(getResources().getColor(R.color.white));
                    mTransmission.setText(Html.fromHtml("Transmission "+styledText));
                }
                else {
                    mTransmission.setText("Transmission");
                    mTransmission.setTextColor(getResources().getColor(R.color.white));

                }
                callWs();
                break;
            case R.id.fuel_type_clear:
                mTransparent.setVisibility(View.GONE);

                mLayoutFuelType.setVisibility(View.GONE);
                break;
            case R.id.fuel_type_ok:
                mTransparent.setVisibility(View.GONE);

                mLayoutFuelType.setVisibility(View.GONE);
                int countFuel=0;
                strFuel="";
                for(int i=0;i<mFuelTypeFilterAdapter.mFilteredList.size();i++){
                    if(mFuelTypeFilterAdapter.mFilteredList.get(i).getChecked()){
                        countFuel++;
                        if(strFuel.length()==0)
                            strFuel=mFuelTypeFilterAdapter.mFilteredList.get(i).getValue();
                        else
                            strFuel=strFuel+","+mFuelTypeFilterAdapter.mFilteredList.get(i).getValue();

                    }
                }

                if(countFuel>0) {
                    String styledText = "<font color='#FF0000'>●</font>";
                    mFuelType.setTextColor(getResources().getColor(R.color.white));
                    mFuelType.setText(Html.fromHtml("Fuel Type "+styledText));
                }
                else {
                    mFuelType.setText("Fuel Type");
                    mFuelType.setTextColor(getResources().getColor(R.color.white));

                }
                callWs();
                break;
            case R.id.body_type_clear:
                mTransparent.setVisibility(View.GONE);

                mLayoutBodyType.setVisibility(View.GONE);
                break;
            case R.id.body_type_ok:
                mTransparent.setVisibility(View.GONE);

                mLayoutBodyType.setVisibility(View.GONE);
                int countBody=0;
                strBody="";

                for(int i=0;i<mBodyTypeFilterAdapter.mFilteredList.size();i++){
                    if(mBodyTypeFilterAdapter.mFilteredList.get(i).getChecked()){
                        countBody++;
                        if(strBody.length()==0)
                            strBody=mBodyTypeFilterAdapter.mFilteredList.get(i).getValue();
                        else
                            strBody=strBody+","+mBodyTypeFilterAdapter.mFilteredList.get(i).getValue();

                    }
                }

                if(countBody>0) {
                    String styledText = "<font color='#FF0000'>●</font>";
                    mBodyStyle.setTextColor(getResources().getColor(R.color.white));
                    mBodyStyle.setText(Html.fromHtml("Body Type "+styledText));
                }
                else {
                    mBodyStyle.setText("Body Type");
                    mBodyStyle.setTextColor(getResources().getColor(R.color.white));

                }
                callWs();
                break;
            case R.id.color_clear:
                mTransparent.setVisibility(View.GONE);

                mLayoutColor.setVisibility(View.GONE);
                break;
            case R.id.color_ok:
                mTransparent.setVisibility(View.GONE);

                mLayoutColor.setVisibility(View.GONE);
                int countColor=0;
                strColor="";
                for(int i=0;i<mColorFilterAdapter.mFilteredList.size();i++){
                    if(mColorFilterAdapter.mFilteredList.get(i).getChecked()){
                        countColor++;
                        if(strColor.length()==0)
                            strColor=mColorFilterAdapter.mFilteredList.get(i).getValue();
                        else
                            strColor=strColor+","+mColorFilterAdapter.mFilteredList.get(i).getValue();

                    }
                }
                if(countColor>0) {
                    String styledText = "<font color='#FF0000'>●</font>";
                    mColor.setTextColor(getResources().getColor(R.color.white));
                    mColor.setText(Html.fromHtml("Color "+styledText));
                }
                else {
                    mColor.setText("Color");
                    mColor.setTextColor(getResources().getColor(R.color.white));

                }
                callWs();
                break;
            case R.id.price_txt_range:
                mTransparent.setVisibility(View.VISIBLE);

                mPriceLayout.setVisibility(View.VISIBLE);
                mBrandLayout.setVisibility(View.GONE);
                break;
            case R.id.clear_brand_filter:
                mTransparent.setVisibility(View.GONE);

                mBrandLayout.setVisibility(View.GONE);
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.clear_price_filter:
                mTransparent.setVisibility(View.GONE);

                mPriceLayout.setVisibility(View.GONE);
                mBrandLayout.setVisibility(View.GONE);
                break;
            case R.id.apply_brand_filter:
                mTransparent.setVisibility(View.GONE);

                // mBrandTxt.setText(strBrandName);
                mBrandLayout.setVisibility(View.GONE);
                mPriceLayout.setVisibility(View.GONE);
                callWs();

                break;
            case R.id.apply_price_filter:
                mTransparent.setVisibility(View.GONE);

                mPriceTxt.setTextColor(getResources().getColor(R.color.white));
                mPriceTxt.setText(mPrice.getText().toString());
                mPriceLayout.setVisibility(View.GONE);
                mBrandLayout.setVisibility(View.GONE);

                callWs();
                break;
            case R.id.back:
                getActivity().onBackPressed();
                break;
            case R.id.bg_transparent:
                mLayoutModel.setVisibility(View.GONE);
                mLayoutTransmission.setVisibility(View.GONE);
                mLayoutBodyType.setVisibility(View.GONE);
                mPriceLayout.setVisibility(View.GONE);
                mLayoutColor.setVisibility(View.GONE);
                mTransparent.setVisibility(View.GONE);
                mLayoutFuelType.setVisibility(View.GONE);
                break;
            case R.id.location:
               /* // mAddressFrame.setVisibility(View.VISIBLE);
                try {
                    Intent intentLoc =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());

                    startActivityForResult(intentLoc, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }*/

                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
       /* if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            //some code

            if(data!=null){
                strFuel=data.getStringExtra(Constant.Key_Fuel);
                strTransmission=data.getStringExtra(Constant.Key_Transmission);
                strBody=data.getStringExtra(Constant.Key_Body);
                strMinBudget=data.getStringExtra(Constant.Key_Min_Price);
                strMaxBudget=data.getStringExtra(Constant.Key_Max_Price);
                strModel=data.getStringExtra(Constant.Key_Model);


            }
        }*/
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mLocation.setText(place.getAddress().toString());
                strLocation=place.getAddress().toString();
                latitude=place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;
                callWs();
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void setFilter() {

        Gson gson = new Gson();
        final PreferenceManager preferenceManager=PreferenceManager.getInstance();
        String carItems=preferenceManager.getStringPreference(getContext(),Constant.SP_CAR_ITEMS);
        carItemsResponse= gson.fromJson(carItems,
                CarItemsResponse.class);
        mModelFilterAdapter=new ModelFilterAdapter(getActivity(),carItemsResponse.getGetCarItems().getModels());
        mListModels.setAdapter(mModelFilterAdapter);

        mTransmissionFilterAdapter=new ModelFilterAdapter(getActivity(),carItemsResponse.getGetCarItems().getTransmissions());
        mListTransmission.setAdapter(mTransmissionFilterAdapter);

        mFuelTypeFilterAdapter=new ModelFilterAdapter(getActivity(),carItemsResponse.getGetCarItems().getFuel_type());
        mListFuelType.setAdapter(mFuelTypeFilterAdapter);

        mBodyTypeFilterAdapter=new ModelFilterAdapter(getActivity(),carItemsResponse.getGetCarItems().getBody_style());
        mListBodyType.setAdapter(mBodyTypeFilterAdapter);

        mColorFilterAdapter=new ModelFilterAdapter(getActivity(),carItemsResponse.getGetCarItems().getColor());
        mListColor.setAdapter(mColorFilterAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void callWs(){
        pageNo=0;
        realmController.clearCarStock(Constant.STOCK_TYPE_OLD_CARS);
        mPresenter.getShowroom(strFuel,strTransmission,strBody,strColor,strMinBudget,strMaxBudget,strModel,strBrandId,strRange,latitude,longitude,postedBy,pageNo,true);
    }

    private void callWsPager(){
        mPresenter.getShowroom(strFuel,strTransmission,strBody,strColor,strMinBudget,strMaxBudget,strModel,strBrandId,strRange,latitude,longitude,postedBy,pageNo,false);
    }


    @Override
    public void onShareButtonClick(String carName, String carId, String carImage) {
        carName=carName+" car at CarEager, world-class services & certified used cars at the lowest prices. Download the app now - ";
        Utility.share(getActivity(),carName+" "+"https://goo.gl/fU5Upb");
    }

    @Override
    public void onTitleClick(int position, String postedBy, String carId) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,carId);
        bundle.putString(Constant.KEY_TYPE,Constant.STOCK_TYPE_OLD_CARS);
        bundle.putBoolean(Constant.KEY_IS_VIEW,true);
        ((HomeScreen)getActivity()).addFragment(new UsedCarDetailFragment(),"UsedCarDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onPublisherClick(String car, String carId, String type) {

        CarLeadRequest carLeadRequest=new CarLeadRequest();
        carLeadRequest.setCar(car);
        mPresenter.carLead(carLeadRequest,carId,type);
             /*  if(postedBy.equalsIgnoreCase("user")) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID, carId);
            ((HomeScreen) getActivity()).makeDrawerVisible();
           // ((HomeScreen) getActivity()).addFragment(new UserProfileFragment(), "UserProfileFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID,carId);
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new ShowroomFragment(), "ShowroomFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }*/
    }

    @Override
    public void onChatClick(String id) {
        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();//
        try {
//            Qiscus.buildChatWith(id)//here we use email as userID. But you can make it whatever you want.
//                    .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                        @Override
//                        public void onSuccess(Intent intent) {
//                            progressDialog.dismiss();
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            //do anything if error occurs
//                            Toast.makeText(getContext(), "Chat facility is not available for this seller.", Toast.LENGTH_SHORT).show();
//
//                            progressDialog.dismiss();
//                            throwable.printStackTrace();
//                            throwable.getLocalizedMessage();
//                        }
//                    });
        }catch (NullPointerException e){
            Toast.makeText(getContext(), "Chat facility is not available for this seller.", Toast.LENGTH_SHORT).show();
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }

        }catch (Exception e){
            Toast.makeText(getContext(), "Chat facility is not available for this seller.", Toast.LENGTH_SHORT).show();

            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }


    public void onLeadSuccess(CarLeadResponse carItemsResponse, final String userId, final String type) {

        if(dialogOTP!=null && dialogOTP.isShowing()) dialogOTP.dismiss();

        Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_ID,userId);
            ((HomeScreen) getActivity()).makeDrawerVisible();
            if(type.equalsIgnoreCase("Business"))
                ((HomeScreen) getActivity()).addFragment(new ShowroomFragment(), "ShowroomFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
            else
               ((HomeScreen) getActivity()).addFragment(new UserProfileFragment(), "UserProfileFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }
}
