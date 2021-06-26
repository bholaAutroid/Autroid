package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

public class InsuranceUpdateRequest {

    @SerializedName("insurance_company")
    String insurance_company;

    @SerializedName("policy_no")
    String policy_no;

    @SerializedName("expire")
    String expire;

    @SerializedName("policy_holder")
    String policy_holder;

    String booking;

    int premium;

    public void setBookingId(String booking) {
        this.booking = booking;
    }

    public void setInsurance_company(String insurance_company) {
        this.insurance_company = insurance_company;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void setPolicy_holder(String policy_holder) {
        this.policy_holder = policy_holder;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }
}
