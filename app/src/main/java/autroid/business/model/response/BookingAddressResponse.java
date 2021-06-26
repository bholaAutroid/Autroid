package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class BookingAddressResponse extends BaseResponse {

    @SerializedName("responseData")
    private UserAddressResponse userAddressResponse;

    public UserAddressResponse getUserAddressResponse() {
        return userAddressResponse;
    }
}
