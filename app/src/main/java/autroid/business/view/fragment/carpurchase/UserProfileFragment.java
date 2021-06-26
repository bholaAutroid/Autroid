package autroid.business.view.fragment.carpurchase;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

//import com.qiscus.sdk.Qiscus;
import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.model.response.UserProfileResponse;
import autroid.business.presenter.UserProfilePresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.EditPicFragment;
import autroid.business.view.fragment.WebViewFragment;
import jp.wasabeef.picasso.transformations.BlurTransformation;


/**
 * A simple {@link Fragment} subclass.
 */

public class UserProfileFragment extends Fragment implements View.OnClickListener {

    LinearLayout mMainLayout;
    UserProfilePresenter mPresenter;

    String userID;
    TextView mProfileView,mFollowers,mFollowing,mTotalClicks;
    TextView mName,mUsername,mDescription,mMenbershipLevel,mCareagerCoins,mJoinedDate,mLOcation;
    ImageView mProfilePic;
    ImageView mCoverPic;
    AppCompatImageView mBackButton,mChatButton;
    RelativeLayout mShareProfile;
    ViewPager mGarage;

    ProgressBar mLevelProgress;
    TextView mLevel;

    TextView mFollow;

    UserProfileResponse mUserProfileResponse;
    AppCompatImageView mImgYoutube,mImgTwitter,mImgLinedin,mImgInstagram,mImgGoogle,mImgFB,mImgWeb;


    View mLineDes;
    ScrollView mNestedScrollView;
    LinearLayout mLLGarage;

    private boolean isLoogedId;
    ImageView mVerified;
    RelativeLayout mLLUser;

    String shareContent="CarEager Xpress - India's first, fully Integrated Automotive Ecosystem. Download the App Now! and get exciting launch offers on your car service.";

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mMainLayout=view.findViewById(R.id.main_layout);
        mPresenter=new UserProfilePresenter(this,mMainLayout);

        mProfileView=view.findViewById(R.id.profile_view);
        mFollowers=view.findViewById(R.id.followers);
        mFollowing=view.findViewById(R.id.following);
        mTotalClicks=view.findViewById(R.id.total_clicks);


        mLLGarage=view.findViewById(R.id.ll_garage);

        mName=view.findViewById(R.id.name_txt);
        mUsername=view.findViewById(R.id.username_txt);
        mDescription=view.findViewById(R.id.description_txt);
        mMenbershipLevel=view.findViewById(R.id.membership_level);
        mCareagerCoins=view.findViewById(R.id.coins);
        mJoinedDate=view.findViewById(R.id.joined);
        mLOcation=view.findViewById(R.id.location_text);
        mShareProfile=view.findViewById(R.id.share_profile);
        mShareProfile.setOnClickListener(this);
        mChatButton=view.findViewById(R.id.chat_profile);
        mChatButton.setOnClickListener(this);

        mLLUser=view.findViewById(R.id.ll_level);

        mVerified=view.findViewById(R.id.img_verified);

        mBackButton=view.findViewById(R.id.img_back);
        mBackButton.setOnClickListener(this);
        mLineDes=view.findViewById(R.id.line_decription);


        mImgYoutube=view.findViewById(R.id.img_youtube);
        mImgTwitter=view.findViewById(R.id.img_twitter);
        mImgLinedin=view.findViewById(R.id.img_linkedin);
        mImgInstagram=view.findViewById(R.id.img_instagram);
        mImgGoogle=view.findViewById(R.id.img_google_plus);
        mImgFB=view.findViewById(R.id.img_fb);
        mImgWeb=view.findViewById(R.id.img_web);

        mImgWeb.setOnClickListener(this);
        mImgYoutube.setOnClickListener(this);
        mImgTwitter.setOnClickListener(this);
        mImgLinedin.setOnClickListener(this);
        mImgInstagram.setOnClickListener(this);
        mImgGoogle.setOnClickListener(this);
        mImgFB.setOnClickListener(this);

        mNestedScrollView=view.findViewById(R.id.nestedScroll);



        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
        int columns = Math.round(dpWidth/50);
       // mBrandList.setLayoutManager(new GridLayoutManager(getActivity(), columns));
        int spanCount = 3; // 3 columns
        int spacingBrand =5; // 50px
        boolean includeEdge = true;
       // mBrandList.addItemDecoration(new GridSpacingItemDecoration(columns, spacingBrand, includeEdge));


        mLevelProgress=view.findViewById(R.id.circle_progress_bar);
        mLevel=view.findViewById(R.id.level);

        mCoverPic=view.findViewById(R.id.profile_cover);
        mProfilePic=view.findViewById(R.id.user_img);

