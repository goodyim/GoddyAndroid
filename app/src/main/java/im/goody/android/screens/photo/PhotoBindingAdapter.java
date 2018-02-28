package im.goody.android.screens.photo;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import im.goody.android.App;

public class PhotoBindingAdapter {
    @BindingAdapter("photo")
    public static void bindPhoto(ImageView imageView, String url) {
        App.picasso.load(url)
                .into(imageView);
    }
}
