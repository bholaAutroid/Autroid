package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CompanyDataBE;

public class GetCompanyResponse extends BaseResponse {

    public ArrayList<CompanyDataBE> getCompanyResponse() {
        return companyResponse;
    }

    public void setCompanyResponse(ArrayList<CompanyDataBE> companyResponse) {
        this.companyResponse = companyResponse;
    }

    @SerializedName("responseData")
    ArrayList<CompanyDataBE> companyResponse;

}
