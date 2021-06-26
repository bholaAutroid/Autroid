package autroid.business.model.bean;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 08/15/17.
 */

public class CarModelBE {

    private String id,model;
    private ArrayList<CarVariantBE> variant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<CarVariantBE> getVariant() {
        return variant;
    }

    public void setVariant(ArrayList<CarVariantBE> variant) {
        this.variant = variant;
    }
}
