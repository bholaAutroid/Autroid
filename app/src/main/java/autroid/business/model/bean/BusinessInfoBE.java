package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 02/27/18.
 */

public class BusinessInfoBE implements Serializable {

    private String business_category,maker,package_subscription_on,package_upgradation,subscribed_package;
    private Boolean is_claimed,assistance;
    private String company_name,gstin;

    public Boolean getAssistance() {
        return assistance;
    }

    public void setAssistance(Boolean assistance) {
        this.assistance = assistance;
    }

    public String getCategory() {
        return business_category;
    }

    public void setCategory(String category) {
        this.business_category = category;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getPackage_subscription_on() {
        return package_subscription_on;
    }

    public void setPackage_subscription_on(String package_subscription_on) {
        this.package_subscription_on = package_subscription_on;
    }

    public String getPackage_upgradation() {
        return package_upgradation;
    }

    public void setPackage_upgradation(String package_upgradation) {
        this.package_upgradation = package_upgradation;
    }

    public String getSubscribed_package() {
        return subscribed_package;
    }

    public void setSubscribed_package(String subscribed_package) {
        this.subscribed_package = subscribed_package;
    }

    public Boolean getIs_claimed() {
        return is_claimed;
    }

    public void setIs_claimed(Boolean is_claimed) {
        this.is_claimed = is_claimed;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
