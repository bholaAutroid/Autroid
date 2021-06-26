package autroid.business.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Icreon on 02-08-2016.
 *
 * This is Factory class for creating a new request API, we will set the Base URL, response type
 * renderer, client and all in the respective method and way. Basically core class for generate a
 * new service call.
 */
public class ApiFactory {

   static Gson gson=new GsonBuilder().serializeNulls().create();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiURL.BASE_URL)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(new ApiCallback.ErrorHandlingCallAdapterFactory());

    public static <S> S createService(Context context, Class<S> serviceClass) {
        Retrofit retrofit = builder.client(ApiUtils.addHeader(context)).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit.Builder getBuilder() {
        return builder;
    }
}
