package autroid.business.model.bean;

import java.io.Serializable;

public class PriceBreakupBE implements Serializable {
    private String item,amount_is_tax,tax,source,tax_rate;
    private float quantity,rate,base,amount,tax_amount,discount;
    private TaxBE tax_info;

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getAmount_is_tax() {
        return amount_is_tax;
    }

    public void setAmount_is_tax(String amount_is_tax) {
        this.amount_is_tax = amount_is_tax;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public float getBase() {
        return base;
    }

    public void setBase(float base) {
        this.base = base;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(float tax_amount) {
        this.tax_amount = tax_amount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public TaxBE getTax_info() {
        return tax_info;
    }

    public void setTax_info(TaxBE tax_info) {
        this.tax_info = tax_info;
    }
}
