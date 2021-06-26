package autroid.business.view.activity.addProduct;

import android.app.Application;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.ProductDetailRequestBE;
import autroid.business.model.realm.ProductRealm;
import autroid.business.model.request.AddProductRequest;
import autroid.business.model.response.AddProductResponse;
import autroid.business.model.response.ProductBrandResponse;
import autroid.business.presenter.AddProductPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import io.realm.Realm;

public class AddProductActivity extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {
    EditText mCategory,mTitle,mDescription,mPrice,mDiscount,mBatteryLength,mBatteryWidth,mBatteryHeight;

    String categoryId,categoryName,specification="",discount,brand="",model="",title,description,price,orientation,dimension;
    Bundle intent;
    Boolean isShow;

    Button mNext;
    RelativeLayout mMainLayout;
    AddProductPresenter mPresenter;
    LinearLayout llShow,llTyre,llBattery;

    private Realm realm;
    RealmController realmController;

    AppCompatSpinner mBrands,mOrientation;
    EditText mSpecification,mModelNo;
    private ProductBrandResponse productBrandResponse;

    AppCompatAutoCompleteTextView etSearchTyre;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void  init(View view){
        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new AddProductPresenter(this,mMainLayout);

        mCategory= (EditText) view.findViewById(R.id.product_category);
        mTitle= (EditText) view.findViewById(R.id.product_title);
        mDescription= (EditText) view.findViewById(R.id.product_description);
        mPrice= (EditText) view.findViewById(R.id.product_price);
        mNext= (Button) view.findViewById(R.id.product_next);
        mNext.setOnClickListener(this);
        mBrands=view.findViewById(R.id.spn_brand);
        mOrientation=view.findViewById(R.id.spn_battery_orientation);
        llShow=view.findViewById(R.id.ll_show);
        llBattery=view.findViewById(R.id.layout_battery);
        llTyre=view.findViewById(R.id.layout_tyre);
        mSpecification=view.findViewById(R.id.battery_specification);
        mModelNo=view.findViewById(R.id.product_model);
        mDiscount=view.findViewById(R.id.discount);
        mBatteryLength=view.findViewById(R.id.battery_length);
        mBatteryWidth=view.findViewById(R.id.battery_width);
        mBatteryHeight=view.findViewById(R.id.battery_height);

        etSearchTyre= (AppCompatAutoCompleteTextView) view.findViewById(R.id.search);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Product",null);

        intent=getArguments();
        if(intent!=null){
            categoryId=intent.getString(Constant.KEY_CATEGORY_ID);
            categoryName=intent.getString(Constant.KEY_CATEGORY_NAME);
            isShow=intent.getBoolean(Constant.KEY_SUB_CATEGORY_NAME,false);

            mCategory.setText(categoryName);
            mCategory.setEnabled(false);

            if(isShow){
                llShow.setVisibility(View.VISIBLE);
                mPresenter.getBrands(categoryName);
                if(categoryName.equalsIgnoreCase("Tyre")){
                    llTyre.setVisibility(View.VISIBLE);
                }
                else if(categoryName.equalsIgnoreCase("Battery")){
                    llBattery.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, getResources().getStringArray(R.array.orientation));
                    mOrientation.setAdapter(adapterBrand);
                }
            }

        }

        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Add Product");



