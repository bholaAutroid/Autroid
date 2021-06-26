package autroid.business.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductDetailBE implements Serializable {
    private String id,title,price,created_at,subcategory,description,price_normal,price_words;
    private ProductCatSubcatBE category;
    private ProductDetailRequestBE detail;

    private boolean publish;



    private ArrayList<ThumbnailBE> thumbnails;
    private VendorInfoBE business;

    public ProductDetailRequestBE getDetail() {
        return detail;
    }

    public void setDetail(ProductDetailRequestBE detail) {
        this.detail = detail;
    }

    public VendorInfoBE getBusiness() {
        return business;
    }

    public void setBusiness(VendorInfoBE business) {
        this.business = business;
    }

    public ProductCatSubcatBE getCategory() {
        return category;
    }

    public void setCategory(ProductCatSubcatBE category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice_normal() {
        return price_normal;
    }

    public void setPrice_normal(String price_normal) {
        this.price_normal = price_normal;
    }

    public String getPrice_words() {
        return price_words;
    }

    public void setPrice_words(String price_words) {
        this.price_words = price_words;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<ThumbnailBE> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ArrayList<ThumbnailBE> thumbnails) {
        this.thumbnails = thumbnails;
    }


}
