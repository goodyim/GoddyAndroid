package im.goody.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import im.goody.android.data.network.res.UserRes;

public class PreferencesManager {
    private final SharedPreferences sharedPreferences;

    private static final String USER_ID_KEY = "USER_ID";
    private static final String USER_NAME_KEY = "USER_NAME";
    private static final String USER_TOKEN_KEY = "USER_TOKEN";
    private static final String USER_IMAGE_URL_KEY = "USER_IMAGE_URL";

    private static final String FILL_PROFILE_KEY = "FILL_PROFILE";

    public static final String SETTINGS_MENTIONS_KEY = "mention_notifications";
    public static final String SETTINGS_COMMENTS_KEY = "comment_notifications";
    public static final String SETTINGS_FINISHED_EVENT = "finished_event";
    public static final String SETTINGS_NEW_PARTICIPATOR = "new_participator";
    public static final String SETTINGS_NEW_FOLLOWEE = "new_followee_event";

    private static final boolean DEFAULT_BOOLEAN_SETTING = true;

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

    private void removeValues(String... keys) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    //endregion

    //region ================= User =================

    public void saveUser(UserRes userRes) {
        UserRes.User user = userRes.getUser();

        setStringValue(USER_NAME_KEY, user.getName());
        setStringValue(USER_IMAGE_URL_KEY, user.getAvatarUrl());
        setIntValue(USER_ID_KEY, user.getId());
        setStringValue(USER_TOKEN_KEY, user.getToken());
    }

    public String getUserAvatarUrl() {
        return sharedPreferences.getString(USER_IMAGE_URL_KEY, "");
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME_KEY, "");
    }

    public int getUserId() {
        return sharedPreferences.getInt(USER_ID_KEY, 0);
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN_KEY, "");
    }

    public boolean isTokenPresent() {
        return sharedPreferences.contains(USER_TOKEN_KEY);
    }

    public boolean isProfileFilled() {
        return sharedPreferences.getBoolean(FILL_PROFILE_KEY, true);
    }

    public void setProfileFilled(boolean isFilled) {
        setBooleanValue(FILL_PROFILE_KEY, isFilled);
    }

    //endregion


    //======= region App prefs =======

    public void clearUserData() {
        removeValues(
                USER_NAME_KEY,
                USER_IMAGE_URL_KEY,
                USER_ID_KEY,
                USER_TOKEN_KEY);
    }

    //endregion


    //region ================= Settings =================

    public boolean isSettingEnabled(String key) {
        return sharedPreferences.getBoolean(key, DEFAULT_BOOLEAN_SETTING);
    }

    //
}
