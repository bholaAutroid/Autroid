package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.JobsQCBE;

public class PointsRequest {

    String booking,status,stage;

    @SerializedName("qc")
    ArrayList<JobsQCBE> arrayList;

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setArrayList(ArrayList<JobsQCBE> arrayList) {
        this.arrayList = arrayList;
    }
}
