package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.ProductDetailBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class AddProductResponse extends BaseResponse {

    @SerializedName("responseData")
    private GetProductResponse getProductResponse;

    public class GetProductResponse{
        private String next,errors;
        private boolean rld;
        private ProductDetailBE item;

        public ProductDetailBE getItem() {
            return item;
        }

        public void setItem(ProductDetailBE item) {
            this.item = item;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public String getErrors() {
            return errors;
        }

        public void setErrors(String errors) {
            this.errors = errors;
        }

        public boolean isRld() {
            return rld;
        }

        public void setRld(boolean rld) {
            this.rld = rld;
        }
    }

    public GetProductResponse getGetProductResponse() {
        return getProductResponse;
    }

    public void setGetProductResponse(GetProductResponse getProductResponse) {
        this.getProductResponse = getProductResponse;
    }
}
