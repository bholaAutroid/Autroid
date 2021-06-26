package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.BookingAddressBE;


public class AddBookingAddressResponse extends BaseResponse {
    @SerializedName("responseData")
    private BookingAddressBE bookingAddressBE;

    public BookingAddressBE getBookingAddressBE() {
        return bookingAddressBE;
    }

    public void setBookingAddressBE(BookingAddressBE bookingAddressBE) {
        this.bookingAddressBE = bookingAddressBE;
    }
}
