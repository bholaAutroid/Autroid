package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.ProductCatSubcatBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductCategoryResponse extends BaseResponse {

    private ArrayList<ProductCatSubcatBE> responseData;

    public ArrayList<ProductCatSubcatBE> getResponseData() {
        return responseData;
    }

    public void setResponseData(ArrayList<ProductCatSubcatBE> responseData) {
        this.responseData = responseData;
    }
}
