package autroid.business.view.fragment.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
//import com.qiscus.sdk.Qiscus;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.ProfileCarStockAdapter;
import autroid.business.adapter.ProfileGalleryAdapter;
import autroid.business.adapter.ProfileOfferAdapter;
import autroid.business.adapter.ProfileProductStockAdapter;
import autroid.business.adapter.ProfileReviewRatingAdapter;
import autroid.business.adapter.ShowroomGalleryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.GalleryImageClickCallback;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.bean.ThumbnailBE;
import autroid.business.model.bean.TimingsBE;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.model.request.SaveBusinessRequest;
import autroid.business.model.response.BusinessProfileResponse;
import autroid.business.model.response.SaveBusinessResponse;
import autroid.business.model.response.ShowroomCarsResponse;
import autroid.business.model.response.ShowroomOfferResponse;
import autroid.business.model.response.ShowroomProductResponse;
import autroid.business.model.response.ShowroomReviewResponse;
import autroid.business.presenter.ShowroomProfilePresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.GridSpacingItemDecoration;
import autroid.business.utils.Utility;
import autroid.business.view.activity.GalleryActivity;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.RealmGalleryActivity;
import autroid.business.view.fragment.AddShowroomReviewFragment;
import autroid.business.view.fragment.EditPicFragment;
import autroid.business.view.fragment.SettingsFragment;
import autroid.business.view.fragment.WebViewFragment;
import autroid.business.view.fragment.carsales.UsedCarDetailFragment;
import autroid.business.view.fragment.profile.profile_tab.ButtomTabAdapter;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.ConstantsProfile;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.GallaryFragment;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.PrefrenceMangerProfile;
import io.realm.Realm;
import io.realm.RealmList;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 */

public class ShowroomFragment extends Fragment implements View.OnClickListener, OnClickBusinessCallback, OnClickCallBack, GalleryImageClickCallback {
    TextView mShowroomName, mTotalRating, mCareagerRating, mShowroomUrl, mShowroomCategory, mShowroomLocation, mShowroomJoined, mShowroomContact, mDescription, mAssistance;
    TextView mMon, mTue, mWed, mThr, mFri, mSat, mSun;
    ImageView mCover;
    RatingBar mRatings;
    LinearLayout mLayoutVerified;
    ShowroomProfilePresenter mPresenter;
    LinearLayout mMainLayout;
    TextView mRate;

    String userId;
    PreferenceManager preferenceManager;
    RecyclerView recListGallery;

    RecyclerView recListCars;
    RecyclerView recListProduct;
    RecyclerView recListOffers;
    RecyclerView recListReviewRating;


    ProfileCarStockAdapter mProfileCarStockAdapter;
    ProfileProductStockAdapter mProfileProductStockAdapter;
    ProfileReviewRatingAdapter mProfileReviewRatingAdapter;
    ShowroomGalleryAdapter mProfileGalleryAdapter;
    ProfileOfferAdapter mProfileOfferAdapter;

    TextView mAllCars, mAllProducts, mAllOffers, mAllReviews;

    RealmController mRealmController;
    Realm mRealm;

    String showroomName, showroomId;
    LinearLayout mNavigate, mShare, mChat, mSave;
    LinearLayout mLayoutCars, mLayoutProducts, mLayoutOffers, mLayoutReviews;
    LinearLayout mLayoutGallery;

    LinearLayout llEdit;
    AppCompatImageView mImgSave;
    TextView mTxtSave;

    BusinessProfileResponse showroomProfileResponse;

    AppCompatImageView mImgYoutube, mImgTwitter, mImgLinedin, mImgInstagram, mImgGoogle, mImgFB, mImgWeb;


    TabLayout tabLayoutBottom;
    TabItem tabItem1, tabItem2, tabItem3,tabItem4;
    ViewPager viewPagerBottom;
    ButtomTabAdapter pageAdapter;

    PrefrenceMangerProfile mangerProfile;


    public ShowroomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_showroom, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
        GlobalBus.getBus().register( this );
        this.mRealm = RealmController.with( getActivity() ).getRealm();
        mRealmController = RealmController.getInstance();


