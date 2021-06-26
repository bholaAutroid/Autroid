package autroid.business.interfaces;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public interface OnClickCallBack {

    void onImageClick(String id);
    void onPublishUnPublishClick(String id);
    void onEditButtonClick(String id,String status);
    void onShareButtonClick(String id);
    void onTitleClick(String id);


}
