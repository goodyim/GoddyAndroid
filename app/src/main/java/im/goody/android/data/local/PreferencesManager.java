package im.goody.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {
    private final SharedPreferences sharedPreferences;

    private static final String USER_ID_KEY = "USER_ID";
    private static final String USER_TOKEN_KEY = "USER_TOKEN";
    private static final String FIRST_LAUNCH_KEY = "Goody.firstLaunch";

    public PreferencesManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //region ================= Private methods =================

    private void setStringValue(String keyName, String keyValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyName, keyValue);
        editor.apply();
    }

    private void setIntValue(String keyName, int keyValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(keyName, keyValue);
        editor.apply();
    }

    private void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    //endregion

    //region ================= User =================

    public void saveUserId(int userId) {
        setIntValue(USER_ID_KEY, userId);
    }

    public int getUserId() {
        return sharedPreferences.getInt(USER_ID_KEY, 0);
    }

    public void saveUserToken(String userToken) {
        setStringValue(USER_TOKEN_KEY, userToken);
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN_KEY, "");
    }

    public boolean isTokenPresent() {
        return sharedPreferences.contains(USER_TOKEN_KEY);
    }

    //endregion


    //======= region App prefs =======

    public void saveFirstLaunched() {
        setBooleanValue(FIRST_LAUNCH_KEY, false);
    }

    public boolean isFirstStart() {
        return !sharedPreferences.contains(FIRST_LAUNCH_KEY);
    }

    //endregion
}
