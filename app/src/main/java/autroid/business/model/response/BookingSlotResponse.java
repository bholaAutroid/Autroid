package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingSlotBE;


public class BookingSlotResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<BookingSlotBE> getSlots;

    public ArrayList<BookingSlotBE> getGetSlots() {
        return getSlots;
    }

    public void setGetSlots(ArrayList<BookingSlotBE> getSlots) {
        this.getSlots = getSlots;
    }
}
