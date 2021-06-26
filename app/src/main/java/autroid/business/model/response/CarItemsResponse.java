package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import autroid.business.model.bean.CarItemsBE;

/**
 * Created by pranav.mittal on 05/30/17.
 */

public class CarItemsResponse extends BaseResponse implements Serializable {

    @SerializedName("responseData")
    private GetCarItems getCarItems;

    public GetCarItems getGetCarItems() {
        return getCarItems;
    }

    public void setGetCarItems(GetCarItems getCarItems) {
        this.getCarItems = getCarItems;
    }
    public class GetCarItems implements Serializable{
        private ArrayList<CarItemsBE> automaker;
        private ArrayList<CarItemsBE> body_style;
        private ArrayList<CarItemsBE> color;
        private ArrayList<CarItemsBE> fuel_type;
        private ArrayList<CarItemsBE> transmissions;
        private ArrayList<CarItemsBE> owner;
        private ArrayList<CarItemsBE> models;


        public ArrayList<CarItemsBE> getModels() {
            return models;
        }

        public void setModels(ArrayList<CarItemsBE> models) {
            this.models = models;
        }

        public ArrayList<CarItemsBE> getAutomaker() {
            return automaker;
        }

        public void setAutomaker(ArrayList<CarItemsBE> automaker) {
            this.automaker = automaker;
        }

        public ArrayList<CarItemsBE> getBody_style() {
            return body_style;
        }

        public void setBody_style(ArrayList<CarItemsBE> body_style) {
            this.body_style = body_style;
        }

        public ArrayList<CarItemsBE> getColor() {
            return color;
        }

        public void setColor(ArrayList<CarItemsBE> color) {
            this.color = color;
        }

        public ArrayList<CarItemsBE> getFuel_type() {
            return fuel_type;
        }

        public void setFuel_type(ArrayList<CarItemsBE> fuel_type) {
            this.fuel_type = fuel_type;
        }

        public ArrayList<CarItemsBE> getTransmissions() {
            return transmissions;
        }

        public void setTransmissions(ArrayList<CarItemsBE> transmissions) {
            this.transmissions = transmissions;
        }

        public ArrayList<CarItemsBE> getOwner() {
            return owner;
        }

        public void setOwner(ArrayList<CarItemsBE> owner) {
            this.owner = owner;
        }
    }
}
