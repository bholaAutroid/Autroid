package autroid.business.model.bean;

import java.io.Serializable;

public class ServicePaymentBE implements Serializable {
    private String discount_type,coupon;
    private float discount,total,paid_total,due,careager_cash;
    private double convenienceCharges, discount_total, pick_up_charges,labour_cost,part_cost,of_cost;

    public double getOf_cost() {
        return of_cost;
    }

    public void setOf_cost(double of_cost) {
        this.of_cost = of_cost;
    }

    public float getCareager_cash() {
        return careager_cash;
    }

    public void setCareager_cash(float careager_cash) {
        this.careager_cash = careager_cash;
    }

    public float getDue() {
        return due;
    }

    public void setDue(float due) {
        this.due = due;
    }

    public double getPick_up_charges() {
        return pick_up_charges;
    }

    public void setPick_up_charges(double pick_up_charges) {
        this.pick_up_charges = pick_up_charges;
    }

    public double getLabour_cost() {
        return labour_cost;
    }

    public void setLabour_cost(double labour_cost) {
        this.labour_cost = labour_cost;
    }

    public double getPart_cost() {
        return part_cost;
    }

    public void setPart_cost(double part_cost) {
        this.part_cost = part_cost;
    }

    public double getDiscount_total() {
        return discount_total;
    }

    public void setDiscount_total(double discount_total) {
        this.discount_total = discount_total;
    }

    public double getConvenienceCharges() {
        return convenienceCharges;
    }

    public void setConvenienceCharges(double convenienceCharges) {
        this.convenienceCharges = convenienceCharges;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
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

    public float getPaid_total() {
        return paid_total;
    }

    public void setPaid_total(float paid_total) {
        this.paid_total = paid_total;
    }
}