        etSearchTyre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length()>1 && charSequence.toString().length()<8){
                    mPresenter.getTyre(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etSearchTyre.setOnItemClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
           // onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.product_next:
                if(validate()){
                    addProduct();
                }
                break;
        }
    }

    private boolean validate(){
        boolean flag=true;

        title=mTitle.getText().toString();
        description=mDescription.getText().toString();
        price=mPrice.getText().toString();
        model=mModelNo.getText().toString();
        discount=mDiscount.getText().toString();

        if(title.trim().length()==0){
          //  Utility.hideSoftKeyboard(getActivity());
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter title");
        }
        else if(description.trim().length()==0){
          //  Utility.hideSoftKeyboard(getActivity());
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter description");
        }else if(price.trim().length()==0){
           // Utility.hideSoftKeyboard(getActivity());
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter price");
        }

        if(isShow){
            if(categoryName.equalsIgnoreCase("Tyre")){
                specification=etSearchTyre.getText().toString();

                if(productBrandResponse!=null){

                    brand=productBrandResponse.getGetBrands().get(mBrands.getSelectedItemPosition()).getValue();
                }

                if(mBrands.getSelectedItemPosition()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please select product brand");
                }
                else if(specification.length()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please enter product specification");
                }

            }
            else if(categoryName.equalsIgnoreCase("Battery")){
                specification=mSpecification.getText().toString();
                orientation=mOrientation.getSelectedItem().toString();

                if(productBrandResponse!=null){

                    brand=productBrandResponse.getGetBrands().get(mBrands.getSelectedItemPosition()).getValue();
                }
                else if(mBrands.getSelectedItemPosition()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please select product brand");
                }
                if(mOrientation.getSelectedItemPosition()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please select battery orientation");
                }
                else if(specification.length()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please enter product specification");
                }
                else if(mBatteryHeight.getText().length()==0 || mBatteryWidth.getText().length()==0 || mBatteryLength.getText().length()==0){
                    flag=false;
                    Utility.showResponseMessage(mMainLayout,"Please enter correct battery size");
                }
                else {
                    dimension=mBatteryHeight.getText().toString()+"x"+mBatteryWidth.getText().toString()+"x"+mBatteryLength.getText().toString();
                }

            }

            }

        return flag;
    }

    private void addProduct(){
        AddProductRequest addProductRequest=new AddProductRequest();
        addProductRequest.setCategory(categoryId);
        addProductRequest.setTitle(title);
        addProductRequest.setDescription(description);
        addProductRequest.setPrice(price);
        addProductRequest.setModel_no(model);
        addProductRequest.setDiscount(discount);

        ProductDetailRequestBE productDetailRequestBE=new ProductDetailRequestBE();
        productDetailRequestBE.setBrand(brand);

        productDetailRequestBE.setSpecification(specification);
        productDetailRequestBE.setOrientation(orientation);
        productDetailRequestBE.setDimension(dimension);
        addProductRequest.setDetail(productDetailRequestBE);


        mPresenter.addProduct(addProductRequest);
    }

    public void onSuccess(AddProductResponse addProductResponse){

        ProductRealm productRealm=new ProductRealm();
        productRealm.setBusinessName(addProductResponse.getGetProductResponse().getItem().getBusiness().getName());
        productRealm.setCategory(addProductResponse.getGetProductResponse().getItem().getCategory().getCategory());
        productRealm.setCreated_at(addProductResponse.getGetProductResponse().getItem().getCreated_at());
        productRealm.setDescription(addProductResponse.getGetProductResponse().getItem().getDescription());
        productRealm.setId(addProductResponse.getGetProductResponse().getItem().getId());
        productRealm.setPrice(addProductResponse.getGetProductResponse().getItem().getPrice());
        productRealm.setTitle(addProductResponse.getGetProductResponse().getItem().getTitle());

        realm.beginTransaction();
        realm.copyToRealm(productRealm);
        realm.commitTransaction();

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, addProductResponse.getGetProductResponse().getItem().getCategory().getCategory());
        mFirebaseAnalytics.logEvent("add_product", params);


        Intent intent=new Intent(getActivity(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID,addProductResponse.getGetProductResponse().getItem().getId());
        intent.putExtra(Constant.KEY_TYPE,Constant.VALUE_PRODUCT);
        startActivity(intent);
        getActivity().onBackPressed();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SELECT_PRODUCT);
        Events.SendEvent sendEvent =
                new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);

    }


    public void onBrandSuccess(ProductBrandResponse productBrandResponse) {
        this.productBrandResponse=productBrandResponse;
        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(getActivity(), R.layout.layout_spinner_text,productBrandResponse.getGetBrands(),"Select Brand");
        mBrands.setAdapter(adapterFuelType);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void tyreResponse(ProductBrandResponse baseResponse) {

        Utility.hideSoftKeyboard(getActivity());
        if(baseResponse.getGetBrands().size()>0) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < baseResponse.getGetBrands().size(); i++) {
                list.add(baseResponse.getGetBrands().get(i).getValue());
            }
            etSearchTyre.showDropDown();
            etSearchTyre.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent fragmentActivityMessage) {
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
