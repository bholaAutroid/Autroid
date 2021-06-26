package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class OrdersResponse extends BaseResponse {

    @SerializedName("responseData")
    ProductsResponse ordersList;

    @SerializedName("responseInfo")
    TotalResponse totalResponse;

    public ProductsResponse getOrdersList() {
        return ordersList;
    }

    public TotalResponse getTotalResponse() {
        return totalResponse;
    }
}
