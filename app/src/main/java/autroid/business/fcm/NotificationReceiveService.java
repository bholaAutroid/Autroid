package autroid.business.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import com.qiscus.sdk.Qiscus;
//import com.qiscus.sdk.service.QiscusFirebaseService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import autroid.business.R;
import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.LoginActivity;
import retrofit2.Response;


public class NotificationReceiveService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
         Log.d("Notification Message",remoteMessage.getData().toString());


//        if (QiscusFirebaseService.handleMessageReceived(remoteMessage)) { // For qiscus
//            try {
//               // Log.d("Notification User ID", remoteMessage.getData().get("payload"));
//                String payload = remoteMessage.getData().get("payload");
//               /* JsonObject jsonObject = new JsonObject();
//                JsonObject jsonObject1=jsonObject.getAsJsonObject(payload);
//
//                Log.d("Notification email", jsonObject1.get("email").toString());*/
//
//                JsonObject jsonObjectnew = (new JsonParser()).parse(payload).getAsJsonObject();
//              //  Log.d("Notification email 1",  jsonObjectnew.get("email").toString());
//
//                if(jsonObjectnew!=null) addLead(jsonObjectnew.get("email").toString().replaceAll("\"",""));
//
//
//            }
//            catch (NullPointerException e){
//
//            }catch (Exception e){
//
//            }
//
//            return;
//        }

        if(remoteMessage!=null) {
            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            int count = preferenceManager.getIntegerPreference(getApplicationContext(), Constant.SP_NOTIFICATION_COUNT);
            count = count + 1;
            preferenceManager.putIntegerPreference(getApplicationContext(), Constant.SP_NOTIFICATION_COUNT, count);
            /*Intent data = new Intent();
            data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_NOTIFICATION_COUNT);
            Events.SendEvent sendEvent =
                    new Events.SendEvent(data);
            GlobalBus.getBus().post(sendEvent);*/
        }


        if(!isAppIsInBackground(getApplicationContext())){

            sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"),remoteMessage.getData().get("source"),remoteMessage.getData().get("activity"));


//            Intent data = new Intent();
//            data.putExtra(Constant.KEY_TYPE,remoteMessage.getData().get("body"));
//            data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_NOTIFICATION);
//            Events.SendEvent sendEvent =
//                    new Events.SendEvent(data);
//            GlobalBus.getBus().post(sendEvent);
        }
        else {
            sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"),remoteMessage.getData().get("source"),remoteMessage.getData().get("activity"));
        }



     /* NotificationCompat.Builder notification = new NotificationCompat.Builder(NotificationReceiveService.this);
        notification.setContentTitle(remoteMessage.getNotification().getBody());
        notification.setSmallIcon(R.drawable.ic_drawer_log);
        Intent resultIntent = new Intent(NotificationReceiveService.this, HomeScreen.class);
        resultIntent.putExtra("fromNotification",true);
        PendingIntent pIntent =  PendingIntent.getActivity(this,0,resultIntent,0);
        notification.setContentIntent(pIntent);
        NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());*/

       /*boolean isAppInBgrd=((DataMarxApplication)getApplication()).isAppInBackground();
        if(isAppInBgrd){
            NotificationCompat.Builder notification = new NotificationCompat.Builder(NotificationReceiveService.this);
            notification.setContentTitle(remoteMessage.getNotification().getBody());
            notification.setSmallIcon(R.drawable.ic_profilesettings);
            Intent resultIntent = new Intent(NotificationReceiveService.this, HomeActivity.class);
            resultIntent.putExtra("fromNotification",true);
            PendingIntent pIntent =  PendingIntent.getActivity(this,0,resultIntent,0);
            notification.setContentIntent(pIntent);
            NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification.build());
        }else{
            Intent intent = new Intent(((DataMarxApplication)getApplicationContext()).INTENT_FILTER);
            intent.putExtra("message", remoteMessage.getNotification().getBody());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    */
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

//        Qiscus.setFcmToken(token);


        PreferenceManager preferenceManager=PreferenceManager.getInstance();
        preferenceManager.putStringPreference(this, Constant.SP_FCM_TOKEN,token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }


    private void sendNotification(String messageBody,String title,String source,String activity) {

        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        final String token=preferenceManager.getStringPreference(getApplicationContext(), Constant.SP_TOKEN);
        Intent intent;
        if(token!=null && token.length()>0) {
            Utility.setAuthToken(token);
            intent = new Intent(this, HomeScreen.class);
        }
        else {
            intent = new Intent(this, LoginActivity.class);
        }

        intent.putExtra("isNotification",true);
        intent.putExtra("source",source);
        intent.putExtra("activity",activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.mipmap.ic_small_logo);
            notificationBuilder.setColor(getResources().getColor(R.color.white_color));
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_small_logo);
        }

        Resources res = getApplicationContext().getResources();
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_large_icon));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    title,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(createID() /* ID of notification */, notificationBuilder.build());
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    public void addLead(String id) {
        ApiRequest apiRequest = ApiFactory.createService(getApplicationContext(), ApiRequest.class);
        ApiCallback.MyCall<BaseResponse> myCall = apiRequest.createLeadChat(id.trim(),"Chat");
        myCall.enqueue(new ApiCallback.MyCallback<BaseResponse>() {
            @Override
            public void success(final Response<BaseResponse> response) {

            }
            @Override
            public void error(String errorMessage) {
            }
        }, getApplicationContext(), null,Boolean.FALSE);
    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

    private int NotificationID() {
        AtomicInteger c = new AtomicInteger(0);

        return c.incrementAndGet();

    }

}
