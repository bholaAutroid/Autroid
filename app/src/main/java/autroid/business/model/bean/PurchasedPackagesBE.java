package autroid.business.model.bean;

import java.util.ArrayList;

public class PurchasedPackagesBE {
    private String name,description,expired_at,id,created_at;
    private ArrayList<PackagesDiscountBE> discount;
    private CarDetailBE car;
    private PaymentBE payment;
    private VendorInfoBE user;


    public VendorInfoBE getUser() {
        return user;
    }

    public void setUser(VendorInfoBE user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(String expired_at) {
        this.expired_at = expired_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<PackagesDiscountBE> getDiscount() {
        return discount;
    }

    public void setDiscount(ArrayList<PackagesDiscountBE> discount) {
        this.discount = discount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CarDetailBE getCar() {
        return car;
    }

    public void setCar(CarDetailBE car) {
        this.car = car;
    }

    public PaymentBE getPayment() {
        return payment;
    }

    public void setPayment(PaymentBE payment) {
        this.payment = payment;
    }
}
