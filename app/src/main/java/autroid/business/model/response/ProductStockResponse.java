package autroid.business.model.response;

import java.util.ArrayList;

import autroid.business.model.bean.ProductDetailBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductStockResponse extends BaseResponse {

    private GetProducts responseData;

    public GetProducts getResponseData() {
        return responseData;
    }

    public void setResponseData(GetProducts responseData) {
        this.responseData = responseData;
    }

    public class GetProducts{
        private String total,published;
        private ArrayList<ProductDetailBE> products;

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public ArrayList<ProductDetailBE> getProducts() {
            return products;
        }

        public void setProducts(ArrayList<ProductDetailBE> products) {
            this.products = products;
        }
    }
}
