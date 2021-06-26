package autroid.business.eventbus;

import android.content.Intent;

/**
 * Created by pranav.mittal on 02/01/18.
 */

public class Events {

    // Event used to send message from fragment to activity.
    public static class FragmentActivityMessage {
        private String message;
        public FragmentActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment.
    public static class ActivityFragmentMessage {
        private Intent message;
        public ActivityFragmentMessage(Intent message) {
            this.message = message;
        }
        public Intent getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment.
    public static class SendEvent {
        private Intent message;
        public SendEvent(Intent intent) {
            this.message = intent;
        }
        public Intent getEvent() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class ActivityActivityMessage {
        private String message;
        public ActivityActivityMessage(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

}
