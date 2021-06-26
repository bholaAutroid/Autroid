package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarItemsBE;
import autroid.business.model.bean.ServiceCategoryBE;
import autroid.business.model.bean.TelephoneCodeBE;

/**
 * Created by pranav.mittal on 08/08/17.
 */

public class RegistrationDataResponse extends BaseResponse {

    @SerializedName("responseData")
    private GetRegistrationData getRegistrationData;

    public class GetRegistrationData{
        ArrayList<ServiceCategoryBE> category;
        ArrayList<CarItemsBE> automaker;
        ArrayList<TelephoneCodeBE> country;

        public ArrayList<TelephoneCodeBE> getCountry() {
            return country;
        }

        public void setCountry(ArrayList<TelephoneCodeBE> country) {
            this.country = country;
        }

        public ArrayList<ServiceCategoryBE> getCategory() {
            return category;
        }

        public void setCategory(ArrayList<ServiceCategoryBE> category) {
            this.category = category;
        }

        public ArrayList<CarItemsBE> getAutomaker() {
            return automaker;
        }

        public void setAutomaker(ArrayList<CarItemsBE> automaker) {
            this.automaker = automaker;
        }
    }

    public GetRegistrationData getGetRegistrationData() {
        return getRegistrationData;
    }

    public void setGetRegistrationData(GetRegistrationData getRegistrationData) {
        this.getRegistrationData = getRegistrationData;
    }
}
