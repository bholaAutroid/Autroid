package autroid.business.service;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.FcmTokenRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 12/03/17.
 */

public class RefreshFcmTokenService extends IntentService {



    public RefreshFcmTokenService() {
        super("RefreshFcmTokenService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent!=null) {
            String refreshToken=intent.getStringExtra(Constant.KEY_FCM_TOKEN);
            sendRegistrationToServer(refreshToken);
        }
    }


    /**
     * Method to send the token to
     * @param refreshtoken
     */
    private void sendRegistrationToServer(String refreshtoken) {
        ApiRequest apiRequest = ApiFactory.createService(this, ApiRequest.class);
        FcmTokenRequest fcmTokenRequest=new FcmTokenRequest();
        fcmTokenRequest.setFcmId(refreshtoken);
        fcmTokenRequest.setDeviceId(Utility.getDeviceId());
        fcmTokenRequest.setDeviceModel(Utility.getDeviceModelName());
        fcmTokenRequest.setDeviceType("Android");
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.refreshFcmToken(fcmTokenRequest);
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {

            }
            @Override
            public void error(String errorMessage) {
            }
        }, this,null,Boolean.FALSE);
    }
}
