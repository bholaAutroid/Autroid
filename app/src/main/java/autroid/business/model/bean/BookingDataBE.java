package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BookingDataBE implements Serializable {

    String convenience, job_no,booking_no,_status, status,date,time_slot,delivery_date,delivery_time,other_assets,sub_status,recording_address;

    long fuel_level,odometer;

    @SerializedName("user")
    UserBE userData;

    @SerializedName("car")
    CarDetailBE carDetail;

    @SerializedName("approved_payment")
    ServicePaymentBE approved_payment;

    @SerializedName("approved_due")
    DueBE approved_due;

    private ArrayList<JobsLogBE> logs;

    @SerializedName("services")
    ArrayList<ServiceBE> service;

    @SerializedName("insurance_info")
    InsuranceDataBE insuranceData;

    @SerializedName("customer_requirements")
     ArrayList<CustomerRequirementsBE> requirements;

    @SerializedName("qc")
    ArrayList<JobsQCBE> jobsQCBEArrayList;

    UserBE business;

    UserBE advisor;

    UserBE driver;

    UserBE technician;

    UserBE surveyor;

    ArrayList<ParticularsDataBE> assets;

    AddressBE address;

    ArrayList<RemarkBE> remarks;


    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public ArrayList<JobsLogBE> getLogs() {
        return logs;
    }

    public void setLogs(ArrayList<JobsLogBE> logs) {
        this.logs = logs;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public ArrayList<JobsQCBE> getJobsQCBEArrayList() {
        return jobsQCBEArrayList;
    }

    public String getRecording_address() {
        return recording_address;
    }

    public String getSub_status() {
        return sub_status;
    }

    public String getOther_assets() {
        return other_assets;
    }

    public InsuranceDataBE getInsuranceData() {
        return insuranceData;
    }

    public void setInsuranceData(InsuranceDataBE insuranceData) {
        this.insuranceData = insuranceData;
    }

    public UserBE getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(UserBE surveyor) {
        this.surveyor = surveyor;
    }

    public UserBE getDriver() {
        return driver;
    }

    public void setDriver(UserBE driver) {
        this.driver = driver;
    }

    public UserBE getTechnician() {
        return technician;
    }

    public void setTechnician(UserBE technican) {
        this.technician = technican;
    }

    public UserBE getAdvisor() {
        return advisor;
    }

    public void setAdvisor(UserBE advisor) {
        this.advisor = advisor;
    }

    public long getFuel() {
        return fuel_level;
    }

    public void setFuel(long fuel_level) {
        this.fuel_level = fuel_level;
    }

    public long getOdometer() {
        return odometer;
    }

    public void setOdometer(long odometer) {
        this.odometer = odometer;
    }

    public ArrayList<ParticularsDataBE> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<ParticularsDataBE> assets) {
        this.assets = assets;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public ArrayList<CustomerRequirementsBE> getRequirements() {
        return requirements;
    }

    public void setRequirements(ArrayList<CustomerRequirementsBE> requirements) {
        this.requirements = requirements;
    }

    public ArrayList<RemarkBE> getRemarks() {
        return remarks;
    }

    public void setRemarks(ArrayList<RemarkBE> remarks) {
        this.remarks = remarks;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ServiceBE> getService() {
        return service;
    }

    public void setService(ArrayList<ServiceBE> service) {
        this.service = service;
    }

    public AddressBE getAddress() {
        return address;
    }

    public void setAddress(AddressBE address) {
        this.address = address;
    }

    public String getBooking_no() {
        return booking_no;
    }

    public void setBooking_no(String booking_no) {
        this.booking_no = booking_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserBE getUserData() {
        return userData;
    }

    public void setUserData(UserBE userData) {
        this.userData = userData;
    }

    public CarDetailBE getCarDetail() {
        return carDetail;
    }

    public void setCarDetail(CarDetailBE carDetail) {
        this.carDetail = carDetail;
    }

    public ServicePaymentBE getPayment() {
        return approved_payment;
    }

    public void setPayment(ServicePaymentBE payment) {
        this.approved_payment = payment;
    }

    public DueBE getDue() {
        return approved_due;
    }

    public String getConvenience() {
        return convenience;
    }

    public void setConvenience(String convenience) {
        this.convenience = convenience;
    }


}
