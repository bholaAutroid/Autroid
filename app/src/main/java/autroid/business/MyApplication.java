package autroid.business;

import android.app.Application;
import android.os.Build;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.qiscus.nirmana.Nirmana;
//import com.qiscus.sdk.Qiscus;
//import com.qiscus.sdk.chat.core.QiscusCore;

import autroid.business.fcmservices.PushNotificationUtil;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class MyApplication extends Application {

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
        super.onCreate();

        setUpRealmConfig();
        Fresco.initialize(getApplicationContext());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    private void setUpRealmConfig(){

        // initialize Realm
        Realm.init(getApplicationContext());
//        Qiscus.init(this,"careager-5tsspx1gba4n");
//        QiscusCore.init(this, "careager-5tsspx1gba4n");
//        Nirmana.init(this);
//        QiscusCore.getChatConfig()
//                .setNotificationListener(PushNotificationUtil::showNotification)
//                .setEnableFcmPushNotification(true);

//        Qiscus.getChatConfig()
//                .setStatusBarColor(R.color.colorPrimaryDark)
//                .setAppBarColor(R.color.matt_black)
//                .setRightBubbleColor(R.color.matt_black)
//                .setRightBubbleTextColor(R.color.white_dark)
//                .setRightBubbleTimeColor(R.color.white_dark)
//                .setLeftBubbleColor(R.color.card_color)
//                .setLeftBubbleTextColor(R.color.white_dark)
//                .setLeftBubbleTimeColor(R.color.white_dark)
//                .setReadIconColor(R.color.colorAccent)
//                .setReplyBarColor(R.color.card_color)
//                .setReplySenderColor(R.color.card_color)
//                .setEnableFcmPushNotification(true)
//                .setNotificationBigIcon(R.drawable.ic_notification)
//                .setNotificationSmallIcon(R.drawable.ic_notification)
//                .setOnlyEnablePushNotificationOutsideChatRoom(true)
//                .setInlineReplyColor(R.color.card_color)
//                .setEnableAddLocation(true)
//                .setEmptyRoomTitleColor(R.color.card_color)
//                .setAccentColor(R.color.colorAccent)
//                .getDeleteCommentConfig().setEnableDeleteComment(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Qiscus.getChatConfig().setEnableReplyNotification(true);
//
//        }

        // create your Realm configuration
        RealmConfiguration config = new RealmConfiguration.
                Builder().
                name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0).
                        deleteRealmIfMigrationNeeded().
                        build();
        Realm.setDefaultConfiguration(config);
    }
}
