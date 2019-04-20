package im.goody.android.screens.photo;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoBindingAdapter {
    @BindingAdapter("photo")
    public static void bindPhoto(ImageView imageView, String url) {
        Picasso.get()
                .load(url)
                .into(imageView);
    }
}
