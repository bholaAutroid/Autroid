package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.BookingCreatedDetailsBE;

public class GetUserBookingResponse extends BaseResponse {

    @SerializedName("responseData")
    BookingCreatedDetailsBE bookingCreatedDetailsBE;

    public BookingCreatedDetailsBE getBookingCreatedDetailsBE() {
        return bookingCreatedDetailsBE;
    }

    public void setBookingCreatedDetailsBE(BookingCreatedDetailsBE bookingCreatedDetailsBE) {
        this.bookingCreatedDetailsBE = bookingCreatedDetailsBE;
    }
}
