package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarItemsBE;

public class ProductBrandResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<CarItemsBE> getBrands;

    public ArrayList<CarItemsBE> getGetBrands() {
        return getBrands;
    }

    public void setGetBrands(ArrayList<CarItemsBE> getBrands) {
        this.getBrands = getBrands;
    }
}
