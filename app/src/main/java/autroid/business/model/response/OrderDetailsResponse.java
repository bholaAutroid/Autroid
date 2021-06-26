package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;
import autroid.business.model.bean.OrderDetailBE;

public class OrderDetailsResponse extends BaseResponse {

    @SerializedName("responseData")
    OrderDetailBE orderDetails;

    public OrderDetailBE getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetailBE orderDetails) {
        this.orderDetails = orderDetails;
    }
}
