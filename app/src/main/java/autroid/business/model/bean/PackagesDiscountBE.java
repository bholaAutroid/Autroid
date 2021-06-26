package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackagesDiscountBE implements Serializable {
    @SerializedName("for")
    private String forDiscount;
    private String label,type,title;
    private int discount,limit,remains;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForDiscount() {
        return forDiscount;
    }

    public void setForDiscount(String forDiscount) {
        this.forDiscount = forDiscount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRemains() {
        return remains;
    }

    public void setRemains(int remains) {
        this.remains = remains;
    }
}
