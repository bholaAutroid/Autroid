package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetailBE {

    @SerializedName("items")
    private ArrayList<ItemDataBE> itemData;

    @SerializedName("payment")
    private ServicePaymentBE payment;

    @SerializedName("due")
    private DueBE due;

    @SerializedName("user")
    private UserBE userData;

    @SerializedName("car")
    private CarDetailBE carDetail;

    @SerializedName("address")
    private AddressBE addressData;

    private String convenience,order_no,delivery_date,status;

    public String getStatus() {
        return status;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }

    public UserBE getUserData() {
        return userData;
    }

    public void setUserData(UserBE userData) {
        this.userData = userData;
    }

    public ArrayList<ItemDataBE> getItemData() {
        return itemData;
    }

    public void setItemData(ArrayList<ItemDataBE> itemData) {
        this.itemData = itemData;
    }

    public ServicePaymentBE getPayment() {
        return payment;
    }

    public void setPayment(ServicePaymentBE payment) {
        this.payment = payment;
    }

    public DueBE getDue() {
        return due;
    }

    public void setDue(DueBE due) {
        this.due = due;
    }

    public CarDetailBE getCarDetail() {
        return carDetail;
    }

    public AddressBE getAddressData() {
        return addressData;
    }
}
