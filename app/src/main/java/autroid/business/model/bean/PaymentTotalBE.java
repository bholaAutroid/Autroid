package autroid.business.model.bean;

public class PaymentTotalBE {
    private String payment_mode,coupon,coupon_type,discount_type,transaction_id,transaction_status,transaction_response,terms;
    private float discount,discount_total,paid_total,total,pick_up_charges,pick_up_limit,convenience_charges,careager_cash;

    public float getCareager_cash() {
        return careager_cash;
    }

    public void setCareager_cash(float careager_cash) {
        this.careager_cash = careager_cash;
    }

    public float getConvenience_charges() {
        return convenience_charges;
    }

    public void setConvenience_charges(float convenience_charges) {
        this.convenience_charges = convenience_charges;
    }


    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public float getPick_up_limit() {
        return pick_up_limit;
    }

    public void setPick_up_limit(float pick_up_limit) {
        this.pick_up_limit = pick_up_limit;
    }

    public float getPick_up_charges() {
        return pick_up_charges;
    }

    public void setPick_up_charges(float pick_up_charges) {
        this.pick_up_charges = pick_up_charges;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    public float getDiscount() {

        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(float discount_total) {
        this.discount_total = discount_total;
    }

    public float getPaid_total() {
        return paid_total;
    }

    public void setPaid_total(float paid_total) {
        this.paid_total = paid_total;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getTransaction_response() {
        return transaction_response;
    }

    public void setTransaction_response(String transaction_response) {
        this.transaction_response = transaction_response;
    }
}
