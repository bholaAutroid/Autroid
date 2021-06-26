package autroid.business.model.request;

/**
 * Created by pranav.mittal on 01/20/18.
 */

public class PhoneVerificationRequest {
    private String otp,id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
