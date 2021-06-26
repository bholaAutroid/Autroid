package autroid.business.fcmservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
//import com.qiscus.sdk.chat.core.QiscusCore;
//import com.qiscus.sdk.chat.core.util.QiscusFirebaseMessagingUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import autroid.business.R;

public class FirebaseService extends FirebaseMessagingService {

    RemoteMessage remoteMessage;
    boolean asyncRunning=false;
    CreateBitmap createBitmap;

    public class CreateBitmap extends AsyncTask<String,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap=null;
            asyncRunning=true;

            try {
                URL url=new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d("Firebase",bitmap.toString());
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            asyncRunning=false;
            makeNotification(bitmap,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }

    }

    @Override
    public void onNewToken(String s) { //unique id for the app to be identified by Firebase Messaging Service
        super.onNewToken(s);
//        QiscusCore.setFcmToken(s); // Given token to qiscus
        Log.e("service",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) { //message sent by firebase
        super.onMessageReceived(remoteMessage);

        this.remoteMessage=remoteMessage;

        Log.d("Firebase","I am onMessageReceived");

//        if (QiscusFirebaseMessagingUtil.handleMessageReceived(remoteMessage)) {
//            //chaecking if message sent by firebase given by qiscus
//            return;
//        }else{
//
//            if(!asyncRunning) {
//                createBitmap=new CreateBitmap();
//                createBitmap.execute(this.remoteMessage.getNotification().getTag());
//            }
//            else if(asyncRunning){
//                createBitmap.cancel(true);
//                asyncRunning=false;
//                createBitmap.execute(this.remoteMessage.getNotification().getTag());
//            }
//        }

    }


    void makeNotification(Bitmap bitmap,String title,String body){


//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent,
//                PendingIntent.FLAG_ONE_SHOT);


        Notification notification = new NotificationCompat.Builder(this, "")
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                        .bigLargeIcon(null))
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notification);

    }

}
