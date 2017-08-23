package im.goody.android.screens.register;

import android.databinding.BindingAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.widget.ImageView;

public class RegisterBindingAdapter {
    @BindingAdapter("selected")
    public static void bindAvatar(ImageView imageView, RoundedBitmapDrawable drawable) {
        if (drawable != null) {
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageDrawable(drawable);
        }
    }
}
