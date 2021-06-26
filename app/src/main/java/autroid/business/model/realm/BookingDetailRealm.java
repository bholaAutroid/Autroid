package autroid.business.model.realm;

import io.realm.RealmObject;

public class BookingDetailRealm extends RealmObject {
    private String tag,id,packageName,cost,details,type,part_cost,labour_cost;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPart_cost() {
        return part_cost;
    }

    public void setPart_cost(String part_cost) {
        this.part_cost = part_cost;
    }

    public String getLabour_cost() {
        return labour_cost;
    }

    public void setLabour_cost(String labour_cost) {
        this.labour_cost = labour_cost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
