package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

public class BookingCreatedDetailsBE {

    @SerializedName("car")
    String carId;

    @SerializedName("user")
    String userId;

    @SerializedName("booking")
    String bookingId;


    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

}
