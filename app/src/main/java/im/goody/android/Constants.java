package im.goody.android;

public class Constants {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_NAME_LENGTH = 4;
    public static final int FAB_HIDE_THRESHOLD = 5;
    public static final int DEFAULT_ANIMATION_DURATION = 200;
    public static final int COLLAPSED_CHARACTERS_COUNT = 200;

    public static final int TITLE_CHARACTERS_COUNT = 37;

    public static final String DATE_TIME_FORMAT = "%02d.%02d.%d %02d:%02d";
    public static final String DATE_FORMAT = "%02d.%02d.%d";

    public static final String COORDINATES_FORMAT = "%s %s";

    public static final int ONE_MILLION = 1_000_000;
    public static final int ONE_THOUSAND = 1_000;

    public static final int PAGE_ITEM_COUNT = 15;

    public static final int SECOND = 1000;

    public static final int SEX_MALE = 1;
    public static final int SEX_FEMALE = 2;

    public static final int CACHED_IMAGE_HEIGHT = 720;
    public static final int CACHED_IMAGE_WIDTH = 1280;

    public static int[] PROGRESS_COLORS = {
            R.color.accent,
            R.color.primary
    };

    public static class Pattern {
        public static final String OPEN_MAP = "geo:0,0?q=%s";
    }

    public static final String ID_NONE = "none";

    public static final String CACHE_FILE_NAME = "goddy_temp.jpeg";

    public static final String MENTION_FORMAT = "@%s ";

    public static class NotificationExtra {
        public static final String TYPE = "type";
        public static final String TYPE_COMMENT = "type_comment";
        public static final String TYPE_MENTION = "mention";
        public static final String TYPE_NEW_EVENT = "new_event";
        public static final String TYPE_CLOSE_EVENT = "close_event";
        public static final String TYPE_NEW_PARTICIPATOR = "new_participator";
        public static final String TYPE_FOLLOWEE_NEW_EVENT = "followee_new_event";
        public static final String TYPE_PHONE_REQUEST = "phone_request";

        public static final String ID = "id";
        public static final String MESSAGE = "message";
        public static final String AUTHOR_NAME = "author";
        public static final String TITLE = "title";
        public static final String TAGS = "tags";
    }
}
