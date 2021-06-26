package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class UpdateProfileRequest {
    private String name,secondary_contact_no,secondary_email,overview;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondary_contact_no() {
        return secondary_contact_no;
    }

    public void setSecondary_contact_no(String secondary_contact_no) {
        this.secondary_contact_no = secondary_contact_no;
    }

    public String getSecondary_email() {
        return secondary_email;
    }

    public void setSecondary_email(String secondary_email) {
        this.secondary_email = secondary_email;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
