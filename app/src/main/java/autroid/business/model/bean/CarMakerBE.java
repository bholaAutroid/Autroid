package autroid.business.model.bean;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 08/15/17.
 */

public class CarMakerBE {

    private String id,maker,logo;
    private ArrayList<CarModelBE> models;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ArrayList<CarModelBE> getModels() {
        return models;
    }

    public void setModels(ArrayList<CarModelBE> models) {
        this.models = models;
    }
}
