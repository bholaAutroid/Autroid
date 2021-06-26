package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ShowroomProfileBE;

/**
 * Created by pranav.mittal on 03/08/18.
 */

public class SavedBusinessResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<ShowroomProfileBE> business;

    public ArrayList<ShowroomProfileBE> getBusiness() {
        return business;
    }

    public void setBusiness(ArrayList<ShowroomProfileBE> business) {
        this.business = business;
    }
}
