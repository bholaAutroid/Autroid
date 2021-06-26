package autroid.business.model.bean;

import java.util.ArrayList;

public class LeadDetailBE {

    String id, user, name, contact_no, email, source, created_at, updated_at, last_active, booking, category;

    int priority;

    boolean contacted;

    FollowUpBE follow_up;

    RemarkBE remark;

    UserBE assignee, advisor;

    ArrayList<RemarkBE> logs;

    public String getCategory() {
        return category;
    }

    public String getBooking() {
        return booking;
    }

    public String getLast_active() {
        return last_active;
    }

    public FollowUpBE getFollow_up() {
        return follow_up;
    }

    public boolean isContacted() {
        return contacted;
    }

    public int getPriority() {
        return priority;
    }

    public ArrayList<RemarkBE> getLogs() {
        return logs;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserBE getAssignee() {
        return assignee;
    }

    public UserBE getAdvisor() {
        return advisor;
    }

    public RemarkBE getRemark() {
        return remark;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail() {
        return email;
    }

    public String getSource() {
        return source;
    }

}
