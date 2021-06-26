package autroid.business.model.bean;

import java.io.Serializable;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class UserBE implements Serializable {

    private String uuid,approved_by_admin,avatar,careager_rating,category,city,company,contact_no,cover,created_at,device_id,avatar_url,avatar_address,joined;
    private String email,gcm_id,gender,location,name,otp,overview,package_subscription_on,package_upgradation,phone_verified,secondary_contact_no,secondary_email;
    private String state,status,subscribed_package,type,updated_at,username,verified_account,zip,cover_address;
    private AddressBE address;
    private String _id;
    private UserPointsBE points;
    private SocialiteBE socialite;
    private AccountInfoBE account_info;
    private OptionalInfoBE optional_info;
    private BusinessInfoBE business_info;

    private int followers,followings,totalPost,totalViews;
    private Boolean isCarEager;
    private PartnerBE partner;

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getAvatar_address() {
        return avatar_address;
    }

    public void setAvatar_address(String avatar_address) {
        this.avatar_address = avatar_address;
    }

    public int getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(int totalViews) {
        this.totalViews = totalViews;
    }

    public SocialiteBE getSocialite() {
        return socialite;
    }

    public void setSocialite(SocialiteBE socialite) {
        this.socialite = socialite;
    }

    public UserPointsBE getPoints() {
        return points;
    }

    public void setPoints(UserPointsBE points) {
        this.points = points;
    }

    public AccountInfoBE getAccount_info() {
        return account_info;
    }

    public void setAccount_info(AccountInfoBE account_info) {
        this.account_info = account_info;
    }

    public OptionalInfoBE getOptional_info() {
        return optional_info;
    }

    public void setOptional_info(OptionalInfoBE optional_info) {
        this.optional_info = optional_info;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    public void setPartner(PartnerBE partner) {
        this.partner = partner;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Boolean getCarEager() {
        return isCarEager;
    }

    public void setCarEager(Boolean carEager) {
        isCarEager = carEager;
    }

    public String getCover_address() {
        return cover_address;
    }

    public void setCover_address(String cover_address) {
        this.cover_address = cover_address;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }


    public AddressBE getAddress() {
        return address;
    }

    public void setAddress(AddressBE address) {
        this.address = address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }


    public String getApproved_by_admin() {
        return approved_by_admin;
    }

    public void setApproved_by_admin(String approved_by_admin) {
        this.approved_by_admin = approved_by_admin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCareager_rating() {
        return careager_rating;
    }

    public void setCareager_rating(String careager_rating) {
        this.careager_rating = careager_rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPackage_subscription_on() {
        return package_subscription_on;
    }

    public void setPackage_subscription_on(String package_subscription_on) {
        this.package_subscription_on = package_subscription_on;
    }

    public String getPackage_upgradation() {
        return package_upgradation;
    }

    public void setPackage_upgradation(String package_upgradation) {
        this.package_upgradation = package_upgradation;
    }

    public String getPhone_verified() {
        return phone_verified;
    }

    public void setPhone_verified(String phone_verified) {
        this.phone_verified = phone_verified;
    }

    public String getSecondary_contact_no() {
        return secondary_contact_no;
    }

    public void setSecondary_contact_no(String secondary_contact_no) {
        this.secondary_contact_no = secondary_contact_no;
    }

    public String getSecondary_email() {
        return secondary_email;
    }

    public void setSecondary_email(String secondary_email) {
        this.secondary_email = secondary_email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscribed_package() {
        return subscribed_package;
    }

    public void setSubscribed_package(String subscribed_package) {
        this.subscribed_package = subscribed_package;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerified_account() {
        return verified_account;
    }

    public void setVerified_account(String verified_account) {
        this.verified_account = verified_account;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public PartnerBE getPartner() {
        return partner;
    }

    public BusinessInfoBE getBusiness_info() {
        return business_info;
    }

    public void setBusiness_info(BusinessInfoBE business_info) {
        this.business_info = business_info;
    }
}
