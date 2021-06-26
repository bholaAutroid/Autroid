package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ProductDetailBE;

public class ShowroomProductResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<ProductDetailBE> products;

    public ArrayList<ProductDetailBE> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDetailBE> products) {
        this.products = products;
    }
}
