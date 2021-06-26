package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ProductDetailBE;

public class ShowroomProductsResponse {
    @SerializedName("responseData")
    private ArrayList<ProductDetailBE> productDetailBES;

    public ArrayList<ProductDetailBE> getProductDetailBES() {
        return productDetailBES;
    }

    public void setProductDetailBES(ArrayList<ProductDetailBE> productDetailBES) {
        this.productDetailBES = productDetailBES;
    }
}
