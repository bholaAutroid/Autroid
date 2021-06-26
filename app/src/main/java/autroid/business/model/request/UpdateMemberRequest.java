package autroid.business.model.request;

public class UpdateMemberRequest {

    private String booking,technician,advisor,address;

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
