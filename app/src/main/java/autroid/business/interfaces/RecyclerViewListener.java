package autroid.business.interfaces;

import android.net.Uri;

public interface RecyclerViewListener {

    void onDocumentClick(Uri downloadUri,String sender,String attachmentName);

    void onImageClick(Uri uri);

    void onMapClick(String address);

    void onLinkClick(String url);

    void onPhrasesClick(int position);

}
