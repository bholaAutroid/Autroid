package autroid.business.model.request;

public class LeadBookingRequest {

    String advisor,registration_no,variant,lead,convenience,date,time_slot, estimation_requested,requirement;

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public void setEstimation_requested(String estimation_requested) {
        this.estimation_requested = estimation_requested;
    }
}
