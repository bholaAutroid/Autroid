package autroid.business.view.activity;

import android.app.Application;
import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.AddCarSpinnerAdapter;
import autroid.business.adapter.EditGalleryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.OnImageDeleteCallback;
import autroid.business.model.bean.ProductDetailRequestBE;
import autroid.business.model.realm.ProductRealm;
import autroid.business.model.request.AddProductRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddProductResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.ProductBrandResponse;
import autroid.business.presenter.EditProductPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener,OnImageDeleteCallback {

    EditText mCategory, mTitle, mDescription, mPrice;

    String productId,specification="",brand="",model="", title, description, price;


    Button mNext,mAddPhotos;
    RelativeLayout mMainLayout;
    RecyclerView mGallery;

    EditProductPresenter mPresenter;

    private Realm realm;
    RealmController realmController;

    EditGalleryAdapter editGalleryAdapter;

    AppCompatSpinner mBrands;
    TextView mSpecification,mModelNo;
    private ProductBrandResponse productBrandResponse;
    LinearLayout llShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        this.realm = RealmController.with(this).getRealm();
        Application appCtx = (MyApplication) getApplication();
        realmController=new RealmController(appCtx);
        init();

        TextView tvTitle= (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Edit Product Details");

        Toolbar toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mCategory = (EditText) findViewById(R.id.product_category);
        mTitle = (EditText) findViewById(R.id.product_title);
        mDescription = (EditText) findViewById(R.id.product_description);
        mPrice = (EditText) findViewById(R.id.product_price);
        mNext = (Button) findViewById(R.id.product_next);
        mNext.setOnClickListener(this);
        mBrands=findViewById(R.id.spn_brand);
        llShow=findViewById(R.id.ll_show);
        mSpecification=findViewById(R.id.product_specification);
        mModelNo=findViewById(R.id.product_model);

        mPresenter=new EditProductPresenter(this,mMainLayout);


        mGallery=findViewById(R.id.gallery);
        LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getApplicationContext());
        llmGallery.setOrientation(LinearLayoutManager.HORIZONTAL);

        // recListGallery.addItemDecoration(new GridSpacingItemDecoration(1,spacing,true));
        mGallery.setLayoutManager(llmGallery);

        mAddPhotos= (Button) findViewById(R.id.add_product_photos);
        mAddPhotos.setOnClickListener(this);

        productId=getIntent().getStringExtra(Constant.KEY_ID);
        ProductRealm productRealm=realmController.getProduct(productId);
        mTitle.setText(productRealm.getTitle());
        mCategory.setText(productRealm.getCategory());
        mDescription.setText(productRealm.getDescription());
        mPrice.setText(productRealm.getPrice());

        if(productRealm.isShow()){
            llShow.setVisibility(View.VISIBLE);
            mSpecification.setText(productRealm.getSpecification());
            mModelNo.setText(productRealm.getModel_no());
            mPresenter.getBrands(productRealm.getCategory(),productRealm.getBrand());
        }

        editGalleryAdapter=new EditGalleryAdapter(productRealm.getMedia(),true,this);
        mGallery.setAdapter(editGalleryAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.product_next:
                if(validate()){
                    editProduct();
                }
                break;
            case R.id.add_product_photos: {


                break;
            }
        }
    }


    private boolean validate(){
        boolean flag=true;

        title=mTitle.getText().toString();
        description=mDescription.getText().toString();
        price=mPrice.getText().toString();

        if(title.trim().length()==0){
            Utility.hideSoftKeyboard(this);
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter title");
        }
        else if(description.trim().length()==0){
            Utility.hideSoftKeyboard(this);
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter description");
        }else if(price.trim().length()==0){
            Utility.hideSoftKeyboard(this);
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter price");
        }

        if(llShow.isShown()){
            specification=mSpecification.getText().toString();
            model=mModelNo.getText().toString();
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
            else if(model.length()==0){
                flag=false;
                Utility.showResponseMessage(mMainLayout,"Please enter model number");
            }

        }

        return flag;
    }

    private void editProduct(){
        AddProductRequest addProductRequest=new AddProductRequest();
        addProductRequest.setTitle(title);
        addProductRequest.setDescription(description);
        addProductRequest.setPrice(price);
        addProductRequest.setId(productId);
        addProductRequest.setModel_no(model);

        ProductDetailRequestBE productDetailRequestBE=new ProductDetailRequestBE();
        productDetailRequestBE.setBrand(brand);

        productDetailRequestBE.setSpecification(specification);
        addProductRequest.setDetail(productDetailRequestBE);

        mPresenter.editProduct(addProductRequest);
        }

    @Override
    public void onDeleteClick(String id) {
        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        mPresenter.removeProductPic(publishUnpublishRequest,id);
    }

    @Override
    public void onDeleteImageClickId(String id) {

    }

    @Override
    public void onAddImageClick(int position) {
        Intent intent = new Intent(getApplicationContext(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID,productId);
        intent.putExtra(Constant.KEY_TYPE, Constant.VALUE_PRODUCT);
        startActivity(intent);
    }

    public void onDeleteSuccess(BaseResponse baseResponse, String imageId){
        realmController.deleteProductImage(productId,imageId);

        editGalleryAdapter.notifyDataSetChanged();
    }

    public void onEditSuccess(AddProductResponse addProductResponse){

        Utility.showResponseMessage(mMainLayout,addProductResponse.getResponseMessage());

        ProductRealm productRealm=new ProductRealm();
        productRealm.setDescription(addProductResponse.getGetProductResponse().getItem().getDescription());
        productRealm.setPrice(addProductResponse.getGetProductResponse().getItem().getPrice());
        productRealm.setTitle(addProductResponse.getGetProductResponse().getItem().getTitle());
        productRealm.setShow(addProductResponse.getGetProductResponse().getItem().getCategory().isIs_show());
        if(addProductResponse.getGetProductResponse().getItem().getDetail()!=null) {
            productRealm.setBrand(addProductResponse.getGetProductResponse().getItem().getDetail().getBrand());
            productRealm.setSpecification(addProductResponse.getGetProductResponse().getItem().getDetail().getSpecification());
           // productRealm.setModel_no(addProductResponse.getGetProductResponse().getItem().getDetail().getModel_no());
        }
        realmController.updateProduct(productId,productRealm);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_REFRESH_OFFER);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
    }

    public void onBrandSuccess(ProductBrandResponse productBrandResponse,String brand) {
        this.productBrandResponse=productBrandResponse;
        AddCarSpinnerAdapter adapterFuelType = new AddCarSpinnerAdapter(this, R.layout.layout_spinner_text,productBrandResponse.getGetBrands(),"Select Brand");
        mBrands.setAdapter(adapterFuelType);


        for(int i=0;i<productBrandResponse.getGetBrands().size();i++){
            if(brand.length() > 0)
                if(brand.trim().equalsIgnoreCase(productBrandResponse.getGetBrands().get(i).getValue())){
                    mBrands.setSelection(i);
                }
        }
    }


}
