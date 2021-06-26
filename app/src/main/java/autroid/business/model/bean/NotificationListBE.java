package autroid.business.model.bean;

public class NotificationListBE {

    private ShowroomProfileBE user;
    private ShowroomProfileBE sender;
    private String activity,created_at,source,title,body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ShowroomProfileBE getUser() {
        return user;
    }

    public void setUser(ShowroomProfileBE user) {
        this.user = user;
    }

    public ShowroomProfileBE getSender() {
        return sender;
    }

    public void setSender(ShowroomProfileBE sender) {
        this.sender = sender;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
