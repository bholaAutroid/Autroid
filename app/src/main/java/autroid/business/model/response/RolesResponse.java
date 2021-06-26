package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.RoleBE;

public class RolesResponse extends BaseResponse {

    @SerializedName("responseData")
    ArrayList<RoleBE> roleData;

    public ArrayList<RoleBE> getRoleData() {
        return roleData;
    }
}
