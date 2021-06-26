package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.RequestLeadBE;

/**
 * Created by pranav.mittal on 08/10/17.
 */

public class RequestLeadResponse extends BaseResponse {

    private ArrayList<RequestLeadBE> responseData;

    public ArrayList<RequestLeadBE> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<RequestLeadBE> responseData) {
        this.responseData = responseData;
    }
}
