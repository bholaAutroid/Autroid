package autroid.business.view.fragment.search;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
//import com.qiscus.sdk.Qiscus;

import autroid.business.R;
import autroid.business.adapter.SearchBusinessAdapter;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.response.SearchBusinessResponse;
import autroid.business.model.response.UserSearchResponse;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.profile.ShowroomFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBusinessFragment extends Fragment implements OnClickBusinessCallback,View.OnClickListener,LeadsCallback {

    EditText etSearch;
    ImageView mBack;

    //SearchBusinessPresenter mPresenter;
    RelativeLayout mMainLayout;
    RecyclerView recList;


    SearchBusinessResponse mSearchBusinessResponse;
    SearchBusinessAdapter mSearchBusinessAdapter;
    private TextView mTitle;

    FirebaseAnalytics mFirebaseAnalytics;

    public SearchBusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_business, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch= view.findViewById(R.id.search_txt);
        mMainLayout= view.findViewById(R.id.main_layout);
        //mPresenter=new SearchBusinessPresenter(this,mMainLayout);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Search",null);

        recList= (RecyclerView) view.findViewById(R.id.showroom_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

     /*   mTitle=view.initializeViews(R.id.toolbar_title);
        mTitle.setText(getString(R.string.search));
        Toolbar toolbar = view.initializeViews(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

     mBack=view.findViewById(R.id.back);
     mBack.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             getActivity().onBackPressed();
         }
     });

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
    }

    private void searchBusiness(String query){
      //  mPresenter.getSearchedUser(query);
    }

    public void onSearchSuccess(SearchBusinessResponse searchBusinessResponse) {
        mSearchBusinessAdapter=new SearchBusinessAdapter(searchBusinessResponse.getResponseData(),getActivity(),this);
        recList.setAdapter(mSearchBusinessAdapter);

    }

    @Override
    public void onBusinessClick(String id) {

        Bundle params = new Bundle();
        params.putString("business_id", id);
        mFirebaseAnalytics.logEvent("search", params);


        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((HomeScreen) getActivity()).makeDrawerVisible();
        ((HomeScreen) getActivity()).addFragment(new ShowroomFragment(), "SearchBusinessFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    public void onSearchUserSuccess(UserSearchResponse baseResponse) {
      /*  SearchUserAdapter searchUserAdapter=new SearchUserAdapter(getActivity(),baseResponse.getGetSearchUser(),this);
        recList.setAdapter(searchUserAdapter);*/
    }

    @Override
    public void onStatusClick(String id) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,id);
        ((HomeScreen) getActivity()).addFragment(new SearchDetailFragment(), "SearchDetailFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onImportantClick(String id) {

    }

    @Override
    public void onChatClick(String id) {
        try {
//            Qiscus.buildChatWith(id) //here we use email as userID. But you can make it whatever you want.
//                    .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                        @Override
//                        public void onSuccess(Intent intent) {
//                            startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            //do anything if error occurs
//                            throwable.printStackTrace();
//                            throwable.getLocalizedMessage();
//                        }
//                    });
        }catch (Exception e){

        }
    }

    @Override
    public void onCallClick(String number) {
        try {
            if(number!=null) {
                if(number.length()>0) {
                    String phone = number;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
            }
        }
        catch (SecurityException e){

        }catch (Exception e){

        }
    }

    @Override
    public void onConvertLead(String name, String email, String contact, String source) {

    }
}
