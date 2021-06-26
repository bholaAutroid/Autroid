package autroid.business.interfaces;

public interface PurchaseCarCallback {
    void onShareButtonClick(String carName,String carId,String carImage);
    void onTitleClick(int position,String postedBy,String carId);
    void onPublisherClick(String car,String carId,String type);
    void onChatClick(String id);

}
