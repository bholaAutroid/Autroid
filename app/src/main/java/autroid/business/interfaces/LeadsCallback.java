package autroid.business.interfaces;

public interface LeadsCallback {
    void onStatusClick(String id);
    void onImportantClick(String id);
    void onChatClick(String id);
    void onCallClick(String number);
    void onConvertLead(String name,String email,String contact,String source);
}
