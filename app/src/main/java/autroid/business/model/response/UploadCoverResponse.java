package autroid.business.model.response;

/**
 * Created by pranav.mittal on 11/26/17.
 */

public class UploadCoverResponse extends BaseResponse {
    GetCoverUrl responseData;

    public GetCoverUrl getResponseData() {
        return responseData;
    }

    public void setResponseData(GetCoverUrl responseData) {
        this.responseData = responseData;
    }

    public class GetCoverUrl{
        private String id,avatar_address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAvatar_address() {
            return avatar_address;
        }

        public void setAvatar_address(String avatar_address) {
            this.avatar_address = avatar_address;
        }
    }



}
