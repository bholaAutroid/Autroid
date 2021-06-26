package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

public class LeadUpdateResponse extends BaseResponse {
    @SerializedName("responseData")
    private UpdateData updateData;

    public UpdateData getUpdateData() {
        return updateData;
    }

    public void setUpdateData(UpdateData updateData) {
        this.updateData = updateData;
    }

    public class UpdateData{
        private String status,color_code,remark;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getColor_code() {
            return color_code;
        }

        public void setColor_code(String color_code) {
            this.color_code = color_code;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
