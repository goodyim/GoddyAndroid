package im.goody.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class BitmapUtils {
    private static final float BITMAP_SCALE = 0.1f;
    private static final float BLUR_RADIUS = 7.5f;

    public static RoundedBitmapDrawable prepareAvatar(Bitmap original, Context context) {
        RoundedBitmapDrawable result = RoundedBitmapDrawableFactory.create(context.getResources(), original);
        result.setCircular(true);
        return result;
    }

    public static Bitmap extractThumbnail(Bitmap original, int size) {
        return ThumbnailUtils.extractThumbnail(original, size, size);
    }

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
