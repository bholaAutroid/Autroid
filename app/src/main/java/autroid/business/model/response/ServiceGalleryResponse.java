package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.GalleryBE;


public class ServiceGalleryResponse extends BaseResponse {
    @SerializedName("responseData")
    ArrayList<GalleryBE> galleryBES;

    public ArrayList<GalleryBE> getGalleryBES() {
        return galleryBES;
    }

    public void setGalleryBES(ArrayList<GalleryBE> galleryBES) {
        this.galleryBES = galleryBES;
    }
}