        mShowroomName = (TextView) view.findViewById( R.id.showroom_name );
        mTotalRating = (TextView) view.findViewById( R.id.total_ratings );
        mCareagerRating = (TextView) view.findViewById( R.id.careager_rating );
        mShowroomUrl = (TextView) view.findViewById( R.id.careager_url );
        mShowroomCategory = (TextView) view.findViewById( R.id.showroom_category );
        mShowroomLocation = (TextView) view.findViewById( R.id.showroom_location );
        mShowroomJoined = (TextView) view.findViewById( R.id.showroom_joined );
        mShowroomContact = (TextView) view.findViewById( R.id.showroom_contact );
        mDescription = (TextView) view.findViewById( R.id.showroom_description );
        mAssistance = (TextView) view.findViewById( R.id.assistance );
        llEdit = (LinearLayout) view.findViewById( R.id.ll_edit );
        llEdit.setOnClickListener( this );

        mLayoutGallery = view.findViewById( R.id.layout_gallery );
        mLayoutCars = view.findViewById( R.id.layout_cars );
        mLayoutProducts = view.findViewById( R.id.layout_products );
        mLayoutOffers = view.findViewById( R.id.layout_offers );
        mLayoutReviews = view.findViewById( R.id.layout_reviews );

        mImgSave = view.findViewById( R.id.ic_save );
        mTxtSave = view.findViewById( R.id.txt_save );

        mNavigate = view.findViewById( R.id.showroom_navigate );
        mNavigate.setOnClickListener( this );
        mShare = view.findViewById( R.id.showroom_share );
        mShare.setOnClickListener( this );
        mSave = view.findViewById( R.id.showroom_save );
        mSave.setOnClickListener( this );
        mChat = view.findViewById( R.id.showroom_chat );
        mChat.setOnClickListener( this );

        mImgYoutube = view.findViewById( R.id.img_youtube );
        mImgTwitter = view.findViewById( R.id.img_twitter );
        mImgLinedin = view.findViewById( R.id.img_linkedin );
        mImgInstagram = view.findViewById( R.id.img_instagram );
        mImgGoogle = view.findViewById( R.id.img_google_plus );
        mImgFB = view.findViewById( R.id.img_fb );
        mImgWeb = view.findViewById( R.id.img_web );


        tabLayoutBottom = view.findViewById( R.id.tab_buttom );
        tabItem1 = view.findViewById( R.id.tab1 );
        tabItem2 = view.findViewById( R.id.tab2 );
        tabItem3 = view.findViewById( R.id.tab3 );
        tabItem4 = view.findViewById( R.id.tab4 );
        viewPagerBottom = view.findViewById( R.id.buttomTabViewpager );

        pageAdapter = new ButtomTabAdapter(getChildFragmentManager(), tabLayoutBottom.getTabCount() );
        viewPagerBottom.setAdapter( pageAdapter );


