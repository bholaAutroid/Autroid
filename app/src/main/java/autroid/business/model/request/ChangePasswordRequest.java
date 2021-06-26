package autroid.business.model.request;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class ChangePasswordRequest {
    private String old_password,password;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
