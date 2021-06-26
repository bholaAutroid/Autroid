package autroid.business.view.activity.map_activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
//import com.qiscus.sdk.chat.core.data.model.QiscusComment;
//import com.qiscus.sdk.chat.core.data.model.QiscusLocation;
//import com.qiscus.sdk.chat.core.data.remote.QiscusApi;

import autroid.business.R;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    PlaceAutocompleteFragment autocompleteFragment;

    GoogleMap googleMap;
    LatLng latLng;
    Marker marker;
    MarkerOptions markerOptions;
    LinearLayout linearLayout;
    String address;

//    QiscusLocation location;
//    QiscusChatRoom qiscusChatRoom;
//    QiscusComment qiscusComment;

    TextView address_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        latLng=getIntent().getParcelableExtra("lat_lng");
        address=getIntent().getStringExtra("address");
//        qiscusChatRoom=getIntent().getParcelableExtra("chat_room");
//        location=getIntent().getParcelableExtra("location");

        address_view=(TextView)findViewById(R.id.address_textview);
        address_view.setText(address);

        linearLayout=(LinearLayout)findViewById(R.id.linear_layout2);

        linearLayout.setOnClickListener(v->{
//            sendLocation(location);
            finish();
        });

        SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment);
        fragment.getMapAsync(this);//tells the fragment to manage the map functionality/

        autocompleteFragment=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latLng=null;
                address=null;
//                location=null;

                if(marker!=null){
                    marker.remove();
                }

                latLng=place.getLatLng();
                address=place.getAddress().toString();

//                location=new QiscusLocation();
//                location.setName("Location");
//                location.setAddress(address);
//                location.setLatitude(latLng.latitude);
//                location.setLongitude(latLng.longitude);

                address_view.setText(address);
                setMarkerAndCamera(latLng,address);
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this,status.getStatusMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void sendLocation(QiscusLocation location) {
//
//        qiscusComment = QiscusComment.generateLocationMessage(qiscusChatRoom.getId(),location);
//        QiscusApi.getInstance().postComment(qiscusComment)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(comment -> {
//                    Log.d("MapActivity", "Success");
//                }, throwable -> {
//                    Log.d("MapActivity", "Error");
//                });
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
        setMarkerAndCamera(latLng,address);
    }

    private void setMarkerAndCamera(LatLng latLng, String address){
        CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(16).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        markerOptions=new MarkerOptions().position(latLng).title(address);
        marker=googleMap.addMarker(markerOptions);

    }
}
