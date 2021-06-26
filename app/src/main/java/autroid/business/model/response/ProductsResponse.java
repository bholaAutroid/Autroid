package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.OrderDataBE;

public class ProductsResponse {

    @SerializedName("products")
    private ArrayList<OrderDataBE> orderData;

    public ArrayList<OrderDataBE> getOrderData() {
        return orderData;
    }

    public void setOrderData(ArrayList<OrderDataBE> orderData) {
        this.orderData = orderData;
    }
}
