package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.CarDocumentBE;

public class CarDocumentResponse extends BaseResponse{

    @SerializedName("responseData")
    ArrayList<CarDocumentBE> carDocuments;

    public ArrayList<CarDocumentBE> getCarDocuments() {
        return carDocuments;
    }
}
