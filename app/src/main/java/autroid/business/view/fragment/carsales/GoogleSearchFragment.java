package autroid.business.view.fragment.carsales;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.PlaceAutocompleteAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleSearchFragment extends DialogFragment implements  GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,View.OnClickListener, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, AwsHomeActivity.AddressCallback {

    Context mContext;
    GoogleApiClient mGoogleApiClient;

    LinearLayout mParent;
    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;


   /* private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));*/

    EditText mSearchEdittext;
    ImageView mClear;



    public GoogleSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity80)));
        return inflater.inflate(R.layout.fragment_google_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        initViews(view);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();

    }

    /*
   Initialize Views
    */
    private void initViews(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText) view.findViewById(R.id.search_et);
        mClear = (ImageView) view.findViewById(R.id.clear);
        mClear.setOnClickListener(this);

        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_NONE)
                .setCountry("IN")
                .build();

        mAdapter = new PlaceAutocompleteAdapter(getActivity(), R.layout.row_place_search,
                mGoogleApiClient, null, filter,this);
        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    mClear.setVisibility(View.VISIBLE);
                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } else {
                    mClear.setVisibility(View.GONE);
                    /*if (mSavedAdapter != null && mSavedAddressList.size() > 0) {
                        mRecyclerView.setAdapter(mSavedAdapter);
                    }*/
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v == mClear){
            mSearchEdittext.setText("");
            if(mAdapter!=null){
                mAdapter.clearList();
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(final ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, final int position) {

            try {

                if(position==0){
                    ((AwsHomeActivity)getActivity()).getCurrentLocation(this);

                }
                else {
                    if (mResultList != null) {

                        final String placeId = String.valueOf(mResultList.get(position - 1).placeId);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getCount() == 1) {
                                    //Do the things here on Click.....
                                    Intent data = new Intent();
                                    data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_LOCATION);
                                    data.putExtra(Constant.Key_lat,places.get(0).getLatLng().latitude);
                                    data.putExtra(Constant.Key_lng,places.get(0).getLatLng().longitude);
                                    data.putExtra(Constant.KEY_LOCATION, mResultList.get(position - 1).primaryText);

                                    Events.SendEvent sendEvent =
                                            new Events.SendEvent(data);
                                    GlobalBus.getBus().post(sendEvent);
                                    getDialog().dismiss();

                                    //setResult(GoogleSearchFragment.RESULT_OK, data);
                                    // finish();
                                } else {
                                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            }

    @Override
    public void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
    }

    @Override
    public void onPause() {
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void getAddress(String address, Location location) {
        Intent data = new Intent();
        data.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_LOCATION);
        data.putExtra(Constant.Key_lat,location.getLatitude());
        data.putExtra(Constant.Key_lng,location.getLongitude());
        data.putExtra(Constant.KEY_LOCATION,address);

        Events.SendEvent sendEvent =
                new Events.SendEvent(data);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }
}
