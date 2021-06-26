package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class PaymentLogBE {

    @SerializedName("transaction_id")
    String transactionId;

    @SerializedName("payment_mode")
    String paymentMode;

    @SerializedName("transaction_date")
    String transactionDate;

    @SerializedName("paid_total")
    int paidTotal;

    String updated_at;

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public int getPaidTotal() {
        return paidTotal;
    }
}
