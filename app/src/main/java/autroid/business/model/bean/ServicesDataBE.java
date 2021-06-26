package autroid.business.model.bean;

public class ServicesDataBE {

    String service,type;

    float cost,labour_cost,part_cost;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getLabour_cost() {
        return labour_cost;
    }

    public void setLabour_cost(float labour_cost) {
        this.labour_cost = labour_cost;
    }

    public float getPart_cost() {
        return part_cost;
    }

    public void setPart_cost(float part_cost) {
        this.part_cost = part_cost;
    }
}
