package autroid.business.model.response;

/**
 * Created by Icreon on 4/10/2017.
 */

public class BaseResponse {

    protected int responseCode;

    protected String responseMessage;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
