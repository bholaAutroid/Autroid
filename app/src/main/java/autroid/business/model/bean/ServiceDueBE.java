package autroid.business.model.bean;

import java.util.ArrayList;

public class ServiceDueBE {

    private UserBE user;
    private CarDetailBE car;
    private String service_reminder,id;

    private ArrayList<RemarkBE> remarks;

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }

    public CarDetailBE getCar() {
        return car;
    }

    public void setCar(CarDetailBE car) {
        this.car = car;
    }

    public String getService_reminder() {
        return service_reminder;
    }

    public void setService_reminder(String service_reminder) {
        this.service_reminder = service_reminder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<RemarkBE> getRemarks() {
        return remarks;
    }

    public void setRemarks(ArrayList<RemarkBE> remarks) {
        this.remarks = remarks;
    }
}
