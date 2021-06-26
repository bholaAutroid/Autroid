package autroid.business.model.bean;

public class PaymentBE {
    private String discount_type,coupon;
    private float discount,total,paid_total,due;

    public float getDue() {
        return due;
    }

    public void setDue(float due) {
        this.due = due;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getPaid_total() {
        return paid_total;
    }

    public void setPaid_total(float paid_total) {
        this.paid_total = paid_total;
    }
}
