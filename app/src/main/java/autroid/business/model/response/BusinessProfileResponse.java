package autroid.business.model.response;

import autroid.business.model.bean.ShowroomProfileBE;

public class BusinessProfileResponse extends BaseResponse {

    ShowroomProfileBE responseData;

    public ShowroomProfileBE getResponseData() {
        return responseData;
    }

    public void setResponseData(ShowroomProfileBE responseData) {
        this.responseData = responseData;
    }
}
