package autroid.business.view.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.response.SearchBusinessResponse;
import autroid.business.presenter.SearchClaimBusinessPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.fragment.ClaimBusinessFragment;

public class SearchClaimBusinessActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    Button btnRegister;
    AppCompatAutoCompleteTextView etSearch;
    String strSearch;

    SearchClaimBusinessPresenter mPresenter;
    RelativeLayout mMainLayout;

    SearchBusinessResponse mSearchBusinessResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_business);

        etSearch= (AppCompatAutoCompleteTextView) findViewById(R.id.search);
        btnRegister= (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        mMainLayout= (RelativeLayout) findViewById(R.id.main_layout);
        mPresenter=new SearchClaimBusinessPresenter(this,mMainLayout);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().length()>2 && charSequence.toString().length()<8){
                    searchBusiness(charSequence.toString());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etSearch.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                    startActivity(new Intent(this,RegisterActivity.class));
                break;
        }
    }

    private void searchBusiness(String query){
        mPresenter.getSearchedBusiness(query);
    }

    public void onSearchSuccess(SearchBusinessResponse mSearchBusinessResponse)
    {
        Utility.hideSoftKeyboard(this);
        if(mSearchBusinessResponse.getResponseData().size()>0) {
            this.mSearchBusinessResponse=mSearchBusinessResponse;
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mSearchBusinessResponse.getResponseData().size(); i++) {
                list.add(mSearchBusinessResponse.getResponseData().get(i).getName());
            }
            etSearch.showDropDown();
            etSearch.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list));
        }
        
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ClaimBusinessFragment claimBusinessFragment=new ClaimBusinessFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,mSearchBusinessResponse.getResponseData().get(i).getId());
        bundle.putString(Constant.KEY_CATEGORY_NAME,mSearchBusinessResponse.getResponseData().get(i).getName());
        claimBusinessFragment.setArguments(bundle);
        claimBusinessFragment.show(getSupportFragmentManager(),"ClaimBusinessFragment");
    }
}
