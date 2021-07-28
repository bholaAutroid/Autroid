package autroid.business.view.fragment;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.model.request.UpdateBusinessLocationRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.UpdateBusinessLocationPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;
import jp.wasabeef.picasso.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateBusinessLocationFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener, AwsHomeActivity.AddressCallback {

    private GoogleMap mMap;
    TextView mSaveLocation,mUpdateLocaion;
    RelativeLayout mMainLayout;
    UpdateBusinessLocationPresenter mPresenter;
    Location mLocation;
    String address;
    TextView mName;
    ImageView mCover;
    AppCompatRatingBar mRatingBar;

    RealmController mRealmController;
    Realm mRealm;

    ShowroomRealm mShowroom;
    Marker marker;

    FirebaseAnalytics mFirebaseAnalytics;

    public static UpdateBusinessLocationFragment newInstance() {
        UpdateBusinessLocationFragment fragment = new UpdateBusinessLocationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_business_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlobalBus.getBus().register(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Update Business Location",null);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();

        mUpdateLocaion= (TextView) view.findViewById(R.id.update_location);
        mSaveLocation= (TextView) view.findViewById(R.id.save_location);
        mName= (TextView) view.findViewById(R.id.business_name);
        mRatingBar= (AppCompatRatingBar) view.findViewById(R.id.business_rating);
        mCover=view.findViewById(R.id.showroom_cover);

        LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.gray_color), PorterDuff.Mode.SRC_ATOP);

        mMainLayout=  view.findViewById(R.id.main_layout);
        mPresenter=new UpdateBusinessLocationPresenter(this,mMainLayout);

        mUpdateLocaion.setOnClickListener(this);
        mSaveLocation.setOnClickListener(this);

        mShowroom=mRealmController.getShowroomData();

        if(null!=mShowroom) {
            mName.setText(mShowroom.getBusinessName());
            mRatingBar.setRating(mShowroom.getRating());
            Picasso.with(getActivity()).load(mShowroom.getCover()).transform(new BlurTransformation(getActivity(), 25, 1)).placeholder(R.drawable.placeholder_big).into(mCover);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.

        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.map_style);
        mMap.setMapStyle(style);

        if(mShowroom.getLatitude()!=null && mShowroom.getLongitude()!=null ) {
            LatLng current = new LatLng(mShowroom.getLatitude(), mShowroom.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mShowroom.getLatitude(), mShowroom.getLongitude()), 12.0f));
        }
        else {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_location:
                ((AwsHomeActivity)getActivity()).getCurrentLocation(this);
                break;
            case R.id.save_location:
                if(mLocation!=null)
                    updateLocation();
                break;
        }
    }



    @Override
    public void getAddress(String address, Location location) {
        Log.d("Address",address);
        this.address=address;
        this.mLocation=location;
        LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
        if(marker!=null) marker.remove();

        marker=mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
    }

    private void updateLocation(){
        UpdateBusinessLocationRequest updateBusinessLocationRequest=new UpdateBusinessLocationRequest();
        updateBusinessLocationRequest.setLatitude(mLocation.getLatitude());
        updateBusinessLocationRequest.setLongitude(mLocation.getLongitude());
        updateBusinessLocationRequest.setLocation(address);
        mPresenter.updateLocation(updateBusinessLocationRequest);
    }

    public void onSuccess(BaseResponse baseResponse){
        mRealmController.updateShowroomLocation(mLocation.getLatitude(),mLocation.getLongitude(),address);
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_location", params);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE_ADDRESS);
        intent.putExtra(Constant.Key_Business_address,address);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
    }
}
