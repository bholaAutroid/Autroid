package autroid.business.model.request;

import com.google.gson.annotations.SerializedName;

public class SurveyorRequest {

    @SerializedName("claim_no")
    String claimNo;

    @SerializedName("policy_type")
    String policyType;

    @SerializedName("surveyor")
    String surveyorId;

    @SerializedName("surveyor_name")
    String surveyorName;

    @SerializedName("surveyor_contact_no")
    String surveyorContact;

    @SerializedName("surveyor_email")
    String email;

    String booking,branch;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public void setSurveyorId(String surveyorId) {
        this.surveyorId = surveyorId;
    }

    public void setSurveyorName(String surveyorName) {
        this.surveyorName = surveyorName;
    }

    public void setSurveyorContact(String surveyorContact) {
        this.surveyorContact = surveyorContact;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
