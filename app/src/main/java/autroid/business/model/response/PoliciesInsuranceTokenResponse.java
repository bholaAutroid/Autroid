package autroid.business.model.response;

public class PoliciesInsuranceTokenResponse {

    private int responseCode;
    private GetData data;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public GetData getData() {
        return data;
    }

    public void setData(GetData data) {
        this.data = data;
    }

    public class GetData {
        private String token, messageId;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
