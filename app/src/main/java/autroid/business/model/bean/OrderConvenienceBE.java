package autroid.business.model.bean;

public class OrderConvenienceBE {
    private String convenience,message;
    private int charges;
    private Boolean chargeable,isEnabled=true;

    public OrderConvenienceBE(String convenience, int cost, String message, Boolean chargeable, Boolean isEnabled){
        this.convenience=convenience;
        this.charges=cost;
        this.message=message;
        this.chargeable=chargeable;
        this.isEnabled=isEnabled;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public Boolean getChargeable() {
        return chargeable;
    }

    public void setChargeable(Boolean chargeable) {
        this.chargeable = chargeable;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
