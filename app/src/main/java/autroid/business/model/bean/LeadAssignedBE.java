package autroid.business.model.bean;

import java.util.ArrayList;

public class LeadAssignedBE {

    private UserBE user;

    private ArrayList<CustomerRequirementsBE> customer_requirements;

    private String id,status,date;

    Boolean estimation_requested;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEstimation_requested() {
        return estimation_requested;
    }

    public void setEstimation_requested(Boolean estimation_requested) {
        this.estimation_requested = estimation_requested;
    }

    public ArrayList<CustomerRequirementsBE> getCustomer_requirements() {
        return customer_requirements;
    }

    public void setCustomer_requirements(ArrayList<CustomerRequirementsBE> customer_requirements) {
        this.customer_requirements = customer_requirements;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