        tabLayoutBottom.setOnTabSelectedListener( new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerBottom.setCurrentItem( tab.getPosition() );

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3 ) {
                   pageAdapter.notifyDataSetChanged();
                   updateData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                 updateData();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                updateData();
            }
        } );

        viewPagerBottom.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayoutBottom ) );

        ExpandableTextView textViewExpand1 = view.findViewById( R.id.expand_text_view1 );
        ExpandableTextView textViewExpand2 = view.findViewById( R.id.expand_text_view2 );
        ExpandableTextView textViewExpand3 = view.findViewById( R.id.expand_text_view3 );
        ExpandableTextView textViewExpand4 = view.findViewById( R.id.expand_text_view4 );
        ExpandableTextView textViewExpand5 = view.findViewById( R.id.expand_text_view5 );
        ExpandableTextView textViewExpand6 = view.findViewById( R.id.expand_text_view6 );


        textViewExpand1.setText( "Category" + '\n' + "Service Station (Multi-brand)Service" );
        textViewExpand2.setText( "Location" + '\n' + "18/1, NH48, Behind Grace Tyota (Near Mercedes-Benz, Sector 35, Gurgaon, Haryana 122004, India" );
        textViewExpand3.setText( "Profile Link" + '\n' + "CarEager.com/carEagerGurgaon" );
        textViewExpand4.setText( "Joined" + '\n' + "Nov 28, 2018" );
        textViewExpand5.setText( "Contact" + '\n' + "18008434300" );
        textViewExpand6.setText( "Description" + '\n' + "World-class services & certified cars at the lowest prices." +
                " CarEager seamlessly merges the physical and digital worlds of the auto industry." );

        mImgWeb.setOnClickListener( this );
        mImgYoutube.setOnClickListener( this );
        mImgTwitter.setOnClickListener( this );
        mImgLinedin.setOnClickListener( this );
        mImgInstagram.setOnClickListener( this );
        mImgGoogle.setOnClickListener( this );
        mImgFB.setOnClickListener( this );

        mAllCars = view.findViewById( R.id.all_cars );
        mAllProducts = view.findViewById( R.id.all_products );
        mAllOffers = view.findViewById( R.id.all_offers );
        mAllReviews = view.findViewById( R.id.all_reviews );

        mAllCars.setOnClickListener( this );
        mAllProducts.setOnClickListener( this );
        mAllOffers.setOnClickListener( this );
        mAllReviews.setOnClickListener( this );


        mRate = view.findViewById( R.id.rate_showroom );
        mRate.setOnClickListener( this );

        mMon = (TextView) view.findViewById( R.id.day_mon );
        mTue = (TextView) view.findViewById( R.id.day_tue );
        mWed = (TextView) view.findViewById( R.id.day_wed );
        mThr = (TextView) view.findViewById( R.id.day_thrus );
        mFri = (TextView) view.findViewById( R.id.day_fri );
        mSat = (TextView) view.findViewById( R.id.day_sat );
        mSun = (TextView) view.findViewById( R.id.day_sun );

        mRatings = (RatingBar) view.findViewById( R.id.careager_ratingbar );


        mCover = (ImageView) view.findViewById( R.id.showroom_cover );
        mCover.setOnClickListener( this );

        mMainLayout = (LinearLayout) view.findViewById( R.id.main_layout );
        mPresenter = new ShowroomProfilePresenter( this, mMainLayout );

        preferenceManager = PreferenceManager.getInstance();
        mangerProfile= new PrefrenceMangerProfile( getActivity() );


        int spacing = (int) Utility.convertDpToPixel( (float) 8, getContext() );

        recListCars = (RecyclerView) view.findViewById( R.id.car_stock_list );
        LinearLayoutManager llmCars;
        llmCars = new LinearLayoutManager( getActivity() );
        llmCars.setOrientation( LinearLayoutManager.HORIZONTAL );
        recListCars.setLayoutManager( llmCars );
        recListCars.setHasFixedSize( true );
        recListCars.addOnScrollListener( new CenterScrollListener() );
        SnapHelper helperCars = new GravitySnapHelper( Gravity.START );
        helperCars.attachToRecyclerView( recListCars );


        recListProduct = (RecyclerView) view.findViewById( R.id.product_stock_list );
        LinearLayoutManager llmProduct;
        llmProduct = new LinearLayoutManager( getActivity() );
        llmProduct.setOrientation( LinearLayoutManager.HORIZONTAL );
        recListProduct.setLayoutManager( llmProduct );
        recListProduct.setHasFixedSize( true );
        recListProduct.addOnScrollListener( new CenterScrollListener() );
        SnapHelper helperProduct = new GravitySnapHelper( Gravity.START );
        helperProduct.attachToRecyclerView( recListProduct );

        recListOffers = (RecyclerView) view.findViewById( R.id.offers_list );
        LinearLayoutManager llmOffers;
        llmOffers = new LinearLayoutManager( getActivity() );
        llmOffers.setOrientation( LinearLayoutManager.HORIZONTAL );
        recListOffers.setLayoutManager( llmOffers );
        recListOffers.setHasFixedSize( true );
        recListOffers.addOnScrollListener( new CenterScrollListener() );
        SnapHelper helperOffers = new GravitySnapHelper( Gravity.START );
        helperOffers.attachToRecyclerView( recListOffers );

        recListReviewRating = (RecyclerView) view.findViewById( R.id.review_rating_list );
        LinearLayoutManager llmReviewRating;
        llmReviewRating = new LinearLayoutManager( getActivity() );
        llmReviewRating.setOrientation( LinearLayoutManager.HORIZONTAL );
        recListReviewRating.addItemDecoration( new GridSpacingItemDecoration( 2, spacing, true ) );
        recListReviewRating.setLayoutManager( llmReviewRating );
        SnapHelper helperRating = new GravitySnapHelper( Gravity.START );
        helperRating.attachToRecyclerView( recListReviewRating );


        recListGallery = (RecyclerView) view.findViewById( R.id.showroom_gallery_list );
        recListGallery.setNestedScrollingEnabled( false );
        setGridLayout();

        userId = preferenceManager.getStringPreference( getActivity(), Constant.SP_USERID );

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey( Constant.KEY_ID )) {
                String Id = bundle.getString( Constant.KEY_ID );
                getShowroom( Id );
            } else {
                mRealmController.clearShowroom();
                getShowroom( userId );
            }
        } else {
            mRealmController.clearShowroom();
            getShowroom( userId );
        }
        //mRealm.writeCopyTo();

    }

    private void setGridLayout() {
        StaggeredGridLayoutManager llm;
        int spanCount = 2; // 2 columns
        llm = new StaggeredGridLayoutManager( spanCount, StaggeredGridLayoutManager.VERTICAL );
        recListGallery.setLayoutManager( llm );

        float spacing = Utility.convertPixelsToDp( 10, getActivity() ); // 50px

        boolean includeEdge = false;

        recListGallery.addItemDecoration( new GridSpacingItemDecoration( spanCount, Math.round( spacing ), includeEdge ) );
    }


    private void getShowroom(String id) {
        mPresenter.getShowroom( id );
        mPresenter.getAllCars( id );
        mPresenter.getAllOffers( id );
        mPresenter.getAllProducts( id );
        mPresenter.getAllReviews( id );
    }
