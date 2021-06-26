package autroid.business.model.response;

import java.util.ArrayList;

public class GetUserResponse extends BaseResponse {

    ArrayList<UserResponseData> responseData;

    public ArrayList<UserResponseData> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<UserResponseData> responseData) {
        this.responseData = responseData;
    }
}
