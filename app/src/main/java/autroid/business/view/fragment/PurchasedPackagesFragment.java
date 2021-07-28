package autroid.business.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//import com.qiscus.sdk.Qiscus;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.PurchasedPackagesAdapter;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.response.PurchasedPackagesResponse;
import autroid.business.presenter.PurchasedPackagesPresenter;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.leads.LeadCreateFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedPackagesFragment extends Fragment implements LeadsCallback {

    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;

    @BindView(R.id.package_list)
    RecyclerView mList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    PurchasedPackagesPresenter mPresenter;

    public PurchasedPackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchased_packages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        LinearLayoutManager llm,llmPeople,llmPlace,llmTags;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);



        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);
                mPresenter.getPackages();
            }
        });



        mPresenter=new PurchasedPackagesPresenter(this,mMainLayout);
        mPresenter.getPackages();
    }


    public void onSuccess(PurchasedPackagesResponse purchasedPackagesResponse) {
        mSwipeRefreshLayout.setRefreshing(false);
        if(purchasedPackagesResponse.getGetPackaged().size()>0) {

            PurchasedPackagesAdapter purchasedPackagesAdapter = new PurchasedPackagesAdapter(getActivity(), purchasedPackagesResponse.getGetPackaged(),this);
            mList.setAdapter(purchasedPackagesAdapter);
        }
        else {

        }

    }

    @Override
    public void onStatusClick(String id) {

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
        Bundle bundle=new Bundle();
        bundle.putString(Constant.Key_Business_Name,name);
        bundle.putString(Constant.Key_Mobile,contact);
        bundle.putString(Constant.Key_Email,email);
        bundle.putString(Constant.Key_Source,source);
        ((AwsHomeActivity)getActivity()).addFragment(new LeadCreateFragment(),"LeadCreateFragment",true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }
}
