package autroid.business.model.bean;

import java.util.ArrayList;

public class BookingServicesBE {
    private String id, service, description, inclusions, icon,type,unit;
    private float cost, mrp,labour_cost,part_cost;
    int quantity;
    private int gallery;
    private Boolean customer_approval;
    private ArrayList<PriceBreakupBE> parts;
    private ArrayList<PriceBreakupBE> labour;
    private ArrayList<PriceBreakupBE> opening_fitting;

    public Boolean getCustomer_approval() {
        return customer_approval;
    }

    public void setCustomer_approval(Boolean customer_approval) {
        this.customer_approval = customer_approval;
    }

    public ArrayList<PriceBreakupBE> getParts() {
        return parts;
    }

    public void setParts(ArrayList<PriceBreakupBE> parts) {
        this.parts = parts;
    }

    public ArrayList<PriceBreakupBE> getLabour() {
        return labour;
    }

    public void setLabour(ArrayList<PriceBreakupBE> labour) {
        this.labour = labour;
    }

    public ArrayList<PriceBreakupBE> getOpening_fitting() {
        return opening_fitting;
    }

    public void setOpening_fitting(ArrayList<PriceBreakupBE> opening_fitting) {
        this.opening_fitting = opening_fitting;
    }

    private Boolean isChecked = false,doorstep;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getGallery() {
        return gallery;
    }

    public void setGallery(int gallery) {
        this.gallery = gallery;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getDoorstep() {
        return doorstep;
    }

    public void setDoorstep(Boolean doorstep) {
        this.doorstep = doorstep;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getMrp() {
        return mrp;
    }

    public void setMrp(float mrp) {
        this.mrp = mrp;
    }
}
