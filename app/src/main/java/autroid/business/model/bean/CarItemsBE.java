package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 05/30/17.
 */

public class CarItemsBE implements Serializable{

    private String id,value,logo,name,maker,model,variant;
    private Boolean isChecked=false;

    public CarItemsBE(){

    }

    public CarItemsBE(String model,String id){
        this.model=model;
        this.id=id;
    }    public CarItemsBE(String value,String id,String name){


        this.value=value;
        this.id=id;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
