package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingAddressBE;

public class ScheduleBookingAddressResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<BookingAddressBE> mAddressList;

    public ArrayList<BookingAddressBE> getmAddressList() {
        return mAddressList;
    }

    public void setmAddressList(ArrayList<BookingAddressBE> mAddressList) {
        this.mAddressList = mAddressList;
    }
}
