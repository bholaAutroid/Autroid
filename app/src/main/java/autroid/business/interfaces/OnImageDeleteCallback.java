package autroid.business.interfaces;

/**
 * Created by pranav.mittal on 06/24/17.
 */

public interface OnImageDeleteCallback {

    void onDeleteClick(String position);
    void onDeleteImageClickId(String id);
    void onAddImageClick(int position);
}
