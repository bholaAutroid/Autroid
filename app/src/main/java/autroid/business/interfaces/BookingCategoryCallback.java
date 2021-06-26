package autroid.business.interfaces;

public interface BookingCategoryCallback {
    void onCategoryClick(String name,String tag,Boolean nested);
    void onDisableCategoryClick(String name,String tag,Boolean nested);

    void onServicesSelected(String id,String name,String tag,String type,Float cost,Float mrp,int quantity,String unit,Boolean isDoorstep);
    void onServicesUnselect(String id);
    void updateQuantity(String id,int quantity);
    void onGalleryClick(String id,String type);
    void onRightSwipe();}
