package im.goody.android.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import im.goody.android.utils.BitmapUtils;

public class CircleImageView extends AppCompatImageView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        post(()-> setBitmap(bm, getWidth(), getHeight()));
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {

            Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();

            post(() -> setBitmap(bmp, getWidth(), getHeight()));
        } else {
            super.setImageDrawable(drawable);
        }
    }

    private void setBitmap(Bitmap bm, int width, int height) {
        Bitmap scaled = Bitmap.createScaledBitmap(bm, width, height, false);

        Drawable result = BitmapUtils.prepareAvatar(scaled, getContext());
        super.setImageDrawable(result);
    }
}
