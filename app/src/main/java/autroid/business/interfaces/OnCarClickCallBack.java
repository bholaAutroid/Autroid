package autroid.business.interfaces;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public interface OnCarClickCallBack {

    void onImageClick(String id);
    void onOffClick(String id, Boolean isPublished);
    void onEditButtonClick(int position, String carId);
    void onShareButtonClick(int position, String imagePath,String content,boolean published);
    void onTitleClick(int position, String postedBy, String carId);
    void onDeleteClick(int position, String carId);
    void onLikeClick(int position, String carId);
    void onNavigateClick(int position, String carId);
}
