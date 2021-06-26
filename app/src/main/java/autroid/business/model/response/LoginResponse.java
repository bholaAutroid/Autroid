package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.ManagementBE;
import autroid.business.model.bean.UserBE;

/**
 * Created by pranav.mittal on 05/18/17.
 */

public class LoginResponse extends BaseResponse {

    @SerializedName("responseData")
    GetLoginData getLoginData;

    public GetLoginData getGetLoginData() {
        return getLoginData;
    }

    public void setGetLoginData(GetLoginData getLoginData) {
        this.getLoginData = getLoginData;
    }

    public class GetLoginData{
        private String token,status;
        private UserBE user;
        private ArrayList<ManagementBE> management;

        public ArrayList<ManagementBE> getManagement() {
            return management;
        }

        public void setManagement(ArrayList<ManagementBE> management) {
            this.management = management;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public UserBE getUser() {
            return user;
        }

        public void setUser(UserBE user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
