package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.NotificationListBE;


public class NotificationsListResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<NotificationListBE> getNotificationsListResponse;

    public ArrayList<NotificationListBE> getGetNotificationsListResponse() {
        return getNotificationsListResponse;
    }

    public void setGetNotificationsListResponse(ArrayList<NotificationListBE> getNotificationsListResponse) {
        this.getNotificationsListResponse = getNotificationsListResponse;
    }
}
