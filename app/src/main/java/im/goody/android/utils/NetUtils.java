package im.goody.android.utils;

import java.text.MessageFormat;

import im.goody.android.data.dto.Deal;

public class NetUtils {
    public static String buildDealImageUrl(Deal deal) {
        return MessageFormat.format(AppConfig.IMAGES_URl_PATTERN, deal.getId(), deal.getImageUrl());
    }
}
