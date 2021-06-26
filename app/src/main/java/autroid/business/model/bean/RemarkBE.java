package autroid.business.model.bean;

public class RemarkBE {
    private String status,customer_remark,assignee_remark,id, updated_at,remark,date,name,resource;

    private UserBE assignee;

    public String getName() {
        return name;
    }

    public String getResource() {
        return resource;
    }

    public UserBE getAssignee() {
        return assignee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer_remark() {
        return customer_remark;
    }

    public void setCustomer_remark(String customer_remark) {
        this.customer_remark = customer_remark;
    }

    public String getAssignee_remark() {
        return assignee_remark;
    }

    public void setAssignee_remark(String assignee_remark) {
        this.assignee_remark = assignee_remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
