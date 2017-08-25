package im.goody.android;

public class Constants {
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MIN_NAME_LENGTH = 4;
    public static final int FAB_HIDE_THRESHOLD = 5;

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
}
