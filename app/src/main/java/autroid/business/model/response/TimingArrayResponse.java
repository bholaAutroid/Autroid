package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class TimingArrayResponse extends BaseResponse {

    @SerializedName("responseData")
    private GetTimings getTimings;

    public GetTimings getGetTimings() {
        return getTimings;
    }

    public void setGetTimings(GetTimings getTimings) {
        this.getTimings = getTimings;
    }

    public class GetTimings{
        private ArrayList<String> open;
        private ArrayList<String> close;

        public ArrayList<String> getOpen() {
            return open;
        }

        public void setOpen(ArrayList<String> open) {
            this.open = open;
        }

        public ArrayList<String> getClose() {
            return close;
        }

        public void setClose(ArrayList<String> close) {
            this.close = close;
        }
    }
}
