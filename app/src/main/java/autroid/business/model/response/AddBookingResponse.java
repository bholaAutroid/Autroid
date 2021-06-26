package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class AddBookingResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetBookingData mGetBookingData;

    public GetBookingData getmGetBookingData() {
        return mGetBookingData;
    }

    public void setmGetBookingData(GetBookingData mGetBookingData) {
        this.mGetBookingData = mGetBookingData;
    }

    public class GetBookingData {
        private String id,booking_no,status;
        private Boolean is_services=true;

        public Boolean getIs_services() {
            return is_services;
        }

        public void setIs_services(Boolean is_services) {
            this.is_services = is_services;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBooking_no() {
            return booking_no;
        }

        public void setBooking_no(String booking_no) {
            this.booking_no = booking_no;
        }
    }
}
