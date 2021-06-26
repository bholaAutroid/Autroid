package autroid.business.model.bean;

import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class ServiceCategoryBE {
    private String id,category;
    private boolean is_company;
    private ArrayList<ServiceBE>  services;

    private String categoryName;
    private String categoryIcon;

    public boolean isIs_company() {
        return is_company;
    }

    public void setIs_company(boolean is_company) {
        this.is_company = is_company;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<ServiceBE> getServices() {
        return services;
    }

    public void setServices(ArrayList<ServiceBE> services) {
        this.services = services;
    }
}
