package autroid.business.interfaces;

/**
 * Created by pranav.mittal on 11/22/17.
 */

public interface BookingStatusCallback {
     void confirmBooking(int pos,String bookingId);
     void rejectBooking(int pos,String bookingId);
    void onDetailClick(String bookingID,String status);
    void onCallClick(String number);
    void onChatClick(String userId);
    void onRescheduleClick(String bookingID,String status);
    void completeBooking(int pos,String bookingId);
    void onAddressClick(String address,String convenience);
    void createLead(String name,String mobile,String email,String bookingId);
    void createJobCard(String userId,String bookingId);
}
