package im.goody.android;

public class Constants {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_NAME_LENGTH = 4;
    public static final int FAB_HIDE_THRESHOLD = 5;
    public static final int DEFAULT_ANIMATION_DURATION = 200;
    public static final int COLLAPSED_CHARACTERS_COUNT = 200;

    public static final int TITLE_CHARACTERS_COUNT = 37;

    public static final String DATE_FORMAT = "%02d.%02d.%d %02d:%02d";

    public static final int ONE_MILLION = 1_000_000;
    public static final int ONE_THOUSAND = 1_000;

    public static final int PAGE_ITEM_COUNT = 15;

    public static final int SECOND = 1000;

    public static int[] PROGRESS_COLORS = {
            R.color.accent,
            R.color.primary
    };

    public static class Pattern {
        public static final String OPEN_MAP = "geo:0,0?q=%s";
    }

    public static final String ID_NONE = "none";

    public static final String CACHE_FILE_NAME = "goddy_temp.jpeg";

    public static final String NUMBER_PATTERN = "[-+]?\\d*\\.?\\d+";
    public static final String MENTION_FORMAT = "@%s";
}
