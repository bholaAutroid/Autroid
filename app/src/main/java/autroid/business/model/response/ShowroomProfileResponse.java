package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.ShowroomProfileBE;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class ShowroomProfileResponse extends BaseResponse {
    private ArrayList<ShowroomProfileBE> responseData;

    public ArrayList<ShowroomProfileBE> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<ShowroomProfileBE> responseData) {
        this.responseData = responseData;
    }
}
