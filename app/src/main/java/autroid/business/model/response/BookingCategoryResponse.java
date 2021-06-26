package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingCategoryBE;


public class BookingCategoryResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<BookingCategoryBE> bookingCategoryBES;

    public ArrayList<BookingCategoryBE> getBookingCategoryBES() {
        return bookingCategoryBES;
    }

    public void setBookingCategoryBES(ArrayList<BookingCategoryBE> bookingCategoryBES) {
        this.bookingCategoryBES = bookingCategoryBES;
    }
}
