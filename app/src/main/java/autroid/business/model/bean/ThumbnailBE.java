package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 06/10/17.
 */

public class ThumbnailBE implements Serializable {
    private String car_id,file,file_address,id,mark_as,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

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

    public String getMark_as() {
        return mark_as;
    }

    public void setMark_as(String mark_as) {
        this.mark_as = mark_as;
    }
}
