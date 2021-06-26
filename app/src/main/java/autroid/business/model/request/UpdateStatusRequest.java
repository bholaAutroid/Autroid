package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusRequest {

    @SerializedName("booking")
    String bookingId;

    @SerializedName("status")
    String statusToUpdate;

    String stage;

    String back;

    public void setBack(String back) {
        this.back = back;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setStatusToUpdate(String statusToUpdate) {
        this.statusToUpdate = statusToUpdate;
    }
}
