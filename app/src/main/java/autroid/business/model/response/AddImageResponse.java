package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;


/**
 * Created by pranav.mittal on 03/03/18.
 */

public class AddImageResponse extends BaseResponse {

    @SerializedName("responseData")
    private ImageUploaded imageUploaded;

    public ImageUploaded getImageUploaded() {
        return imageUploaded;
    }

    public void setImageUploaded(ImageUploaded imageUploaded) {
        this.imageUploaded = imageUploaded;
    }

    public class ImageUploaded{
        private GetItem item;
        private String file_address;

        public String getFile_address() {
            return file_address;
        }

        public void setFile_address(String file_address) {
            this.file_address = file_address;
        }

        public GetItem getItem() {
            return item;
        }

        public void setItem(GetItem item) {
            this.item = item;
        }

        public class GetItem{
            private String file_address,id;

            public String getFile_address() {
                return file_address;
            }

            public void setFile_address(String file_address) {
                this.file_address = file_address;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
