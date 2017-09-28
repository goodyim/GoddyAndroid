package im.goody.android.utils;

import java.util.Locale;

import im.goody.android.Constants;
import im.goody.android.data.dto.Deal;

public class TextUtils {
    private static final String THOUSAND_FORMAT = "%.2fK";
    private static final String MILLION_FORMAT = "%.2fM";

    public static String buildShareText(Deal deal) {
        String url = AppConfig.SHARE_DEALS_URL + deal.getId();
        String tags = "#goody #гуди";
        String title = deal.getTitle();

        if (android.text.TextUtils.isEmpty(title) || "null".equals(title))
            title = "";
        else
            title += "\n";

        return title
                + deal.getDescription() + "\n\n"
                + url + "\n\n"
                + tags;
    }

    public static String formatCount(int count) {
        String result;

        if (count <= 0) {
            result = null;
        } else if (count > Constants.ONE_MILLION) {
            result = String.format(Locale.ENGLISH, MILLION_FORMAT, count / (Constants.ONE_MILLION * 1.0));
        } else if (count > Constants.ONE_THOUSAND) {
            result = String.format(Locale.ENGLISH, THOUSAND_FORMAT, count / (Constants.ONE_MILLION * 1.0));
        } else {
            result = String.valueOf(count);
        }

        return result;
    }

    public static boolean isEmpty(String s) {
        return android.text.TextUtils.isEmpty(s) || "null".equals(s);
    }
}
