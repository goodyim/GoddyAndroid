package im.goody.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {
    private final SharedPreferences sharedPreferences;

    public PreferencesManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

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

    //region ================= User Id =================

    private static final String USER_ID_KEY = "USER_ID";

    public void saveUserId(int userId) {
        setIntValue(USER_ID_KEY, userId);
    }

    public int getUserId() {
        return sharedPreferences.getInt(USER_ID_KEY, 0);
    }

    //endregion

    //region ================= User Token =================

    private static final String USER_TOKEN_KEY = "USER_TOKEN";

    public void saveUserToken(String userToken) {
        setStringValue(USER_TOKEN_KEY, userToken);
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN_KEY, "");
    }

    //endregion
}