package im.goody.android.screens.register;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.widget.ImageView;

import im.goody.android.ui.views.CircleImageView;
import im.goody.android.utils.BitmapUtils;

public class RegisterBindingAdapter {
    @BindingAdapter("selected")
    public static void bindAvatar(CircleImageView imageView, Bitmap bmp) {
        if (bmp != null) {
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageBitmap(BitmapUtils.extractTumbnail(bmp, imageView.getWidth()));
        }
    }
}
