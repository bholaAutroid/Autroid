package autroid.business.camera;

public interface RecyclerViewListener {

    void onItemImage(String url,int index);

    void onImageDelete(String id,int position);

    void onDocumentClick(String fileUrl,int position,String documentType);

    void onDocumentDelete(String id,int position);
}
