package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class InspectionDataBE {

    @SerializedName("id")
    String imageId;

    int index;

    @SerializedName("file_address")
    String imageAddress;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImageAddress() {
        return imageAddress;
    }

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }
}
