package im.goody.android.utils;

import im.goody.android.BuildConfig;

public class AppConfig {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    // ---------- LOG ----------
    static final boolean LOG_ENABLED = DEBUG;

    // Retrofit config
    public static final String BASE_URL = "http://";
    public static final int MAX_CONNECTION_TIMEOUT = 10000;
    public static final int MAX_READ_TIMEOUT = 10000;
    public static final int MAX_WRITE_TIMEOUT = 5000;

    public static final int RETRY_REQUEST_COUNT = 3;
    public static final int RETRY_REQUEST_BASE_DELAY = 1000;
}
