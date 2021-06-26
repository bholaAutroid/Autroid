package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import autroid.business.model.bean.BookingDataBE;

public class BookingDetailsResponse extends BaseResponse implements Serializable {

    @SerializedName("responseData")
    ArrayList<BookingDataBE> bookingData;

    public ArrayList<BookingDataBE> getBookingData() {
        return bookingData;
    }

    public void setBookingData(ArrayList<BookingDataBE> bookingData) {
        this.bookingData = bookingData;
    }
}
