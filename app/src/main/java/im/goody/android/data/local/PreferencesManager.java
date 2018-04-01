package im.goody.android.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import im.goody.android.data.dto.Settings;
import im.goody.android.data.network.res.UserRes;

public class PreferencesManager {
    private final SharedPreferences sharedPreferences;

    private static final String USER_ID_KEY = "USER_ID";
    private static final String USER_NAME_KEY = "USER_NAME";
    private static final String USER_TOKEN_KEY = "USER_TOKEN";
    private static final String USER_IMAGE_URL_KEY = "USER_IMAGE_URL";

    private static final String FIRST_LAUNCH_KEY = "Goody.firstLaunch";

    private static final String ALERT_FROM_SUBSCRIBER_KEY = "ALERT_FROM_SUBSCRIBER";
    private static final String ALERT_FROM_NEARBY_KEY = "ALERT_FROM_NEARBY";
    private static final String NOTIFY_MENTIONS_KEY = "NOTIFY_MENTIONS";
    private static final String NOTIFY_MESSAGES_KEY = "NOTIFY_MESSAGES";

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

    private void removeValue(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
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

    //endregion


    //region ======= App prefs =======

    public void saveFirstLaunched() {
        setBooleanValue(FIRST_LAUNCH_KEY, false);
    }

    public boolean isFirstStart() {
        return !sharedPreferences.contains(FIRST_LAUNCH_KEY);
    }

    public void clearUserData() {
        removeValue(USER_NAME_KEY);
        removeValue(USER_IMAGE_URL_KEY);
        removeValue(USER_ID_KEY);
        removeValue(USER_TOKEN_KEY);
    }

    //endregion

    //region ================= Settings =================

    public void saveSettings(Settings settings) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ALERT_FROM_SUBSCRIBER_KEY, settings.isAlertFromSubscriber());
        editor.putBoolean(ALERT_FROM_NEARBY_KEY, settings.isAlertFromNearby());
        editor.putBoolean(NOTIFY_MENTIONS_KEY, settings.isNotifyMentions());
        editor.putBoolean(NOTIFY_MESSAGES_KEY, settings.isNotifyMessages());
        editor.apply();
    }

    public Settings getSettings() {
        return new Settings(
                sharedPreferences.getBoolean(ALERT_FROM_SUBSCRIBER_KEY, false),
                sharedPreferences.getBoolean(ALERT_FROM_NEARBY_KEY, false),
                sharedPreferences.getBoolean(NOTIFY_MENTIONS_KEY, false),
                sharedPreferences.getBoolean(NOTIFY_MESSAGES_KEY, false)
        );
    }

    //endregion
}
