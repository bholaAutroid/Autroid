package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 03/02/18.
 */

public class ProductCatSubcatBE implements Serializable {
    private String _id,category,id;
    private boolean is_show;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_show() {
        return is_show;
    }

    public void setIs_show(boolean is_show) {
        this.is_show = is_show;
    }
}
