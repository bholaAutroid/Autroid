package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class InsuranceDueBE {

    private String id,title,registration_no;

    @SerializedName("insurance_info")
    private InsuranceDataBE insuranceData;

    private UserBE user;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public InsuranceDataBE getInsuranceData() {
        return insuranceData;
    }

    public UserBE getUser() {
        return user;
    }
}
