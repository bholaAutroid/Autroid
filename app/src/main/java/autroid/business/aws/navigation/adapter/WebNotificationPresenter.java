package autroid.business.aws.navigation.adapter;

import android.view.ViewGroup;
import android.widget.Toast;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.aws.Notification_Web;

import autroid.business.model.response.UserSearchResponse;
import retrofit2.Response;

public class WebNotificationPresenter {

    private Notification_Web mActivity;
    private ViewGroup mMainLayout;


    public WebNotificationPresenter(Notification_Web mActivity, ViewGroup mMainLayout) {
        this.mActivity = mActivity;
        this.mMainLayout = mMainLayout;
    }


    public void getWebNotifications(){

//        ApiRequest apiRequest= ApiFactory.createService( mActivity,ApiRequest.class );
//        ApiCallback.MyCall<WebNotificationResponse> myCall=apiRequest.getWebNotificationData();


        ApiRequest apiRequest = ApiFactory.createService(mActivity, ApiRequest.class);
        ApiCallback.MyCall<WebNotificationResponse> myCall = apiRequest.getWebNotificationData();

        myCall.enqueue( new ApiCallback.MyCallback<WebNotificationResponse>() {
            @Override
            public void success(Response<WebNotificationResponse> response) {
                mActivity.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            WebNotificationResponse webNotificationResponse = response.body();
                            mActivity.onWebNotificationSuccess( webNotificationResponse );

                            Toast.makeText( mActivity, "i am running..", Toast.LENGTH_SHORT ).show();
                        } else {
                            Toast.makeText( mActivity, "not running..", Toast.LENGTH_SHORT ).show();
                        }
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
                Toast.makeText( mActivity, ""+errorMessage, Toast.LENGTH_SHORT ).show();
            }
        },mActivity,mMainLayout,Boolean.TRUE);


    }
}
