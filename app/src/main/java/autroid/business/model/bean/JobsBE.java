package autroid.business.model.bean;

public class JobsBE {
    private String id,delivery_date,created_at,_status,job_no,booking_no,time_left,updated_at;
    private CarDetailBE car;
    private UserBE user;

    public String getBooking_no() {
        return booking_no;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTime_left() {
        return time_left;
    }

    public void setTime_left(String time_left) {
        this.time_left = time_left;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        this._status = status;
    }

    public CarDetailBE getCar() {
        return car;
    }

    public void setCar(CarDetailBE car) {
        this.car = car;
    }

    public UserBE getUser() {
        return user;
    }

    public void setUser(UserBE user) {
        this.user = user;
    }
}
