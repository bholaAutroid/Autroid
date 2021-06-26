package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.PaymentLogBE;

public class PaymentLogResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<PaymentLogBE> logsList;

    public ArrayList<PaymentLogBE> getLogsList() {
        return logsList;
    }
}
