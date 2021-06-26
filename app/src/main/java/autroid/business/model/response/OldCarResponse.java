package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarDetailBE;


/**
 * Created by pranav.mittal on 09/21/17.
 */

public class OldCarResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<CarDetailBE> stocks;

    public ArrayList<CarDetailBE> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<CarDetailBE> stocks) {
        this.stocks = stocks;
    }
}
