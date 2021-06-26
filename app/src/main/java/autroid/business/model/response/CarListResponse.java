package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarDetailBE;

/**
 * Created by pranav.mittal on 08/15/17.
 */

public class CarListResponse extends BaseResponse{


    @SerializedName("responseData")
    private GetCarStock getCarStock;

    public GetCarStock getGetCarStock() {
        return getCarStock;
    }

    public void setGetCarStock(GetCarStock getCarStock) {
        this.getCarStock = getCarStock;
    }

    public class GetCarStock{

        private String total,published;

        private ArrayList<CarDetailBE> stocks;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public ArrayList<CarDetailBE> getStocks() {
            return stocks;
        }

        public void setStocks(ArrayList<CarDetailBE> stocks) {
            this.stocks = stocks;
        }
    }
}
