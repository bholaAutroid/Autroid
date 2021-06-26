package autroid.business.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class ShowroomProfileBE implements Serializable {

    private String careager_rating,_id,name,username,email,contact_no,joined,avatar_address;
    private Boolean isLiked,is_bookmarked,is_chat_active;
    private ArrayList<Double> coordinates;
    private AccountInfoBE account_info;
    private AddressBE address;
    private BusinessInfoBE business_info;
    private OptionalInfoBE optional_info;
    private SocialiteBE socialite;
    private float total_rating;
    private ArrayList<ThumbnailBE>  business_gallery;
    private ArrayList<TimingsBE> timing;

    public Boolean getIs_chat_active() {
        return is_chat_active;
    }

    public void setIs_chat_active(Boolean is_chat_active) {
        this.is_chat_active = is_chat_active;
    }


    public Boolean getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(Boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public AccountInfoBE getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountInfoBE account_info) {
        this.account_info = account_info;
    }

    public AddressBE getAddress() {
        return address;
    }

    public void setAddress(AddressBE address) {
        this.address = address;
    }

    public String getCareager_rating() {
        return careager_rating;
    }

    public void setCareager_rating(String careager_rating) {
        this.careager_rating = careager_rating;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getAvatar_address() {
        return avatar_address;
    }

    public void setAvatar_address(String avatar_address) {
        this.avatar_address = avatar_address;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public BusinessInfoBE getBusiness_info() {
        return business_info;
    }

    public void setBusiness_info(BusinessInfoBE business_info) {
        this.business_info = business_info;
    }

    public OptionalInfoBE getOptional_info() {
        return optional_info;
    }

    public void setOptional_info(OptionalInfoBE optional_info) {
        this.optional_info = optional_info;
    }

    public SocialiteBE getSocialite() {
        return socialite;
    }

    public void setSocialite(SocialiteBE socialite) {
        this.socialite = socialite;
    }

    public float getTotal_rating() {
        return total_rating;
    }

    public void setTotal_rating(float total_rating) {
        this.total_rating = total_rating;
    }

    public ArrayList<ThumbnailBE> getBusiness_gallery() {
        return business_gallery;
    }

    public void setBusiness_gallery(ArrayList<ThumbnailBE> business_gallery) {
        this.business_gallery = business_gallery;
    }



    public ArrayList<TimingsBE> getTiming() {
        return timing;
    }

    public void setTiming(ArrayList<TimingsBE> timing) {
        this.timing = timing;
    }

}
