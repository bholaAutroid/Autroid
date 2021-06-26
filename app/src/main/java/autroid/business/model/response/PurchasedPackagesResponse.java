package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.PurchasedPackagesBE;


public class PurchasedPackagesResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<PurchasedPackagesBE> getPackaged;

    public ArrayList<PurchasedPackagesBE> getGetPackaged() {
        return getPackaged;
    }

    public void setGetPackaged(ArrayList<PurchasedPackagesBE> getPackaged) {
        this.getPackaged = getPackaged;
    }
}
