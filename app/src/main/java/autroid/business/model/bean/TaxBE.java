package autroid.business.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class TaxBE implements Serializable {
    private String tax;
    private float rate,base,amount;
    private ArrayList<TaxBE> detail;

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
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

    public ArrayList<TaxBE> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<TaxBE> detail) {
        this.detail = detail;
    }
}
