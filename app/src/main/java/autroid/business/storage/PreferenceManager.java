package autroid.business.storage;

import android.content.Context;

import autroid.business.utils.Utility;


/**
 * Class is the manager class to store data in shared prefrences
 */

public class PreferenceManager {
    public static final String AUTHORIZATION = "authorization";
    public static final String KEY_AUTHTOKEN = "auth_token";


    private static PreferenceManager preferenceManager;
    private static android.content.SharedPreferences sharedPreferencesObj;
    private static android.content.SharedPreferences.Editor editor;
    private static final String PREFERENCE_NAME = "CarEagerVendor";

    private PreferenceManager() {
    }

    public synchronized static PreferenceManager getInstance() {
        if (preferenceManager == null)
            preferenceManager = new PreferenceManager();
        return preferenceManager;
    }

    private  android.content.SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferencesObj == null)
            sharedPreferencesObj = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferencesObj;
    }

    private  android.content.SharedPreferences.Editor getPreferenceEditor(Context context) {
        if (editor == null)
            editor = getSharedPreferences(context).edit();
        return editor;
    }

    public  void putStringPreference(Context context, String key, String value) {
        getPreferenceEditor(context).putString(key, value).commit();
    }

    public String getStringPreference(Context context, String key) {
        return getSharedPreferences(context).getString(key, Utility.EMPTY_STRING);
    }

    public  void putIntegerPreference(Context context, String key, int value) {
        getPreferenceEditor(context).putInt(key, value).commit();
    }

    public  int getIntegerPreference(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    public  void putBooleanPreference(Context context, String key, boolean value) {
        getPreferenceEditor(context).putBoolean(key, value).commit();
    }

    public  boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public void clearAllPrefernces(Context mcontext){
        getSharedPreferences(mcontext).edit().clear().commit();
    }

}
