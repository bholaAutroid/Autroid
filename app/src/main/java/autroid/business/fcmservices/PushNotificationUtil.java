package autroid.business.fcmservices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
//
//import com.qiscus.sdk.chat.core.QiscusCore;
//import com.qiscus.sdk.chat.core.data.model.QiscusComment;
//import com.qiscus.sdk.chat.core.util.BuildVersionUtil;
//import com.qiscus.sdk.chat.core.util.QiscusAndroidUtil;
//import com.qiscus.sdk.chat.core.util.QiscusNumberUtil;
import autroid.business.R;
import autroid.business.view.activity.ChatActivity;

public final class PushNotificationUtil {

//    private static String USER = QiscusCore.getQiscusAccount().getEmail();
//
//    public static int notificationId;
//
//    private PushNotificationUtil() {
//    }
//
//    public static void showNotification(Context context, QiscusComment qiscusComment) {
//        if (QiscusCore.getDataStore().isContains(qiscusComment)) {
//            return;
//        }
//
//        if(qiscusComment.getSenderEmail().equals(USER)){
//            return;
//        }else if(!qiscusComment.getSenderEmail().equals(USER) && ChatActivity.isActivityOpened){
//            return;
//        }
//
//        QiscusCore.getDataStore().addOrUpdate(qiscusComment);
//
//        String notificationChannelId = QiscusCore.getApps().getPackageName() + ".qiscus.sdk.notification.channel";
//        if (BuildVersionUtil.isOreoOrHigher()) {
//            NotificationChannel notificationChannel =
//                    new NotificationChannel(notificationChannelId, "Chat", NotificationManager.IMPORTANCE_HIGH);
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//
//        PendingIntent pendingIntent;
//        Intent openIntent = new Intent(context, NotificationClickReceiver.class);
//        openIntent.putExtra("data", qiscusComment);
//        pendingIntent = PendingIntent.getBroadcast(context, QiscusNumberUtil.convertToInt(qiscusComment.getRoomId()),
//                openIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, notificationChannelId);
//
//
//        notificationBuilder.setContentTitle(qiscusComment.getSender())
//                .setContentIntent(pendingIntent)
//                .setContentText(qiscusComment.getMessage())
//                .setTicker(qiscusComment.getMessage())
//                .setGroup("CHAT_NOTIF_" + qiscusComment.getRoomId())
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            notificationBuilder.setSmallIcon(R.drawable.ic_logo_notification);
//            notificationBuilder.setColor(context.getApplicationContext().getResources().getColor(R.color.white_color));
//        } else {
//            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        }
//
//        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getApplicationContext().getResources(),R.mipmap.ic_launcher));
//
//        notificationId=QiscusNumberUtil.convertToInt(qiscusComment.getRoomId());
//        QiscusAndroidUtil.runOnUIThread(() -> NotificationManagerCompat.from(context)
//                .notify(notificationId, notificationBuilder.build()));
//    }

}
