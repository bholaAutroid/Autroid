package autroid.business.view.activity.addProduct;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.R;
import autroid.business.adapter.ProductCategoryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.response.ProductCategoryResponse;
import autroid.business.presenter.ProductCategoryPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.RecyclerItemClickListener;
import autroid.business.view.activity.HomeScreen;

public class ProductSubcategoryActivity extends Fragment {

    RecyclerView recList;
    RelativeLayout mMainLayout;
    ProductCategoryPresenter mPresenter;
    ProductCategoryAdapter productCategoryAdapter;
    ProductCategoryResponse productCategoryResponse;
    String categoryName,id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_product_subcategory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recList= (RecyclerView) view.findViewById(R.id.subcategory_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new ProductCategoryPresenter(this,mMainLayout);

        Bundle intent=getArguments();
        if(intent!=null) {
            id=intent.getString(Constant.KEY_ID);
            categoryName= intent.getString(Constant.KEY_CATEGORY_NAME);
        }

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                    Bundle intent1=new Bundle();

                        intent1.putString(Constant.KEY_CATEGORY_ID,productCategoryResponse.getResponseData().get(position).get_id());
                        intent1.putString(Constant.KEY_CATEGORY_NAME,productCategoryResponse.getResponseData().get(position).getCategory());
                        intent1.putBoolean(Constant.KEY_SUB_CATEGORY_NAME,productCategoryResponse.getResponseData().get(position).isIs_show());
                        //startActivity(intent1);

                        ((HomeScreen) getActivity()).addFragment(new AddProductActivity(), "AddProductActivity", true, false, intent1, ((HomeScreen) getActivity()).currentFrameId);


                    }

                }));

        getSubCategory();
        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Subcategory");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
         //   onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getSubCategory(){
        mPresenter.getProductSubCategory(id);
    }


    public void onSuccess(ProductCategoryResponse productCategoryResponse){
        this.productCategoryResponse=productCategoryResponse;
        productCategoryAdapter=new ProductCategoryAdapter(getActivity(),productCategoryResponse.getResponseData());
        recList.setAdapter(productCategoryAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }


    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SELECT_PRODUCT) {
           // finish();
            getActivity().onBackPressed();
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SELECT_PRODUCT1);
            Events.SendEvent sendEvent1 =
                    new Events.SendEvent(broadcastIntent);
            GlobalBus.getBus().post(sendEvent1);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
