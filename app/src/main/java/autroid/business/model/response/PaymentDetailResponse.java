package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.CouponApplyBE;


public class PaymentDetailResponse extends BaseResponse{
    @SerializedName("responseData")
    private CouponApplyBE getPaymentData;

    public CouponApplyBE getGetPaymentData() {
        return getPaymentData;
    }

    public void setGetPaymentData(CouponApplyBE getPaymentData) {
        this.getPaymentData = getPaymentData;
    }
}
