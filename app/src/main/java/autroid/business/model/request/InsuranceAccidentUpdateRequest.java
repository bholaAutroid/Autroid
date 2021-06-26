package autroid.business.model.request;

public class InsuranceAccidentUpdateRequest {

    private String contact_no,booking, policy_no, branch, expire, gstin, insurance_company, policy_holder, policy_type, claim_no, driver_accident, accident_place, accident_date, accident_time, accident_cause, spot_survey, fir, state, manufacture_year;

    private boolean claim,cashless;

    private int premium;

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getPolicy_no() {
        return policy_no;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setPolicy_no(String policy_no) {
        this.policy_no = policy_no;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public void setInsurance_company(String insurance_company) {
        this.insurance_company = insurance_company;
    }

    public void setPolicy_holder(String policy_holder) {
        this.policy_holder = policy_holder;
    }

    public void setPremium(int premium) {
        this.premium = premium;
    }

    public void setPolicy_type(String policy_type) {
        this.policy_type = policy_type;
    }

    public void setClaim_no(String claim_no) {
        this.claim_no = claim_no;
    }

    public void setDriver_accident(String driver_accident) {
        this.driver_accident = driver_accident;
    }

    public void setAccident_place(String accident_place) {
        this.accident_place = accident_place;
    }

    public void setAccident_date(String accident_date) {
        this.accident_date = accident_date;
    }

    public void setAccident_time(String accident_time) {
        this.accident_time = accident_time;
    }

    public void setAccident_cause(String accident_cause) {
        this.accident_cause = accident_cause;
    }

    public void setSpot_survey(String spot_survey) {
        this.spot_survey = spot_survey;
    }

    public void setFir(String fir) {
        this.fir = fir;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setManufacture_year(String manufacture_year) {
        this.manufacture_year = manufacture_year;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }

    public void setCashless(boolean cashless) {
        this.cashless = cashless;
    }
}

