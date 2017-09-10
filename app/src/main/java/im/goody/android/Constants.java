package im.goody.android;

public class Constants {
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_NAME_LENGTH = 4;
    public static final int FAB_HIDE_THRESHOLD = 5;
    public static final int DEFAULT_ANIMATION_DURATION = 200;
    public static final int COLLAPSED_CHARACTERS_COUNT = 200;
    public static final int REGISTER_AVATAR_SIZE = 80; //dp

    public static final int PAGE_ITEM_COUNT = 15;

    public static int[] PROGRESS_COLORS = {
            R.color.accent,
            R.color.primary
    };

    public static class Welcome {
        public static final int PAGES_COUNT = 3;
        public static final int[] TITLES = {
                R.string.welcome_title_1,
                R.string.welcome_title_2,
                R.string.welcome_title_3
        };

        public static final int[] DESCRIPTIONS = {
                R.string.welcome_description_1,
                R.string.welcome_description_2,
                R.string.welcome_description_3
        };

        public static final int[] COLORS = {
                R.color.accent,
                R.color.primary,
                R.color.auth_facebook_background
        };

        public static final int[] ICONS = {
                R.drawable.ic_add,
                R.drawable.ic_comment,
                R.drawable.ic_heart
        };
    }

    public static class Pattern {
        public static final String OPEN_MAP = "geo:0,0?q=%s";
    }
}
