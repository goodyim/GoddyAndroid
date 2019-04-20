package im.goody.android.utils;

import java.io.File;

import im.goody.android.BuildConfig;

public class AppConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    // ---------- LOG ----------
    static final boolean LOG_ENABLED = DEBUG;

    // Retrofit config
    private static final String HOST = "http://178.62.14.96/";

    public static final String BASE_URL = HOST+ "api/v1/";
    public static final String SHARE_DEALS_URL = HOST + "good_deals/";

    private static final String IMAGES_BASE_URL = "http://dobrodel.s3.amazonaws.com";
    static final String IMAGES_URl_PATTERN = IMAGES_BASE_URL +
            "/good_deals/images/{0}/medium/{1}";

    public static final String CACHE_PATH_FORMAT = "%s" + File.separator + "goody";

    public static String ENDCODING = "UTF-8";

    public static final int MAX_CONNECTION_TIMEOUT = 15000;
    public static final int MAX_READ_TIMEOUT = 15000;
    public static final int MAX_WRITE_TIMEOUT = 15000;

    public static final int RETRY_REQUEST_COUNT = 3;
    public static final int RETRY_REQUEST_BASE_DELAY = 1000;
}
