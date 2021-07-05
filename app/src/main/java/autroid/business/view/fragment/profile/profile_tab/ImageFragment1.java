package autroid.business.view.fragment.profile.profile_tab;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.ProfileGalleryAdapter;
import autroid.business.adapter.ShowroomGalleryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.GalleryImageClickCallback;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.bean.ThumbnailBE;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.model.response.BusinessProfileResponse;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.GridSpacingItemDecoration;
import autroid.business.utils.Utility;
import io.realm.Realm;
import io.realm.RealmList;

public class ImageFragment1 extends Fragment implements OnClickBusinessCallback, GalleryImageClickCallback {

    String userId;
    PreferenceManager preferenceManager;
    RecyclerView recListGallery;

    RealmController mRealmController;
    Realm mRealm;
    LinearLayout mLayoutGallery;
    ShowroomGalleryAdapter mProfileGalleryAdapter;

    public ImageFragment1() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );
//        GlobalBus.getBus().register( this );
        this.mRealm = RealmController.with( getActivity() ).getRealm();
        mRealmController = RealmController.getInstance();



        mLayoutGallery = view.findViewById( R.id.layout_gallery_image );
        preferenceManager = PreferenceManager.getInstance();


        recListGallery = (RecyclerView) view.findViewById( R.id.showroom_gallery_list );
        recListGallery.setNestedScrollingEnabled( false );
        setGridLayout();


        userId = preferenceManager.getStringPreference( getActivity(), Constant.SP_USERID );

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

    BusinessProfileResponse showroomProfileResponse;
    public void onSuccess(BusinessProfileResponse showroomProfileResponse) {

        this.showroomProfileResponse = showroomProfileResponse;
        if (userId.equalsIgnoreCase( showroomProfileResponse.getResponseData().getId() )) {
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
            recListGallery.setAdapter( new ProfileGalleryAdapter( getActivity(), showroomProfileResponse.getResponseData().getBusiness_gallery(), this ) );
        }

        if (showroomProfileResponse.getResponseData().getBusiness_gallery().size() == 0) {
            mLayoutGallery.setVisibility( View.VISIBLE );
        } else {
            mLayoutGallery.setVisibility( View.VISIBLE );
        }


//        Picasso.with( getActivity() ).load( showroomProfileResponse.getResponseData().getAvatar_address() ).transform( new BlurTransformation( getActivity(), 25, 1 ) ).placeholder( R.drawable.placeholder_big ).into( mCover );

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_gallery1, container, false );
    }

    @Override
    public void onBusinessClick(String id) {

    }

    @Override
    public void onGalleryClick(ArrayList<ThumbnailBE> mImages) {

    }
}