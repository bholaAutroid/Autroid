package autroid.business.model.bean;

import java.io.Serializable;

public class InsuranceDataBE implements Serializable {

    String insurance_company,policy_no,expire, policy_holder,branch,claim_no,policy_type;

    String contact_no,gstin,driver_accident, accident_place, accident_date, accident_time, accident_cause, spot_survey, fir;

    int premium;

    boolean claim,cashless;

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getDriver_accident() {
        return driver_accident;
    }

    public String getAccident_place() {
        return accident_place;
    }

    public String getAccident_date() {
        return accident_date;
    }

    public String getAccident_time() {
        return accident_time;
    }

    public String getAccident_cause() {
        return accident_cause;
    }

    public String getSpot_survey() {
        return spot_survey;
    }

    public String getFir() {
        return fir;
    }

    public String getPolicy_type() {
        return policy_type;
    }

    public String getClaim_no() {
        return claim_no;
    }

    public boolean isCashless() {
        return cashless;
    }

    public String getBranch() {
        return branch;
    }

    public boolean isClaim() {
        return claim;
    }

    public String getPolicy_holder() {
        return policy_holder;
    }

    public void setPolicy_holder(String policy_holder) {
        this.policy_holder = policy_holder;
    }

    public String getInsurance_company() {
        return insurance_company;
    }

    public void setInsurance_company(String insurance_company) {
        this.insurance_company = insurance_company;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public int getPremium() {
        return premium;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }
}