// my preference manger

    public void updateData(){
        if (showroomProfileResponse!=null){

            if (mangerProfile.getString( ConstantsProfile.KEY_ID_ )==null){
                mangerProfile.putString( ConstantsProfile.KEY_ID_,showroomProfileResponse.getResponseData().getId() );
                mangerProfile.putString( ConstantsProfile.KEY_BUSINESS_NAME_,showroomProfileResponse.getResponseData().getName() );
            }else {
                mangerProfile.updateString( ConstantsProfile.KEY_ID_,showroomProfileResponse.getResponseData().getId() );
                mangerProfile.updateString( ConstantsProfile.KEY_BUSINESS_NAME_,showroomProfileResponse.getResponseData().getName() );
            }
        }
    }

    public void onSuccess(BusinessProfileResponse showroomProfileResponse) {

        this.showroomProfileResponse = showroomProfileResponse;

        if (!showroomProfileResponse.getResponseData().getIs_chat_active()) {
            mChat.setVisibility( View.GONE );
        }

        if (showroomProfileResponse.getResponseData().getIs_bookmarked()) {
            mTxtSave.setText( getString( R.string.saved ) );
            mImgSave.setColorFilter( ContextCompat.getColor( getActivity(), R.color.red_color ), android.graphics.PorterDuff.Mode.SRC_IN );
        } else {
            mTxtSave.setText( getString( R.string.save ) );
            mImgSave.setColorFilter( ContextCompat.getColor( getActivity(), R.color.gray_color ), android.graphics.PorterDuff.Mode.SRC_IN );
        }

        if (userId.equalsIgnoreCase( showroomProfileResponse.getResponseData().getId() )) {
            mRate.setVisibility( View.GONE );
            ShowroomRealm showroomRealm = new ShowroomRealm();
            showroomRealm.setBusinessName( showroomProfileResponse.getResponseData().getName() );
            showroomRealm.setLocation( showroomProfileResponse.getResponseData().getAddress().getLocation() );
            showroomRealm.setLatitude( showroomProfileResponse.getResponseData().getCoordinates().get( 0 ) );
            showroomRealm.setLongitude( showroomProfileResponse.getResponseData().getCoordinates().get( 1 ) );
            showroomRealm.setRating( showroomProfileResponse.getResponseData().getTotal_rating() );
            showroomRealm.setCover( showroomProfileResponse.getResponseData().getAvatar_address() );
            showroomRealm.setBusinessId( showroomProfileResponse.getResponseData().getId() );


            if (showroomProfileResponse.getResponseData().getBusiness_gallery().size() > 0) {
                RealmList<MediaRealm> mediaRealms = new RealmList<>();
                for (int j = 0; j < showroomProfileResponse.getResponseData().getBusiness_gallery().size(); j++) {
                    MediaRealm mediaRealm = new MediaRealm();
                    mediaRealm.setId( showroomProfileResponse.getResponseData().getBusiness_gallery().get( j ).getId() );
                    mediaRealm.setPath( showroomProfileResponse.getResponseData().getBusiness_gallery().get( j ).getFile_address() );
                    mediaRealms.add( mediaRealm );
                }
                showroomRealm.setMedia( mediaRealms );
            }
            mRealmController.addDataToShowroom( showroomRealm );

            mProfileGalleryAdapter = new ShowroomGalleryAdapter( mRealmController.getShowroomData().getMedia(), true, userId, this );
            recListGallery.setAdapter( mProfileGalleryAdapter );

            Intent intent = new Intent();
            intent.putExtra( Constant.KEY_EVENT_ID, Constant.EVENT_COVER_IMAGE );
            intent.putExtra( Constant.KEY_IMAGES, showroomProfileResponse.getResponseData().getAvatar_address() );
            Events.SendEvent sendEvent = new Events.SendEvent( intent );
            GlobalBus.getBus().post( sendEvent );
        } else {
            llEdit.setVisibility( View.GONE );
            recListGallery.setAdapter( new ProfileGalleryAdapter( getActivity(), showroomProfileResponse.getResponseData().getBusiness_gallery(), this ) );


           RealmList<MediaRealm> mediaRealms = new RealmList<>();
//            for (int j = 0; j < showroomProfileResponse.getResponseData().getVendor_gallery().size(); j++) {
//                MediaRealm mediaRealm = new MediaRealm();
//                mediaRealm.setId(showroomProfileResponse.getResponseData().getVendor_gallery().get(j).getId());
//                mediaRealm.setPath(showroomProfileResponse.getResponseData().getVendor_gallery().get(j).getFile_address());
//                mediaRealms.add(mediaRealm);
//            }
            mRealm.copyFromRealm(mediaRealms);
            mProfileGalleryAdapter=new ShowroomGalleryAdapter(mediaRealms,true,userId,this);
            recListGallery.setAdapter(mProfileGalleryAdapter);
        }

        if (showroomProfileResponse.getResponseData().getBusiness_gallery().size() == 0) {
            mLayoutGallery.setVisibility( View.GONE );
        } else {
//            mLayoutGallery.setVisibility( View.VISIBLE );
        }

        showroomName = showroomProfileResponse.getResponseData().getName();
        showroomId = showroomProfileResponse.getResponseData().getId();

        mShowroomName.setText( showroomProfileResponse.getResponseData().getName() );
        mCareagerRating.setText( showroomProfileResponse.getResponseData().getCareager_rating() + "/" + 5 );
        mRatings.setRating( showroomProfileResponse.getResponseData().getTotal_rating() );
        mTotalRating.setText( showroomProfileResponse.getResponseData().getTotal_rating() + "" );

        mShowroomUrl.setText( "CarEager.com/" + showroomProfileResponse.getResponseData().getUsername() );
        mShowroomContact.setText( showroomProfileResponse.getResponseData().getContact_no() );
        mShowroomCategory.setText( showroomProfileResponse.getResponseData().getBusiness_info().getCategory() );
        mShowroomLocation.setText( showroomProfileResponse.getResponseData().getAddress().getLocation() );
        mShowroomJoined.setText( showroomProfileResponse.getResponseData().getJoined() );
        mDescription.setText( showroomProfileResponse.getResponseData().getOptional_info().getOverview() );

        if (showroomProfileResponse.getResponseData().getBusiness_info().getAssistance())
            mAssistance.setText( "24 X 7 Assistance: " + "Yes" );
        else
            mAssistance.setText( "24 X 7 Assistance: " + "No" );


        Picasso.with( getActivity() ).load( showroomProfileResponse.getResponseData().getAvatar_address() ).transform( new BlurTransformation( getActivity(), 25, 1 ) ).placeholder( R.drawable.placeholder_big ).into( mCover );


//         recListGallery.setAdapter(new FeedsImagesAdapter(realmController.getUserDetail().get(0).getGallery(),true,userProfileResponse.getResponseData().get(0).getId(),this));

        if (showroomProfileResponse.getResponseData().getTiming() != null)
            if (showroomProfileResponse.getResponseData().getTiming().size() > 0)
                setTimings( showroomProfileResponse.getResponseData().getTiming() );


    }

    public void refreshList() {
        userId = preferenceManager.getStringPreference( getActivity(), Constant.SP_USERID );
        getShowroom( userId );
    }


    @Override
    public void onResume() {
        super.onResume();
//        getActivity().registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        // getActivity().unregisterReceiver(mReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_edit:
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                ((AwsHomeActivity) getActivity()).addFragment( new SettingsFragment(), "SettingsFragment", true, false, null, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            case R.id.showroom_chat:
                try {
//                 Qiscus.buildChatWith(showroomId) //here we use email as userID. But you can make it whatever you want.
//                         .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                             @Override
//                             public void onSuccess(Intent intent) {
//                                 startActivity(intent);
//                             }
//
//                             @Override
//                             public void onError(Throwable throwable) {
//                                 //do anything if error occurs
//                                 throwable.printStackTrace();
//                                 throwable.getLocalizedMessage();
//                             }
//                         });

                    // startActivity(new Intent(getActivity(),SettingsFragment.class));
                } catch (Exception e) {

                }
                break;
            case R.id.showroom_navigate:
                String uri = "http://maps.google.com/maps?daddr=" + showroomProfileResponse.getResponseData().getCoordinates().get( 0 ) + "," + showroomProfileResponse.getResponseData().getCoordinates().get( 1 ) + " (" + showroomProfileResponse.getResponseData().getName() + ")";
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( uri ) );
                intent.setPackage( "com.google.android.apps.maps" );
                startActivity( intent );
                //startActivity(new Intent(getActivity(),SettingsFragment.class));
                break;
            case R.id.showroom_save:
                SaveBusinessRequest saveBusinessRequest = new SaveBusinessRequest();
                saveBusinessRequest.setId( showroomId );
                mPresenter.saveShowroom( saveBusinessRequest );
                break;
            case R.id.showroom_share:
                String content = "Check " + showroomName + " profile at CarEager - Integrated Automotive Ecosystem. Download the App Now! https://goo.gl/fU5Upb";

                Utility.share( getActivity(), content );
                // startActivity(new Intent(getActivity(),SettingsFragment.class));
                break;
            case R.id.rate_showroom:
                Bundle bundle = new Bundle();
                bundle.putString( Constant.KEY_ID, showroomId );
                bundle.putString( Constant.Key_Business_Name, showroomName );
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                ((AwsHomeActivity) getActivity()).addFragment( new AddShowroomReviewFragment(), "AddShowroomReviewFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            case R.id.img_youtube: {

                if (showroomProfileResponse.getResponseData().getSocialite().getYoutube().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getYoutube() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.img_twitter: {
                if (showroomProfileResponse.getResponseData().getSocialite().getTwitter().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getTwitter() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.img_linkedin: {
                if (showroomProfileResponse.getResponseData().getSocialite().getLinkedin().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getLinkedin() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }

            case R.id.img_instagram: {
                if (showroomProfileResponse.getResponseData().getSocialite().getInstagram().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getInstagram() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.img_google_plus: {
                if (showroomProfileResponse.getResponseData().getSocialite().getGoogleplus().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getGoogleplus() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.img_fb: {
                if (showroomProfileResponse.getResponseData().getSocialite().getFacebook().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getFacebook() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.img_web: {
                if (showroomProfileResponse.getResponseData().getSocialite().getWebsite().length() == 0) {
                    Utility.showResponseMessage( mMainLayout, getString( R.string.link_not_updated_business ) );
                    break;
                }
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_TYPE, showroomProfileResponse.getResponseData().getSocialite().getWebsite() );
                ((AwsHomeActivity) getActivity()).addFragment( WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.all_cars: {
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_ID, showroomProfileResponse.getResponseData().getId() );
                bundle1.putString( Constant.Key_Business_Name, showroomProfileResponse.getResponseData().getName() );
                ((AwsHomeActivity) getActivity()).addFragment( new ShowroomCarsFragment(), "ShowroomCarsFragment", true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.all_products: {
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_ID, showroomProfileResponse.getResponseData().getId() );
                bundle1.putString( Constant.Key_Business_Name, showroomProfileResponse.getResponseData().getName() );
                ((AwsHomeActivity) getActivity()).addFragment( new ShowroomProductsFragment(), "ShowroomProductsFragment", true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.all_offers: {
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_ID, showroomProfileResponse.getResponseData().getId() );
                bundle1.putString( Constant.Key_Business_Name, showroomProfileResponse.getResponseData().getName() );
                ((AwsHomeActivity) getActivity()).addFragment( new ShowroomOffersFragment(), "ShowroomOffersFragment", true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;

            }
            case R.id.all_reviews: {
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_ID, showroomProfileResponse.getResponseData().getId() );
                bundle1.putString( Constant.Key_Business_Name, showroomProfileResponse.getResponseData().getName() );
                ((AwsHomeActivity) getActivity()).addFragment( new ShowroomReviewsFragment(), "ShowroomReviewsFragment", true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;
            }
            case R.id.showroom_cover: {
                EditPicFragment editPicFragment = new EditPicFragment();
                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_IMAGES, showroomProfileResponse.getResponseData().getAvatar_address() );
                bundle1.putString( Constant.KEY_TYPE, "cover" );
                editPicFragment.setArguments( bundle1 );
                editPicFragment.show( getChildFragmentManager(), "EditPicFragment" );

                break;
            }

        }

    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_PROFILE) {
            mShowroomName.setText( intent.getStringExtra( Constant.Key_Business_Name ) );
            mDescription.setText( intent.getStringExtra( Constant.Key_Business_overview ) );
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_COVER) {
            String url = intent.getStringExtra( Constant.KEY_IMAGES );
            Picasso.with( getActivity() ).load( url ).transform( new BlurTransformation( getActivity(), 25, 1 ) ).placeholder( R.drawable.placeholder_big ).into( mCover );
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_TIMING) {
            ArrayList<TimingsBE> timingsBES = (ArrayList<TimingsBE>) intent.getSerializableExtra( Constant.KEY_TYPE );
            setTimings( timingsBES );
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_GALLERY) {
//            mProfileGalleryAdapter.notifyDataSetChanged();
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_ADDRESS) {
            mShowroomLocation.setText( intent.getStringExtra( Constant.Key_Business_address ) );
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_CHANGE_ACCOUNT) {
            userId = preferenceManager.getStringPreference( getActivity(), Constant.SP_USERID );
            mPresenter.getShowroom( userId );
        }
    }

    private void setTimings(ArrayList<TimingsBE> timingsBES) {
        for (int i = 0; i < timingsBES.size(); i++) {
            String strTimings = timingsBES.get( i ).getDay() + " - " + timingsBES.get( i ).getOpen() + "-" + timingsBES.get( i ).getClose();
            switch (i) {
                case 0:
                    if (timingsBES.get( i ).isIs_closed())
                        mMon.setText( "Mon - Closed" );
                    else
                        mMon.setText( strTimings );
                    break;
                case 1:
                    if (timingsBES.get( i ).isIs_closed())
                        mTue.setText( "Tue - Closed" );
                    else
                        mTue.setText( strTimings );
                    break;
                case 2:
                    if (timingsBES.get( i ).isIs_closed())
                        mWed.setText( "Wed - Closed" );
                    else
                        mWed.setText( strTimings );
                    break;
                case 3:
                    if (timingsBES.get( i ).isIs_closed())
                        mThr.setText( "Thr - Closed" );
                    else
                        mThr.setText( strTimings );
                    break;
                case 4:
                    if (timingsBES.get( i ).isIs_closed())
                        mFri.setText( "Fri - Closed" );
                    else
                        mFri.setText( strTimings );
                    break;
                case 5:
                    if (timingsBES.get( i ).isIs_closed())
                        mSat.setText( "Sat - Closed" );
                    else
                        mSat.setText( strTimings );
                    break;
                case 6:
                    if (timingsBES.get( i ).isIs_closed())
                        mSun.setText( "Sun - Closed" );
                    else
                        mSun.setText( strTimings );
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister( this );
    }

    @Override
    public void onBusinessClick(String id) {
        Intent intent = new Intent( getActivity(), RealmGalleryActivity.class );

        intent.putExtra( Constant.KEY_TYPE, Constant.VALUE_SHOWROOM );
        intent.putExtra( Constant.KEY_ID, id );
        startActivity( intent );
    }

    public void onSaveSuccess(SaveBusinessResponse saveBusinessResponse) {
        Utility.showResponseMessage( mMainLayout, saveBusinessResponse.getResponseMessage() );

        if (saveBusinessResponse.getGetSaveResponse().isIs_bookmarked()) {
            mTxtSave.setText( getString( R.string.saved ) );
            mImgSave.setColorFilter( ContextCompat.getColor( getActivity(), R.color.red_color ), android.graphics.PorterDuff.Mode.SRC_IN );
        } else {
            mTxtSave.setText( getString( R.string.save ) );
            mImgSave.setColorFilter( ContextCompat.getColor( getActivity(), R.color.gray_color ), android.graphics.PorterDuff.Mode.SRC_IN );
        }
    }

    @Override
    public void onImageClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString( Constant.KEY_ID, id );
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        bundle.putBoolean( Constant.KEY_IS_VIEW, true );
        ((AwsHomeActivity) getActivity()).addFragment( new UsedCarDetailFragment(), FragmentTags.FRAGMENT_CAR_STOCK_Detail, true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId );
    }

    @Override
    public void onPublishUnPublishClick(String id) {

    }

    @Override
    public void onEditButtonClick(String id, String status) {

    }

    @Override
    public void onShareButtonClick(String id) {

    }

    @Override
    public void onTitleClick(String id) {

    }

    public void onCarSuccess(ShowroomCarsResponse showroomCarsResponse) {

        if (showroomCarsResponse.getCarDetailBES().size() > 0) {
//            mLayoutCars.setVisibility( View.VISIBLE );
            mProfileCarStockAdapter = new ProfileCarStockAdapter( getActivity(), showroomCarsResponse.getCarDetailBES(), false, this );
            recListCars.setAdapter( mProfileCarStockAdapter );

            if (showroomCarsResponse.getCarDetailBES().size() > 0) {
//                mAllCars.setVisibility( View.VISIBLE );
            }
        } else {
//            mLayoutCars.setVisibility( View.GONE );
        }
    }

    public void onOfferSuccess(ShowroomOfferResponse showroomOfferResponse) {

        if (showroomOfferResponse.getOfferBES().size() > 0) {
//            mLayoutOffers.setVisibility( View.VISIBLE );
            mProfileOfferAdapter = new ProfileOfferAdapter( getActivity(), showroomOfferResponse.getOfferBES(), false );
            recListOffers.setAdapter( mProfileOfferAdapter );

            if (showroomOfferResponse.getOfferBES().size() > 0) {
//                mAllOffers.setVisibility( View.VISIBLE );
            }
        } else {
//            mLayoutOffers.setVisibility( View.GONE );
        }

    }

    public void onProductSuccess(ShowroomProductResponse showroomProductResponse) {


        if (showroomProductResponse.getProducts().size() > 0) {
//            mLayoutProducts.setVisibility( View.VISIBLE );
            mProfileProductStockAdapter = new ProfileProductStockAdapter( getActivity(), showroomProductResponse.getProducts(), false );
            recListProduct.setAdapter( mProfileProductStockAdapter );

            if (showroomProductResponse.getProducts().size() > 0) {
//                mAllProducts.setVisibility( View.VISIBLE );
            }
        } else {
//            mLayoutProducts.setVisibility( View.GONE );
        }


    }

    public void onReviewsSuccess(ShowroomReviewResponse showroomReviewResponse) {
        if (showroomReviewResponse.getRatingReviewBES().size() > 0) {
//            mLayoutReviews.setVisibility( View.VISIBLE );
            mProfileReviewRatingAdapter = new ProfileReviewRatingAdapter( getActivity(), showroomReviewResponse.getRatingReviewBES(), false );
            recListReviewRating.setAdapter( mProfileReviewRatingAdapter );
            if (showroomReviewResponse.getRatingReviewBES().size() > 0) {
//                mAllReviews.setVisibility( View.VISIBLE );
            }

        } else {
//            mLayoutReviews.setVisibility( View.GONE );
        }
    }

    @Override
    public void onGalleryClick(ArrayList<ThumbnailBE> mImages) {
        if (mImages.size() > 0) {
            Intent intent = new Intent( getActivity(), GallaryFragment.class );
            intent.putExtra( Constant.KEY_IMAGES, mImages );
            startActivity( intent );
        }
    }
}
