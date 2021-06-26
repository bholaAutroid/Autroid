package autroid.business.model.bean;

public class CouponApplyBE {
    private PaymentTotalBE payment;
    private  PaymentBE due;

    public PaymentTotalBE getGetPaymentData() {
        return payment;
    }

    public void setGetPaymentData(PaymentTotalBE getPaymentData) {
        this.payment = getPaymentData;
    }

    public PaymentBE getDue() {
        return due;
    }

    public void setDue(PaymentBE due) {
        this.due = due;
    }
}
