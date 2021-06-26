package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.AddressBE;

public class AddressResponse extends BaseResponse{

    @SerializedName("responseData")
    AddressBE address;

    public AddressBE getAddress() {
        return address;
    }
}
