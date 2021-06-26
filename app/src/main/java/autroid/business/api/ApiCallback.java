package autroid.business.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import autroid.business.R;
import autroid.business.model.response.BaseResponse;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.NetworkUtil;
import autroid.business.utils.Utility;
import autroid.business.view.activity.LoginActivity;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Icreon on 02-08-2016.
 *
 * Class is responsible for handling all API responses and then tranfer the response to the activity
 * which generated the request and handling will be done inside that activity
 */

public class ApiCallback {
    /**
     * A callback which offers granular callbacks for various conditions.
     */
    public interface MyCallback<T> {
        /**
         * Called for [200, 300) responses.
         */
        void success(Response<T> response);

        /**
         * @param errorMessage
         */
        void error(String errorMessage);
    }

    public interface MyCall<T> {
        void cancel();
        void enqueue(MyCallback<T> callback, Context context, ViewGroup mainLayout, boolean isProgress);
        MyCall<T> clone();
    }

    public static class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
        @Override public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations,
                                               Retrofit retrofit) {
            if (getRawType(returnType) != MyCall.class) {
                return null;
            }
            if (!(returnType instanceof ParameterizedType)) {
                throw new IllegalStateException(
                        "MyCall must have generic type (e.g., MyCall<ResponseBody>)");
            }
            Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
            Executor callbackExecutor = retrofit.callbackExecutor();
            return new ErrorHandlingCallAdapter<>(responseType, callbackExecutor);
        }

        private static final class ErrorHandlingCallAdapter<R> implements CallAdapter<R, MyCall<R>> {
            private final Type responseType;
            private final Executor callbackExecutor;

            ErrorHandlingCallAdapter(Type responseType, Executor callbackExecutor) {
                this.responseType = responseType;
                this.callbackExecutor = callbackExecutor;
            }

            @Override public Type responseType() {
                return responseType;
            }

            @Override public MyCall<R> adapt(Call<R> call) {
                return new MyCallAdapter<>(call, callbackExecutor);
            }
        }
    }
    /**
     * Adapts a {@link Call} to {@link MyCall}.
     */
    /** Adapts a {@link Call} to {@link MyCall}. */
    static class MyCallAdapter<T> implements MyCall<T> {
        private final Call<T> call;
        private Context context;
        private ViewGroup mMainLayout;
        private ProgressDialog progressDialog;
        private final Executor callbackExecutor;

        MyCallAdapter(Call<T> call, Executor callbackExecutor) {
            this.call = call;
            this.callbackExecutor = callbackExecutor;
//                this.context=context;
        }

        @Override
        public void cancel() {
            call.cancel();
        }

        @Override
        public void enqueue(final MyCallback<T> callback, final Context context, ViewGroup mainLayout, boolean isProgress) {
            this.context = context;
            this.mMainLayout = mainLayout;
            if (!NetworkUtil.isInternetConnected(context) ){
                if(mMainLayout != null)
                    Utility.showResponseMessage(mMainLayout,context.getString(R.string.no_internet));
                return;
            }
            if (isProgress) {
                //  Utility.showProgressDialog(context);
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(Boolean.FALSE);
                progressDialog.show();
            }

            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, final Response<T> response) {
                    try {
                        //Utility.dismissProgressDialog();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        int code = response.code();
                        if (response.body() instanceof BaseResponse) {
                            int responseCode = ((BaseResponse) response.body()).getResponseCode();
                            if (responseCode >= 200 && responseCode < 300) {
                                callback.success(response);
                            } else {
                                displayError(((BaseResponse) response.body()).getResponseMessage());
                                callback.error(((BaseResponse) response.body()).getResponseMessage());
                            }
                            return;
                        }

                        Log.e("Error Code from server:", response.code() + "");
                        if (code >= 200 && code < 300) {
                            callback.success(response);
                        } else if (code == 401) {
                            logout();
                            /* serverError(response);
                            callback.error(response.message());*/
                        } else if (code >= 400 && code < 500) {
                            serverError(response);
                            callback.error(response.message());

                        } else if (code >= 500 && code < 600) {
                            serverError(response);
                            callback.error(response.message());
                        } else {
                            unexpectedError(new RuntimeException("Unexpected response " + response));
                            callback.error("Unexpected response " + response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    //Utility.dismissProgressDialog();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (t instanceof IOException) {
                        networkError((IOException) t);
                        callback.error("Network Error");
                    } else {
                        unexpectedError(t);
                        callback.error("Network Error");
                    }
                }
            });
        }

        private void logout() {
            //  Toast.makeText(context, "Session Expired Please Login Again!", Toast.LENGTH_LONG).show();
            PreferenceManager preferenceManager=PreferenceManager.getInstance();
            preferenceManager.putStringPreference(context, Constant.SP_TOKEN,"");
            Intent intent1 = new Intent(context, LoginActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent1);

        }

        private void unexpectedError(Throwable t) {
//            Log.e("Error",t.getMessage());
            t.printStackTrace();
            //   displayError(context.getString(R.string.unexpected_error));//+ t.getMessage());

        }

        private void networkError(IOException t) {
            //  Log.e("Error",t.getMessage());
            t.printStackTrace();
            //   displayError(context.getString(R.string.network_error));//+ t.getMessage());
        }

        private void unexpectedError(RuntimeException e) {
//            Log.e("Error", e.getMessage());
            e.printStackTrace();
            //    displayError(context.getString(R.string.unexpected_error));
        }

        private void serverError(Response<T> response) {
            //     Log.e("Error", response.message());
            try {
                String errorMessage = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorMessage);
                displayError(jsonObject.getString("responseMessage"));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public MyCall<T> clone() {
            return new MyCallAdapter<>(call.clone(),callbackExecutor);
        }

        private void displayError(final String message) {
            Log.e("displayError Method: ", message);
            if(context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        //Utility.showErroroBar(context, message);
                        //Utility.showResponseMessage();
                        if (mMainLayout != null) {
                            Utility.showResponseMessage(mMainLayout, message);
                        }
                    }
                });
            }
        }
    }





}
