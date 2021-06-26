package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StatusCountResponse extends BaseResponse {
    @SerializedName("responseData")
    private ArrayList<GetResponse> getResponse;

    public ArrayList<GetResponse> getGetResponse() {
        return getResponse;
    }

    public void setGetResponse(ArrayList<GetResponse> getResponse) {
        this.getResponse = getResponse;
    }

    public class GetResponse{
        String status;
        int count;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
