package autroid.business.model.realm;

import io.realm.RealmObject;

public class EstimateTaxCalculationRealm extends RealmObject {
    private float amount;
    private String tax;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
