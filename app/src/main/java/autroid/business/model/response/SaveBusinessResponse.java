package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pranav.mittal on 03/08/18.
 */

public class SaveBusinessResponse extends BaseResponse {
    @SerializedName("responseData")
    private GetSaveResponse getSaveResponse;

    public GetSaveResponse getGetSaveResponse() {
        return getSaveResponse;
    }

    public void setGetSaveResponse(GetSaveResponse getSaveResponse) {
        this.getSaveResponse = getSaveResponse;
    }

    public class GetSaveResponse{
        private boolean is_bookmarked;

        public boolean isIs_bookmarked() {
            return is_bookmarked;
        }

        public void setIs_bookmarked(boolean is_bookmarked) {
            this.is_bookmarked = is_bookmarked;
        }
    }
}
