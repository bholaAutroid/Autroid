package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class BookingRescheduleResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetData getData;

    public GetData getGetData() {
        return getData;
    }

    public void setGetData(GetData getData) {
        this.getData = getData;
    }

    public class GetData{
        private String date,time_slot,updated_at,status;

        public String getStatus() {
            return status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime_slot() {
            return time_slot;
        }

        public void setTime_slot(String time_slot) {
            this.time_slot = time_slot;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
