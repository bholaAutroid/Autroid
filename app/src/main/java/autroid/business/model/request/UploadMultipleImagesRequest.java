package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/17/17.
 */

public class UploadMultipleImagesRequest {
    private String id,filename,media;

    public String getCar_id() {
        return id;
    }

    public void setId(String car_id) {
        this.id = car_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
