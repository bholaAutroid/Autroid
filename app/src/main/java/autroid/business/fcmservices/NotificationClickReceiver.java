package autroid.business.fcmservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

//import com.qiscus.sdk.chat.core.QiscusCore;
//import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
//import com.qiscus.sdk.chat.core.data.model.QiscusComment;
//import com.qiscus.sdk.chat.core.data.remote.QiscusApi;

import autroid.business.view.activity.ChatActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.qiscus.mychatui.ui.ChatRoomActivity;
//import com.qiscus.mychatui.ui.GroupChatRoomActivity;

/**
 * @author Yuana andhikayuana@gmail.com
 * @since Aug, Tue 14 2018 12.40
 **/
public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        QiscusComment qiscusComment = intent.getParcelableExtra("data");
//        QiscusApi.getInstance()
//                .getChatRoom(qiscusComment.getRoomId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(qiscusChatRoom -> QiscusCore.getDataStore().addOrUpdate(qiscusChatRoom))
//                .map(qiscusChatRoom -> getChatRoomActivity(context, qiscusChatRoom))
//                .subscribe(newIntent -> start(context, newIntent), throwable ->
//                        Toast.makeText(context, throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show());
//    }

//    private Intent getChatRoomActivity(Context context, QiscusChatRoom qiscusChatRoom) {
//      //  startActivity(QiscusChatActivity.generateIntent(getActivity(), adapter.getData().get(position)));
//        return new Intent(context, ChatActivity.class).putExtra("chat_room",qiscusChatRoom);
//    }

//    private void start(Context context, Intent newIntent) {
//        context.startActivity(newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

    }
}