        mCoverPic.setOnClickListener(this);
        mProfilePic.setOnClickListener(this);

        mFollow=view.findViewById(R.id.btn_follow);
        mFollow.setOnClickListener(this);



       /* mSwipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                randomText=Utility.randomText();
                mPresenter.getProfileInfo(userID);
                mPresenter.getCarStock(userID);
            }
        });*/



       /* LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getActivity());
        llmGallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        int spacing=(int) Utility.convertDpToPixel((float)8,getContext());
        recListGallery.addItemDecoration(new GridSpacingItemDecoration(2,spacing,true));
        recListGallery.setLayoutManager(llmGallery);*/



        Bundle bundle=getArguments();
        if(bundle!=null){
            userID=bundle.getString(Constant.KEY_ID);
            mBackButton.setVisibility(View.VISIBLE);

        }

      //  realmController.deleteUserDetail(isLoogedId);
       // userID=preferenceManager.getStringPreference(getContext(), Constant.SP_USERID);
        mPresenter.getProfileInfo(userID);



        int screenWidth=Utility.getWindowWidth(getActivity());

       /* mScrollListener = new EndlessScrollListener(llmPost) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mPresenter.getFeeds(feedType,pageNo,Boolean.FALSE,userID);
            }
        };

        recListPost.addOnScrollListener(mScrollListener);*/



    }




    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser){
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
           // realmController.deleteUserDetail(randomText);
            randomText=Utility.randomText();
            mPresenter.getProfileInfo(userID);
            mPresenter.getCarStock(userID);
            Log.e("Visible hint","in visibleHint ()");
            //add your method to set adapter to listview here by passing (getActivity().getApplicationContext()) parameter for context
        }
    }*/

    public void onSuccess(UserProfileResponse userProfileResponse){
        try {
            //mSwipeRefreshLayout.setRefreshing(false);
            this.mUserProfileResponse = userProfileResponse;
            mFollowers.setText(userProfileResponse.getResponseData().getFollowers() + "");
            mFollowing.setText(userProfileResponse.getResponseData().getFollowings() + "");
            mProfileView.setText(userProfileResponse.getResponseData().getTotalViews() + "");
            mTotalClicks.setText("0");
            mName.setText(userProfileResponse.getResponseData().getName());
            mUsername.setText("@" + userProfileResponse.getResponseData().getUsername());


            if (userProfileResponse.getResponseData().getOptional_info().getOverview() != null)
                if (userProfileResponse.getResponseData().getOptional_info().getOverview().length() > 0) {
                    mDescription.setVisibility(View.VISIBLE);
                    mLineDes.setVisibility(View.VISIBLE);
                    mDescription.setText(userProfileResponse.getResponseData().getOptional_info().getOverview());

                } else {
                    mDescription.setVisibility(View.GONE);
                    mLineDes.setVisibility(View.GONE);
                }

                mMenbershipLevel.setText(Html.fromHtml("Membership: <b>Level " + userProfileResponse.getResponseData().getPoints().getLevel() + "</b>"));
                mLevel.setText("" + userProfileResponse.getResponseData().getPoints().getLevel());
                mCareagerCoins.setText(Html.fromHtml("CarEagerCash: <b>" + userProfileResponse.getResponseData().getPoints().getPoints() + "</b>"));
                mJoinedDate.setText(Html.fromHtml("Joined: <b>" + userProfileResponse.getResponseData().getJoined() + "</b>"));
                mLOcation.setText(Html.fromHtml("Lives in: <b>" + userProfileResponse.getResponseData().getAddress().getCountry() + "</b>"));
                mLevelProgress.setProgress(userProfileResponse.getResponseData().getPoints().getLevel());

       /* mMenbershipLevel.setText(Html.fromHtml("Membership: <b>Level "+userProfileResponse.getResponseData().getPoints().getLevel()+"</b>"));
        mLevel.setText(""+userProfileResponse.getResponseData().getPoints().getLevel());
        mCareagerCoins.setText(Html.fromHtml("CarEager Coins: <b>"+userProfileResponse.getResponseData().getPoints().getPoints()+"</b>"));
        mJoinedDate.setText(Html.fromHtml("Joined: <b>"+userProfileResponse.getResponseData().getJoined()+"</b>"));
        mLOcation.setText(Html.fromHtml("Lives in: <b>"+userProfileResponse.getResponseData().getAddress().getLocation()+"</b>"));
        mLevelProgress.setProgress(userProfileResponse.getResponseData().getPoints().getLevel());*/

      /*  mPosts.setText(userProfileResponse.getResponseData().getTotalPost()+"");
        mBusinessReviews.setText(userProfileResponse.getResponseData().getBusinessReview()+"");
        mCarReviews.setText(userProfileResponse.getResponseData().getTotalInGarage()+"");
        mBusinessesAdded.setText(userProfileResponse.getResponseData().getTotalBusinessAdded()+"");
        mCarsAdded.setText(userProfileResponse.getResponseData().getTotalInGarage()+"");
        mBookings.setText(userProfileResponse.getResponseData().getTotalBooking()+"");*/




            // Picasso.with(getActivity()).load(R.drawable.user_default_cover).transform(new BlurTransformation(getActivity(),25,1   )).placeholder(R.drawable.placeholder_big).into(mCoverPic);

            Picasso.with(getActivity()).load(userProfileResponse.getResponseData().getAvatar_address()).transform(new BlurTransformation(getActivity(), 25, 1)).placeholder(R.drawable.placeholder_big).into(mCoverPic);
            Picasso.with(getActivity()).load(userProfileResponse.getResponseData().getAvatar_address()).placeholder(R.drawable.ic_whatsapp).into(mProfilePic);

        /*ProfileGalleryAdapter mProfileGalleryAdapter=new ProfileGalleryAdapter(getActivity(),userProfileResponse.getResponseData().getGallery());
        recListGallery.setAdapter(mProfileGalleryAdapter);*/


      /*  if(userProfileResponse.getResponseData().getLikes().size()>0){
            ArrayList<LikedBrandBE> likedBrandBEs=new ArrayList<>();
            for(int i=0;i<userProfileResponse.getResponseData().getLikes().size();i++){
                if(userProfileResponse.getResponseData().getLikes().get(i).getIs_liked()){
                    likedBrandBEs.add(userProfileResponse.getResponseData().getLikes().get(i));
                }
            }
            mLikedMakerAdapter=new LikedMakerAdapter(getActivity(),likedBrandBEs,this,Boolean.TRUE);
            mBrandList.setAdapter(mLikedMakerAdapter);
        }*/

            //setPostData(userProfileResponse.getResponseData().getPost(),true);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        EditPicFragment imageZoomFragment =new EditPicFragment();
        switch (view.getId()){
            case R.id.profile_cover: {
                if(mUserProfileResponse!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_IMAGES, mUserProfileResponse.getResponseData().getAvatar_address());
                    bundle.putString(Constant.KEY_TYPE, "cover");
                    imageZoomFragment.setArguments(bundle);
                    imageZoomFragment.show(getChildFragmentManager(), "ImageZoomFragment");
                    // ((HomeScreenActivity) getActivity()).addFragment(new ImageZoomFragment(), FragmentTags.FRAGMENT_EDIT_PIC, true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
                }break;
            }
            case R.id.user_img:
                if(mUserProfileResponse!=null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_IMAGES, mUserProfileResponse.getResponseData().getAvatar_address());
                    bundle.putString(Constant.KEY_TYPE, "avatar");
                    imageZoomFragment.setArguments(bundle);
                    imageZoomFragment.show(getChildFragmentManager(), "ImageZoomFragment");
                    // ((HomeScreenActivity) getActivity()).addFragment(new ImageZoomFragment(), FragmentTags.FRAGMENT_EDIT_PIC,true,false,bundle,((HomeScreenActivity) getActivity()).currentFrameId);

                }break;
            case R.id.chat_profile:
                try {
//                    Qiscus.buildChatWith(userID) //here we use email as userID. But you can make it whatever you want.
//                            .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                                @Override
//                                public void onSuccess(Intent intent) {
//                                    startActivity(intent);
//                                }
//
//                                @Override
//                                public void onError(Throwable throwable) {
//                                    //do anything if error occurs
//                                    throwable.printStackTrace();
//                                }
//                            });
                }catch (NullPointerException e){

                }catch (Exception e){

                }
                break;
            case R.id.img_back:
                getActivity().onBackPressed();
                break;
            case R.id.img_youtube: {
                if(mUserProfileResponse==null)
                    break;

                if(mUserProfileResponse.getResponseData().getSocialite().getYoutube().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }

                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getYoutube());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_twitter: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getTwitter().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getTwitter());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_linkedin: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getLinkedin().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getLinkedin());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_instagram: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getInstagram().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getInstagram());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_google_plus: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getGoogleplus().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getGoogleplus());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_fb: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getFacebook().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getFacebook());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.img_web: {
                if(mUserProfileResponse==null)
                    break;
                if(mUserProfileResponse.getResponseData().getSocialite().getWebsite().length()==0){
                    Utility.showResponseMessage(mMainLayout,getString(R.string.link_not_updated_business));
                    break;
                }
                ((HomeScreen) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString(Constant.KEY_TYPE,mUserProfileResponse.getResponseData().getSocialite().getWebsite());
                ((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }

        }
    }






}
