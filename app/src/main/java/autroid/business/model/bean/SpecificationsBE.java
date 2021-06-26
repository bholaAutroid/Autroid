package autroid.business.model.bean;

import java.io.Serializable;

public class SpecificationsBE implements Serializable {

    String id,value;

    boolean selected;

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public boolean isSelected() {
        return selected;
    }
}
