package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.CarDocumentBE;

public class DocumentResponse extends BaseResponse {

    @SerializedName("responseData")
    CarDocumentBE carDocument;

    public CarDocumentBE getCarDocument() {
        return carDocument;
    }
}
