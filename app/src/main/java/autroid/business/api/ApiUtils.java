package autroid.business.api;

import android.content.Context;



import java.io.IOException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Icreon on 02-08-2016.
 * <p/>
 * Class is used to define common structure if we require to add in our all requests call like
 * adding some common keys in header like device id, session token etc...
 */
public class ApiUtils {

    public static OkHttpClient addHeader(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                builder.header("x-access-token", PreferenceManager.getInstance().getStringPreference(context,Constant.SP_TOKEN));
                builder.header("business", PreferenceManager.getInstance().getStringPreference(context,Constant.SP_BUSINESS));
                builder.header("DeviceId", Utility.getDeviceId());
                builder.header("DeviceType", "Android");
                builder.header("DeviceModel", Utility.getDeviceModelName());
                builder.header("DeviceName", Utility.getDeviceName());
                builder.header("Language", "1");
                builder.header("AppVersion", Utility.getAppVersion(context));
                builder.header("app", "Suite");
                builder.header("Content-Type", "application/json");
                builder.header("tz", TimeZone.getDefault().getID());
                //builder.header("connection","close");
                builder.method(original.method(), original.body());
                return chain.proceed(builder.build());
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);

        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.writeTimeout(5, TimeUnit.MINUTES);

        return httpClient.build(); //.retryOnConnectionFailure(true).build();
    }


    public static OkHttpClient addLog(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(logging);
        return httpClient.build();
    }
}