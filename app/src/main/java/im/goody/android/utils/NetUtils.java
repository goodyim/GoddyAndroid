package im.goody.android.utils;

import android.net.Uri;

import java.text.MessageFormat;

import im.goody.android.data.dto.Deal;

public class NetUtils {
    public static String buildDealImageUrl(Deal deal) {
        String fileName =  Uri.encode(deal.getImageUrl());
        return MessageFormat.format(AppConfig.IMAGES_URl_PATTERN, deal.getId(), fileName);
    }
}
