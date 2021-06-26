package autroid.business.model.bean;

import java.io.Serializable;

public class GalleryBE implements Serializable {
    private String id,file_address,type,preview_address,post,file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_address() {
        return file_address;
    }

    public void setFile_address(String file_address) {
        this.file_address = file_address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreview_address() {
        return preview_address;
    }

    public void setPreview_address(String preview_address) {
        this.preview_address = preview_address;
    }
}
