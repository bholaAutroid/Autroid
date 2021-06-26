package autroid.business.interfaces;

public interface BookingsClickCallback {
    void onReBookClick(String businessId, String businessName, String carID, String carName, String status);
    void onCancelClick(String bookingID, String status);
    void onOrderCancelClick(String bookingID, String status);
    void onDetailClick(String bookingID);
    void onOrderDetailClick(String bookingID);
    void onRescheduleClick(String bookingID, String status, String businessId);
    void onBusinessClick(String businessId);
    void onInvoiceClick(String businessId);
    void onPayDue(String bookingId, String status, float due, String type);
}
