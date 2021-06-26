package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 02/27/18.
 */

public class AccountInfoBE implements Serializable {
    private String type,status;
    private Boolean approved_by_admin,phone_verified,verified_account;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getApproved_by_admin() {
        return approved_by_admin;
    }

    public void setApproved_by_admin(Boolean approved_by_admin) {
        this.approved_by_admin = approved_by_admin;
    }

    public Boolean getPhone_verified() {
        return phone_verified;
    }

    public void setPhone_verified(Boolean phone_verified) {
        this.phone_verified = phone_verified;
    }

    public Boolean getVerified_account() {
        return verified_account;
    }

    public void setVerified_account(Boolean verified_account) {
        this.verified_account = verified_account;
    }
}
