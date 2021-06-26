package autroid.business.fcm;

/**
 * Created by Icreon on 7/9/15.
 */
public class Consts {
    // In GCM, the Sender ID is a project ID that you acquire from the API console
    //public static final String PROJECT_NUMBER = Utility.GCM_PROJECT_ID;

    public static final String EXTRA_MESSAGE = "message";

    public static final String GCM_NOTIFICATION = "DataMarx";
    public static final String GCM_DELETED_MESSAGE = "Deleted messages on server: ";
    public static final String GCM_SEND_ERROR = "Send error: ";
    public static final String GCM_RECEIVED = "Received: ";

    public static final String KEY_GCM_PUSH_DEVICE_TOKEN = "GCM_PUSH_DEVICE_TOKEN";

    public static final String KEY_PUSH_MESSAGE = "m";
    public static final String KEY_PUSH_TYPE = "t";
    public static final String KEY_PUSH_ITEM_ID = "i";

    public static final String NEW_PUSH_EVENT = "new-push-event";

}
