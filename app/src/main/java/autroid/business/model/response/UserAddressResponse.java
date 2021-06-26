package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingAddressBE;
import autroid.business.model.bean.OrderConvenienceBE;

public class UserAddressResponse extends BaseResponse{

    @SerializedName("address")
    private ArrayList<BookingAddressBE> addressList;

    @SerializedName("convenience")
    private ArrayList<OrderConvenienceBE> convenienceList;

    public ArrayList<BookingAddressBE> getAddressList() {
        return addressList;
    }

    public ArrayList<OrderConvenienceBE> getConvenienceList() {
        return convenienceList;
    }
}
