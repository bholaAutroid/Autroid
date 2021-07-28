package autroid.business.view.fragment;


import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.OfferAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.realm.OffersRealm;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.OffersResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.presenter.OffersPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersFragment extends Fragment implements View.OnClickListener,OnClickCallBack {


    RecyclerView recList;

    RelativeLayout mMainLayout;
    OffersPresenter mPresenter;
    OfferAdapter offerAdapter;
    FloatingActionButton mAddButton;
    OffersResponse mOffersResponse;
    TextView mSaved,mPublished;

    private Realm realm;
    RealmController realmController;
    SwipeRefreshLayout mSwipeRefreshLayout;

    EndlessScrollListener mScrollListener = null;

    int pageNo=0;

    FirebaseAnalytics mFirebaseAnalytics;

    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Offers",null);

        realmController.clearOffers();

        recList= (RecyclerView) view.findViewById(R.id.offers_list);
        mAddButton= (FloatingActionButton) view.findViewById(R.id.fab_add);
        mAddButton.setOnClickListener(this);

        mSaved= (TextView) view.findViewById(R.id.txt_saved);
        mPublished= (TextView) view.findViewById(R.id.txt_published);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new OffersPresenter(this,mMainLayout);


        getOffers(pageNo);

        mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                realmController.clearOffers();
                pageNo=0;
                getOffers(pageNo);
            }
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                pageNo=page;
                getOffers(pageNo);

            }
        };

        recList.addOnScrollListener(mScrollListener);


       /* recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onImageClick(View view, int position) {


                    }

                }));*/
    }

    private void getOffers(int page){
        mPresenter.getOffers(page);
    }

    public void onSuccess(OffersResponse offersResponse){
        mOffersResponse=offersResponse;
        mSwipeRefreshLayout.setRefreshing(false);
        mSaved.setText(offersResponse.getResponseData().getTotal());
        mPublished.setText(offersResponse.getResponseData().getPublished());

        for(int i=0;i<offersResponse.getResponseData().getOffers().size();i++){
            OffersRealm offersRealm=new OffersRealm();

            offersRealm.setDescription(offersResponse.getResponseData().getOffers().get(i).getDescription());
            offersRealm.setOfferId(offersResponse.getResponseData().getOffers().get(i).getId());
            offersRealm.setTitle(offersResponse.getResponseData().getOffers().get(i).getOffer());
            offersRealm.setOfferImg(offersResponse.getResponseData().getOffers().get(i).getFile_address());
            offersRealm.setValidity(offersResponse.getResponseData().getOffers().get(i).getValid_till());
            offersRealm.setPublish(offersResponse.getResponseData().getOffers().get(i).isPublish());
            realm.beginTransaction();
            realm.copyToRealm(offersRealm);
            realm.commitTransaction();
        }

        if(pageNo==0) {
            offerAdapter = new OfferAdapter(realmController.getOffers(), true, this);
            recList.setAdapter(offerAdapter);
        }
        else {
            offerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add:
                ((AwsHomeActivity)getActivity()).makeDrawerVisible();
                ((AwsHomeActivity)getActivity()).addFragment(AddOfferFragment.newInstance(), FragmentTags.FRAGMENT_ADD_OFFERS,true,false,null,((AwsHomeActivity) getActivity()).currentFrameId);
                break;
        }
    }

    @Override
    public void onImageClick(String id) {
        //showDialog(mOffersResponse.getResponseData().getOffers().get(position).getDescription());
    }

    @Override
    public void onPublishUnPublishClick(String id) {
        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        mPresenter.publishOffer(publishUnpublishRequest,id);

    }

    @Override
    public void onEditButtonClick(String id,String status) {
        Bundle bundle=new Bundle();
      //  OfferBE offerBE=mOffersResponse.getResponseData().getOffers().get(position);
        bundle.putSerializable(Constant.KEY_ID,id);
        bundle.putBoolean(Constant.KEY_IS_OFFER_EDITABLE,true);
        ((AwsHomeActivity)getActivity()).makeDrawerVisible();
        ((AwsHomeActivity)getActivity()).addFragment(AddOfferFragment.newInstance(), FragmentTags.FRAGMENT_ADD_OFFERS,true,false,bundle,((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onShareButtonClick(String id) {
       Utility.share(getActivity(),"");
    }

    @Override
    public void onTitleClick(String id) {

    }



    @Override
    public void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
      //getActivity().unregisterReceiver(mReceiver);
        GlobalBus.getBus().unregister(this);

    }

    private void showDialog(String description){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("")
                .setMessage(description)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    public void publishUnpublishResponse(PublishResponse publishResponse,String id){

        Bundle params = new Bundle();
        params.putBoolean("publish_offer", publishResponse.getGetPublishResponse().isPublish());
        mFirebaseAnalytics.logEvent("publish_offer", params);

        Utility.showResponseMessage(mMainLayout,publishResponse.getResponseMessage());
        mPublished.setText(publishResponse.getGetPublishResponse().getPublished()+"");
        mSaved.setText(publishResponse.getGetPublishResponse().getTotal()+"");
        realmController.updateOfferPublishStatus(id,publishResponse.getGetPublishResponse().isPublish());
    }



    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_REFRESH_OFFER) {
            offerAdapter.notifyDataSetChanged();
        }
    }
}
