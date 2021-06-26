package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 02/27/18.
 */

public class OptionalInfoBE implements Serializable {
    private String contact_no,email,overview;

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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
