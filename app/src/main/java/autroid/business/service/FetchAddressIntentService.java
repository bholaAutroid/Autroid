package autroid.business.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by pranav.mittal on 06/23/17.
 */

public class FetchAddressIntentService extends IntentService {

    private static String LOCATION="location";
    private static String RESULTRECEIVER="resultreeciver";
    public  static String RESULT_ADDRESS="address";
    public  static String RESULT_LOCATION="location";
    public static int FAILURE_RESULT=400;
    public static int SUCCESS_RESULT=200;
    private ResultReceiver mResultReceiver;
    private String errorMessage="";
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }


    public static void startService(Context context, Location location, ResultReceiver receiver) {
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(LOCATION,location);
        intent.putExtra(RESULTRECEIVER,receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            String result = null;
            Location location = intent.getParcelableExtra(LOCATION);
            mResultReceiver=intent.getParcelableExtra(RESULTRECEIVER);
            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
            }
            if (addresses == null || addresses.size()  == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "Not Found";
                }
                deliverResultToReceiver(FAILURE_RESULT, errorMessage,location);
            }else{
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    StringBuilder sb = new StringBuilder();
                    sb.append(address.getAddressLine(0)).append("\n");
                    for (int i = 1; i <= address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                   /* sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());*/
                    result = sb.toString();
                    deliverResultToReceiver(SUCCESS_RESULT,result,location);
                }
            }

        }
    }
    private void deliverResultToReceiver(int resultCode, String address,Location location) {
        Bundle bundle = new Bundle();
        bundle.putString(RESULT_ADDRESS, address);
        bundle.putParcelable(RESULT_LOCATION,location);
        mResultReceiver.send(resultCode, bundle);
    }

}
