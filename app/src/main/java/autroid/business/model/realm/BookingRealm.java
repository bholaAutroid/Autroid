package autroid.business.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by pranav.mittal on 11/26/17.
 */

public class BookingRealm extends RealmObject {


    private String bookingId,job_no;
    private String shortId;
    private String listing;
    private float price,paidTotal,discount,due,pickupAmount,careager_cash;
    private String vehicleTitle,services,convenience,dated,updatedAt,carImage,providerName,timeSlot,status,primaryStatus,registrationNumber,coupon,discount_type,mUserName,mUserNumber,mUserId,address;
    private String carId,businessName,businessId,packages;
    private RealmList<BookingDetailRealm> bookingDetailRealms;

    private RealmList<SelectedBookingDataRealm> selectedBookingDataRealms;


    public Float getCareager_cash() {
        return careager_cash;
    }

    public void setCareager_cash(Float careager_cash) {
        this.careager_cash = careager_cash;
    }

    public RealmList<BookingDetailRealm> getBookingDetailRealms() {
        return bookingDetailRealms;
    }

    public void setBookingDetailRealms(RealmList<BookingDetailRealm> bookingDetailRealms) {
        this.bookingDetailRealms = bookingDetailRealms;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public Float getDue() {
        return due;
    }

    public void setDue(Float due) {
        this.due = due;
    }

    public Float getPickupAmount() {
        return pickupAmount;
    }

    public void setPickupAmount(Float pickupAmount) {
        this.pickupAmount = pickupAmount;
    }

    public String getPrimaryStatus() {
        return primaryStatus;
    }

    public void setPrimaryStatus(String primaryStatus) {
        this.primaryStatus = primaryStatus;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmUserNumber() {
        return mUserNumber;
    }

    public void setmUserNumber(String mUserNumber) {
        this.mUserNumber = mUserNumber;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getShortId() {
        return shortId;
    }

    public void setShortId(String shortId) {
        this.shortId = shortId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleTitle() {
        return vehicleTitle;
    }

    public void setVehicleTitle(String vehicleTitle) {
        this.vehicleTitle = vehicleTitle;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(Float paidTotal) {
        this.paidTotal = paidTotal;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public String getDated() {
        return dated;
    }

    public void setDated(String dated) {
        this.dated = dated;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public RealmList<SelectedBookingDataRealm> getSelectedBookingDataRealms() {
        return selectedBookingDataRealms;
    }

    public void setSelectedBookingDataRealms(RealmList<SelectedBookingDataRealm> selectedBookingDataRealms) {
        this.selectedBookingDataRealms = selectedBookingDataRealms;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
