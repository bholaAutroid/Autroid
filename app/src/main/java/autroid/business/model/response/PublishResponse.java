package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pranav.mittal on 12/13/17.
 */

public class PublishResponse extends BaseResponse{

    @SerializedName("responseData")
    private GetPublishResponse getPublishResponse;

    public GetPublishResponse getGetPublishResponse() {
        return getPublishResponse;
    }

    public void setGetPublishResponse(GetPublishResponse getPublishResponse) {
        this.getPublishResponse = getPublishResponse;
    }

    public class GetPublishResponse{
        private boolean publish;
        private int total,published;

        public boolean isPublish() {
            return publish;
        }

        public void setPublish(boolean publish) {
            this.publish = publish;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPublished() {
            return published;
        }

        public void setPublished(int published) {
            this.published = published;
        }
    }
}
