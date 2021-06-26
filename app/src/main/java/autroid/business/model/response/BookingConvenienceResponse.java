package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookingAddressBE;
import autroid.business.model.bean.OrderConvenienceBE;

public class BookingConvenienceResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetData getData;

    public GetData getGetData() {
        return getData;
    }

    public void setGetData(GetData getData) {
        this.getData = getData;
    }

    public class GetData{
        private ArrayList<BookingAddressBE> address;
        private ArrayList<OrderConvenienceBE> convenience;

        public ArrayList<BookingAddressBE> getAddress() {
            return address;
        }

        public void setAddress(ArrayList<BookingAddressBE> address) {
            this.address = address;
        }

        public ArrayList<OrderConvenienceBE> getConvenience() {
            return convenience;
        }

        public void setConvenience(ArrayList<OrderConvenienceBE> convenience) {
            this.convenience = convenience;
        }
    }

}
