package autroid.business.interfaces;

public interface BookingSlotCallback {
    void onSlotClick(Boolean status, String slot);
    void onConvenienceClick(String slot, int charges);
}
