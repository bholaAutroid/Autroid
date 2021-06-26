package autroid.business.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class ServiceBE implements Serializable {

    private String id,service,mileage,description,source;
    private String inclusions,type,pay_by;
    private int quantity=1;
    private float cost,labour_cost,part_cost,discount,of_cost,exceeded_cost;
    private boolean isChecked=false,customer_approval,surveyor_approval,isSelected=false,isExpanded=false,claim,custom,labour_cost_editable,of_cost_editable,part_cost_editable;
    private ArrayList<PriceBreakupBE> parts;
    private ArrayList<PriceBreakupBE> labour;
    private ArrayList<PriceBreakupBE> opening_fitting;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSurveyor_approval() {
        return surveyor_approval;
    }

    public void setSurveyor_approval(boolean surveyor_approval) {
        this.surveyor_approval = surveyor_approval;
    }

    public float getExceeded_cost() {
        return exceeded_cost;
    }

    public void setExceeded_cost(float exceeded_cost) {
        this.exceeded_cost = exceeded_cost;
    }

    public String getPay_by() {
        return pay_by;
    }

    public void setPay_by(String pay_by) {
        this.pay_by = pay_by;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getOf_cost() {
        return of_cost;
    }

    public void setOf_cost(float of_cost) {
        this.of_cost = of_cost;
    }

    public boolean isClaim() {
        return claim;
    }

    public void setClaim(boolean claim) {
        this.claim = claim;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public boolean isLabour_cost_editable() {
        return labour_cost_editable;
    }

    public void setLabour_cost_editable(boolean labour_cost_editable) {
        this.labour_cost_editable = labour_cost_editable;
    }

    public boolean isOf_cost_editable() {
        return of_cost_editable;
    }

    public void setOf_cost_editable(boolean of_cost_editable) {
        this.of_cost_editable = of_cost_editable;
    }

    public boolean isPart_cost_editable() {
        return part_cost_editable;
    }

    public void setPart_cost_editable(boolean part_cost_editable) {
        this.part_cost_editable = part_cost_editable;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isCustomer_approval() {
        return customer_approval;
    }

    public void setCustomer_approval(boolean customer_approval) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInclusions() {
        return inclusions;
    }

    public void setInclusions(String inclusions) {
        this.inclusions = inclusions;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

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
}
