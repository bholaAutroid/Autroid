package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentModeResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<String> getMode;

    public ArrayList<String> getGetMode() {
        return getMode;
    }

    public void setGetMode(ArrayList<String> getMode) {
        this.getMode = getMode;
    }
}
