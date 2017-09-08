package im.goody.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import im.goody.android.R;

public class BitmapUtils {
    public static RoundedBitmapDrawable prepareAvatar(Bitmap original, Context context) {
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(context.getResources(), original);
        result.setCircular(true);
        return result;
    }

    public static Bitmap extractTumbnail(Bitmap original, int size) {
        return ThumbnailUtils.extractThumbnail(original, size, size);
    }
}
