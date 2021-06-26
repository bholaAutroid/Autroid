package autroid.business.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import autroid.business.model.bean.BookedPackagesBE;


public class BookedPackageResponse extends BaseResponse {

    @SerializedName("responseData")
    private ArrayList<BookedPackagesBE> bookedPackagesBES;

    public ArrayList<BookedPackagesBE> getBookedPackagesBES() {
        return bookedPackagesBES;
    }

    public void setBookedPackagesBES(ArrayList<BookedPackagesBE> bookedPackagesBES) {
        this.bookedPackagesBES = bookedPackagesBES;
    }
}
