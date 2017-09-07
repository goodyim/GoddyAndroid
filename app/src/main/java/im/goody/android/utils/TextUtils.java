package im.goody.android.utils;

import im.goody.android.data.dto.Deal;

public class TextUtils {
    public static String buildShareText(Deal deal) {
        String url = AppConfig.SHARE_DEALS_URL + deal.getId();
        String tags = "#goody #гуди";
        return deal.getDescription() + "\n\n"
                + url + "\n\n"
                + tags;
    }
}
