package autroid.business.model.bean;

public class LeadsBE {
    private String name,contact_no,email,updated_at,created_at,_id,type;
    private String user,source;
    private RemarkBE remark;
    private Boolean important;
    private FollowUpBE follow_up;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public FollowUpBE getFollow_up() {
        return follow_up;
    }

    public void setFollow_up(FollowUpBE follow_up) {
        this.follow_up = follow_up;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public RemarkBE getRemark() {
        return remark;
    }

    public void setRemark(RemarkBE remark) {
        this.remark = remark;
    }
}
