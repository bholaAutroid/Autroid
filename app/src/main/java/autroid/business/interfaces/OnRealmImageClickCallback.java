package autroid.business.interfaces;

/**
 * Created by pranav.mittal on 02/02/18.
 */

public interface OnRealmImageClickCallback {
     void onImageClick(int pos, String id);
     void onDetailClick(int pos,String des);
     void onDetailClick(String id,String type);

     void onTraveloguePagerClick(int pos, String id);
}
