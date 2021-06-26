package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingsBE;


/**
 * Created by pranav.mittal on 11/21/17.
 */

public class BookingsResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<BookingsBE> getBookings;

    @SerializedName("responseInfo")
    private TotalResponse totalResponse;

    public ArrayList<BookingsBE> getGetBookings() {
        return getBookings;
    }

    public void setGetBookings(ArrayList<BookingsBE> getBookings) {
        this.getBookings = getBookings;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }

    public void setTotalResponse(TotalResponse totalResponse) {
        this.totalResponse = totalResponse;
    }
}
