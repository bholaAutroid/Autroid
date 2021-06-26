package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import autroid.business.model.bean.CarDetailBE;

/**
 * Created by pranav.mittal on 06/12/17.
 */

public class AddCarResponse extends BaseResponse {

    @SerializedName("responseData")
    private GetCarResponse getCarResponse;

    public class GetCarResponse{
        private String next,errors;
        private boolean rld;
        private CarDetailBE item;

        public CarDetailBE getItem() {
            return item;
        }

        public void setItem(CarDetailBE item) {
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

    public GetCarResponse getGetCarResponse() {
        return getCarResponse;
    }

    public void setGetCarResponse(GetCarResponse getCarResponse) {
        this.getCarResponse = getCarResponse;
    }
}
