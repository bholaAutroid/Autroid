package autroid.business.model.bean;

public class LeadStatusBE {
    private String status,follow_up_period,color_code,id,created_at;
    private Boolean is_follow_up;
    private int count;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFollow_up_period() {
        return follow_up_period;
    }

    public void setFollow_up_period(String follow_up_period) {
        this.follow_up_period = follow_up_period;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIs_follow_up() {
        return is_follow_up;
    }

    public void setIs_follow_up(Boolean is_follow_up) {
        this.is_follow_up = is_follow_up;
    }
}
