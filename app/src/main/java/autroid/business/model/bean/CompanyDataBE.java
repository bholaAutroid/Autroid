package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class CompanyDataBE {

    @SerializedName("company")
    String companyName;

    @SerializedName("id")
    String companyId;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
