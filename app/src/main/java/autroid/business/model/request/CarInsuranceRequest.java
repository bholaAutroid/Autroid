package autroid.business.model.request;

import java.io.Serializable;

public class CarInsuranceRequest implements Serializable {

    private String registrationNumber = "", make = "", model = "", variant = "", dateOfRegistration = "", registrationYear = "", policyNumber = "", prevPolicyEndDate = "", chasisNumber = "";

    private String firstName = "", lastName = "", mobileNumber = "", emailId = "", address = "";

    private String city = "", state = "", pincode = "";

    private String insuranceType="",ncb="";

    private boolean isPrevPolicy;

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public void setPrevPolicyEndDate(String prevPolicyEndDate) {
        this.prevPolicyEndDate = prevPolicyEndDate;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrevPolicy(boolean prevPolicy) {
        isPrevPolicy = prevPolicy;
    }

    public void setChasisNumber(String chasisNumber) {
        this.chasisNumber = chasisNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getVariant() {
        return variant;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getPrevPolicyEndDate() {
        return prevPolicyEndDate;
    }

    public String getChasisNumber() {
        return chasisNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public boolean isPrevPolicy() {
        return isPrevPolicy;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public String getNcb() {
        return ncb;
    }

    public void setNcb(String ncb) {
        this.ncb = ncb;
    }
}
