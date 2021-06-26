package autroid.business.model.request;

public class LeadPriorityRequest {

   private String lead;

   private int priority;

   boolean contacted;

    public void setContacted(boolean contacted) {
        this.contacted = contacted;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
