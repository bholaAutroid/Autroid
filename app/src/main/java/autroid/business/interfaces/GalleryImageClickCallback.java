package autroid.business.interfaces;

import java.util.ArrayList;

import autroid.business.model.bean.ThumbnailBE;

public interface GalleryImageClickCallback {

    void onGalleryClick(ArrayList<ThumbnailBE> mImages);

}
